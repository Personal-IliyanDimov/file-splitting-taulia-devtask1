package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.transformer.context.FileContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.helper.SplitHelper;
import com.taulia.devtask1.transformer.context.helper.SplitSourceSelector;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class SplittingConsumer<T> implements TransformerConsumer<T> {

    private final TransformerContext context;
    private final SplitHelper helper;
    private SplitSourceSelector<T, String> splitSourceSelector;
    private Function<String, Long> splitFunction;
    private Map<Long, FileContext> childSplitIndexToFileContext;

    public SplittingConsumer(TransformerContext context) {
        this.context = context;
        this.helper = new SplitHelper();
        initialize(context);
    }

    private void initialize(TransformerContext context) {
        final Split.SplitDetails splitDetails = context.getCurrentSplit().getSplitDetails();

        final long splitFactor = splitDetails.getSplitFactor();
        final Function<Long, Function<String, Long>> factory = splitDetails.getSplitFunctionFactory();

        this.splitSourceSelector = context.getSplitSourceSelector();
        this.splitFunction = factory.apply(splitFactor);
        this.childSplitIndexToFileContext = new HashMap<>();
    }

    @Override
    public Split[] process(TransformerInputReader<T> inputReader) throws Exception {
        Consumer<T> recordsConsumer = getRecordsConsumer();
        try {
            inputReader.process(recordsConsumer);
        }
        finally {
            closeStreams();
        }

        final Split[] splitArray = prepareSplits();
        return splitArray != null ? splitArray : new Split[0];
    }

    private void closeStreams() throws IOException {
        final List<Exception> list = new LinkedList<>();

        for (Map.Entry<Long, FileContext> entry : childSplitIndexToFileContext.entrySet()) {
            try {
                final FileContext fileContext = entry.getValue();
                fileContext.getOrCreateOutputWriter().close();
            } catch (Exception exc) {
                log.error("Unable to close file. ", exc);
                list.add(exc);
            }
        }

        if (! list.isEmpty()) {
            throw new IOException("Multiple io exceptions occurred during clean up: " + list.toString());
        }
    }

    private Consumer<T> getRecordsConsumer() {
        return t -> routeInvoice(t);
    }

    private void routeInvoice(T t) {
        final String splitSource = splitSourceSelector.splitSource(t);
        final long childSplitIndex = splitFunction.apply(splitSource);
        if (childSplitIndexToFileContext.containsKey(childSplitIndex)) {
            appendToContext(childSplitIndexToFileContext.get(childSplitIndex), t);
        }
        else {

            final FileContext fileContext = context.nextSplitContext();
            try {
                fileContext.getOrCreateOutputWriter().init();
            } catch (IOException ioe) {
                throw new RuntimeException("Unable to create output writer: " + fileContext, ioe);
            }

            childSplitIndexToFileContext.put(childSplitIndex, fileContext);
            appendToContext(fileContext, t);
        }
    }

    private void appendToContext(FileContext fileContext, T t) {
        try {
            fileContext.getOrCreateOutputWriter().process(t);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to append data to file context: " + fileContext, ioe);
        }
    }

    public Split[] prepareSplits() throws IOException {
        List<Split> result = new ArrayList<>();

        for (Map.Entry<Long, FileContext> entry : childSplitIndexToFileContext.entrySet()) {
            final FileContext fileContext = entry.getValue();
            final Split split = helper.buildSplit(fileContext.getOutputFile(), context.getCurrentSplit(), context.getConfig());
            result.add(split);
        }

        return result.toArray(new Split[0]);
    }
}

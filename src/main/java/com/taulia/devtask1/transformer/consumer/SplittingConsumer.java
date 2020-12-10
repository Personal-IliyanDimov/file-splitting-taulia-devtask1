package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.transformer.consumer.io.OutputInfo;
import com.taulia.devtask1.transformer.context.FileContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.helper.SplitHelper;
import com.taulia.devtask1.transformer.context.helper.SplitSourceSelector;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
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

    private final TransformerContext<T> context;
    private final SplitHelper helper;
    private SplitSourceSelector<T, String> splitSourceSelector;
    private Function<String, Long> splitFunction;
    private Map<Long, OutputInfo<T>> childSplitIndexToOutputinfoMap;

    public SplittingConsumer(TransformerContext<T> context) {
        this.context = context;
        this.helper = new SplitHelper();
        initialize(context);
    }

    private void initialize(TransformerContext<T> context) {
        final Split.SplitDetails splitDetails = context.getCurrentSplit().getSplitDetails();

        final long splitFactor = splitDetails.getSplitFactor();
        final Function<Long, Function<String, Long>> factory = splitDetails.getSplitFunctionFactory();

        this.splitSourceSelector = context.getSplitSourceSelector();
        this.splitFunction = factory.apply(splitFactor);
        this.childSplitIndexToOutputinfoMap = new HashMap<>();
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

        for (Map.Entry<Long, OutputInfo<T>> entry : childSplitIndexToOutputinfoMap.entrySet()) {
            try {
                final OutputInfo<T> outputInfo = entry.getValue();
                outputInfo.getOutputWriter().close();
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
        if (childSplitIndexToOutputinfoMap.containsKey(childSplitIndex)) {
            appendToContext(childSplitIndexToOutputinfoMap.get(childSplitIndex), t);
        }
        else {

            final FileContext fileContext = context.nextSplitContext();
            final File outputFile = context.getFileNameProducer().apply(fileContext);
            TransformerOutputWriter<T> outputWriter = null;

            try {
                outputWriter = context.getIoContext().buildWriter(outputFile);
                outputWriter.init();
            } catch (IOException ioe) {
                throw new RuntimeException("Unable to create output writer: " + fileContext, ioe);
            }

            final OutputInfo<T> childSplitOutputInfo = new OutputInfo<>(outputFile, outputWriter);
            childSplitIndexToOutputinfoMap.put(childSplitIndex, childSplitOutputInfo);
            appendToContext(childSplitOutputInfo, t);
        }
    }

    private void appendToContext(OutputInfo<T> outputInfo, T t) {
        try {
            outputInfo.getOutputWriter().process(t);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to append data to output: " + outputInfo, ioe);
        }
    }

    public Split[] prepareSplits() {
        List<Split> result = new ArrayList<>();

        for (Map.Entry<Long, OutputInfo<T>> entry : childSplitIndexToOutputinfoMap.entrySet()) {
            final OutputInfo<T> fileContext = entry.getValue();
            final Split split = helper.buildSplit(fileContext.getOutputFile(), context.getCurrentSplit(), context.getConfig());
            result.add(split);
        }

        return result.toArray(new Split[0]);
    }
}

package com.taulia.devtask1.transformer.splitter;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;
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
public class SplittingConsumer implements TransformerConsumer {

    private final TransformerContext context;
    private final SplitHelper helper;
    private Function<String, Long> splitFunction = null;
    private Map<Long, TransformerContext.FileContext> childSplitIndexToFileContext;

    public SplittingConsumer(TransformerContext context) {
        this.context = context;
        this.helper = new SplitHelper();
        initialize(context);
    }

    private void initialize(TransformerContext context) {
        final Split currentSplit = context.getCurrentSplit();
        final long splitFactor = currentSplit.getSplitDetails().getSplitFactor();
        final Function<Long, Function<String, Long>> factory = currentSplit.getSplitDetails().getSplitFunctionFactory();

        this.splitFunction = factory.apply(splitFactor);
        this.childSplitIndexToFileContext = new HashMap<>();
    }

    @Override
    public Consumer<InvoiceRecord> getRecordsConsumer() {
        return r -> routeInvoice(r);
    }

    @Override
    public Split[] process() throws Exception {
        final Split[] splitArray = doProcess();
        return splitArray;
    }

    @Override
    public void finish() throws Exception {
        doCleanUp();
    }

    private void doCleanUp() throws IOException {
        final List<Exception> list = new LinkedList<>();

        for (Map.Entry<Long, TransformerContext.FileContext> entry : childSplitIndexToFileContext.entrySet()) {
            try {
                final TransformerContext.FileContext fileContext = entry.getValue();
                fileContext.getOrCreateOutputWriter().close();
            } catch (Exception exc) {
                log.error("Unable to close file. ", exc);
                list.add(exc);
            }
        }
        childSplitIndexToFileContext.clear();


        if (! list.isEmpty()) {
            throw new IOException("Multiple io exceptions occurred during clean up: " + list.toString());
        }
    }

    private void routeInvoice(InvoiceRecord invoiceRecord) {
        final long childSplitIndex = splitFunction.apply(invoiceRecord.getBuyer());
        if (childSplitIndexToFileContext.containsKey(childSplitIndex)) {
            appendInvoice(childSplitIndexToFileContext.get(childSplitIndex), invoiceRecord);
        }
        else {

            final TransformerContext.FileContext fileContext = context.nextSplitContext();
            try {
                fileContext.getOrCreateOutputWriter().init();
            } catch (IOException ioe) {
                throw new RuntimeException("Unable to create output writer: " + fileContext, ioe);
            }

            childSplitIndexToFileContext.put(childSplitIndex, fileContext);
            appendInvoice(fileContext, invoiceRecord);
        }
    }

    private void appendInvoice(TransformerContext.FileContext fileContext, InvoiceRecord invoiceRecord) {
        try {
            fileContext.getOrCreateOutputWriter().process(invoiceRecord, prepareImageContext());
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to append data to file context: " + fileContext, ioe);
        }
    }

    private OutputWriter.ImageContext prepareImageContext() {
        return new OutputWriter.ImageContext() {
            @Override
            public File generateFileName() {
                return context.nextImageFile();
            }
        };
    }

    public Split[] doProcess() throws IOException {
        List<Split> result = new ArrayList<>();

        for (Map.Entry<Long, TransformerContext.FileContext> entry : childSplitIndexToFileContext.entrySet()) {
            final TransformerContext.FileContext fileContext = entry.getValue();
            final Split split = helper.buildSplit(fileContext.getOutputFile(), context.getCurrentSplit(), context.getConfig());
            result.add(split);
        }

        return result.toArray(new Split[0]);
    }
}

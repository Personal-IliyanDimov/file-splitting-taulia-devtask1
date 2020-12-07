package com.taulia.devtask1.transformer.memory;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.splitter.Split;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InMemoryConsumer implements TransformerConsumer {
    private final TransformerContext context;
    private Map<String, List<InvoiceRecord>> buyerToInvoiceListMap = null;

    public InMemoryConsumer(TransformerContext context) {
        this.context = context;
        this.buyerToInvoiceListMap = new HashMap<>();
    }

    @Override
    public Split[] process(InputReader<InvoiceRecord> inputReader) throws Exception {
        Consumer<InvoiceRecord> recordsConsumer = getRecordsConsumer();
        try {
            inputReader.process(recordsConsumer);
            doProcess();
        }
        finally {
            finish();
        }

        return new Split[0];
    }

    private Consumer<InvoiceRecord> getRecordsConsumer() {
        return r -> {
            List<InvoiceRecord> list = buyerToInvoiceListMap.get(r.getBuyer());
            if (list == null) {
                list = new LinkedList<>();
                buyerToInvoiceListMap.put(r.getBuyer(), list);
            }
            list.add(r);

        };
    }

    private void doProcess() throws Exception {
        for (Map.Entry<String, List<InvoiceRecord>> entry : buyerToInvoiceListMap.entrySet()) {
            final TransformerContext.FileContext fileContext = context.nextBuyerContext();
            OutputWriter<InvoiceRecord> outputWriter = null;
            try {
                outputWriter = fileContext.getOrCreateOutputWriter();
                outputWriter.init();

                final List<InvoiceRecord> recordList = entry.getValue();
                for (InvoiceRecord record : recordList) {
                    outputWriter.process(record, prepareImageContext());
                }

                outputWriter.end();
            }
            finally {
                if (outputWriter != null) {
                    outputWriter.close();
                }
            }
        }
    }

    private void finish() throws Exception {
        buyerToInvoiceListMap.clear();
    }

    private OutputWriter.ImageContext prepareImageContext() {
        return new OutputWriter.ImageContext() {
            @Override
            public File generateFileName() {
                return context.nextImageFile();
            }
        };
    }
}

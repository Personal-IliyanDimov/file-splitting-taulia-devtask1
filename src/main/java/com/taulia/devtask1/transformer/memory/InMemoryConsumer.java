package com.taulia.devtask1.transformer.memory;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;

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
    public Consumer<InvoiceRecord> getRecordsConsumer() {
        return r -> {
            List<InvoiceRecord> list = buyerToInvoiceListMap.get(r.getBuyer());
            if (list == null) {
                list = new LinkedList<>();
                buyerToInvoiceListMap.put(r.getBuyer(), list);
            }
            list.add(r);

        };
    }

    @Override
    public void process() throws Exception {
        doProcess();
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
                    outputWriter.process(record);
                }

                outputWriter.end();
            }
            finally {
                outputWriter.close();
            }
        }
    }
}

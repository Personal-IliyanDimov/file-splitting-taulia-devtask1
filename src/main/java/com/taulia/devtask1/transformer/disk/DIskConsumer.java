package com.taulia.devtask1.transformer.disk;

import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DIskConsumer implements TransformerConsumer {
    private final TransformerContext context;

    private Map<String, TransformerContext.FileContext> buyerToFileContext = null;
    private List<InvoiceRecord> initialRecords = null;
    private List<InvoiceRecord> otherRecords = null;


    public DIskConsumer(TransformerContext context) {
        this.context = context;

        initialRecords = new LinkedList<>();
        otherRecords = new LinkedList<>();
        buyerToFileContext = new HashMap<>();
    }

    @Override
    public Consumer<InvoiceRecord> getRecordsConsumer() {
        return r -> initialRecords.add(r);
    }

    @Override
    public void process() {
        doProcess();
    }

    private boolean doProcess() {
        while (! initialRecords.isEmpty()) {

            for (InvoiceRecord invoiceRecord: initialRecords) {
                final InvoiceRecord record = initialRecords.remove(0);
                routeInvoice(invoiceRecord);
            }

            // close files for the already handled buyers


        }
    }

    private void routeInvoice(InvoiceRecord invoiceRecord) {
        final String buyer = invoiceRecord.getBuyer();
        if (buyerToFileContext.containsKey(buyer)) {
            appendInvoice(invoiceRecord);
        }
        else {
              if (buyerToFileContext.keySet().size() < context.getConfig().getMaxOpenHandlers()) {
                  final TransformerContext.FileContext fileContext = context.nextBuyerContext();
                  try {
                      fileContext.getOrCreateOutputWriter().init();
                  } catch (IOException ioe) {
                      throw new RuntimeException("Unable to create output writer: " + fileContext, ioe);
                  }

                  buyerToFileContext.put(buyer, fileContext);
                  appendInvoice(invoiceRecord);
              }
              else {
                  otherRecords.add(invoiceRecord);
              }
        }
    }

    private void appendInvoice(InvoiceRecord invoiceRecord) {
        final String buyer = invoiceRecord.getBuyer();
        final TransformerContext.FileContext fileContext = buyerToFileContext.get(buyer);
        try {
            fileContext.getOrCreateOutputWriter().process(invoiceRecord);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to append data to file context: " + fileContext, ioe);
        }
    }
}

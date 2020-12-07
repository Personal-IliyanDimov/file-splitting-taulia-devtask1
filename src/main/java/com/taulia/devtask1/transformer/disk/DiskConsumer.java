package com.taulia.devtask1.transformer.disk;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.splitter.Split;
import com.taulia.devtask1.transformer.splitter.SplitHelper;
import com.taulia.devtask1.transformer.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DiskConsumer implements TransformerConsumer {
    private final TransformerContext context;
    private final SplitHelper splitHelper;

    private Map<String, TransformerContext.FileContext> buyerToFileContext;
    private TransformerContext.FileContext otherFileContext;

    public DiskConsumer(TransformerContext context) {
        this.context = context;
        this.splitHelper = new SplitHelper();
        this.buyerToFileContext = new HashMap<>();
    }

    @Override
    public Split[] process(InputReader<InvoiceRecord> inputReader) throws Exception {
        Consumer<InvoiceRecord> recordsConsumer = getRecordsConsumer();
        try {
            inputReader.process(recordsConsumer);
        }
        finally {
            closeStreams();
        }
        final Split split = prepareSplit();
        return split != null ? new Split[] { split } : new Split[0];
    }

    private void closeStreams() throws IOException {
        final List<Exception> list = new LinkedList<>();

        for (Map.Entry<String, TransformerContext.FileContext> entry : buyerToFileContext.entrySet()) {
            try {
                final TransformerContext.FileContext fileContext = entry.getValue();
                fileContext.getOrCreateOutputWriter().close();
            } catch (Exception exc) {
                log.error("Unable to close file. ", exc);
                list.add(exc);
            }
        }

        if (otherFileContext != null) {
            try {
                otherFileContext.getOrCreateOutputWriter().close();
            } catch (Exception exc) {
                log.error("Unable to close file. ", exc);
                list.add(exc);
            }
        }

        if (! list.isEmpty()) {
            throw new IOException("Multiple io exceptions occurred during clean up: " + list.toString());
        }
    }

    private Consumer<InvoiceRecord> getRecordsConsumer() {
        return r -> routeInvoice(r);
    }

    private void routeInvoice(InvoiceRecord invoiceRecord) {
        final String buyer = invoiceRecord.getBuyer();
        if (buyerToFileContext.containsKey(buyer)) {
            appendInvoice(buyerToFileContext.get(buyer), invoiceRecord);
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
                  appendInvoice(fileContext, invoiceRecord);
              }
              else {

                  if (otherFileContext == null) {
                      final TransformerContext.FileContext otherFileContext = context.nextOtherContext();
                      try {
                          otherFileContext.getOrCreateOutputWriter().init();
                      } catch (IOException ioe) {
                          throw new RuntimeException("Unable to create output writer: " + otherFileContext, ioe);
                      }
                  }

                  appendInvoice(otherFileContext, invoiceRecord);
              }
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

    public Split prepareSplit() throws IOException {
        if (otherFileContext == null) {
            return null;
        }

        final Split otherSplit = splitHelper.buildSplit(otherFileContext.getOutputFile(),
                                                        context.getCurrentSplit(),
                                                        context.getConfig());
        if (Strategy.SPLIT.equals(otherSplit.getStrategy())) {
            throw new IllegalStateException("Unexpected strategy is provided. ");
        }

        return otherSplit;
    }
}

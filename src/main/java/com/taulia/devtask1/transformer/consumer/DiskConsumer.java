package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.transformer.context.FileContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.helper.GroupNameSelector;
import com.taulia.devtask1.transformer.context.helper.SplitHelper;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DiskConsumer<T> implements TransformerConsumer<T> {
    private final TransformerContext context;
    private final SplitHelper splitHelper;
    private GroupNameSelector<T, String> groupNameSelector;
    private Map<String, FileContext> groupToFileContext;
    private FileContext otherFileContext;

    public DiskConsumer(TransformerContext context) {
        this.context = context;
        this.splitHelper = new SplitHelper();
        this.groupNameSelector = context.getGroupNameSelector();
        this.groupToFileContext = new HashMap<>();
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
        final Split split = prepareSplit();
        return split != null ? new Split[] { split } : new Split[0];
    }

    private void closeStreams() throws IOException {
        final List<Exception> list = new LinkedList<>();

        for (Map.Entry<String, FileContext> entry : groupToFileContext.entrySet()) {
            try {
                final FileContext fileContext = entry.getValue();
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

    private Consumer<T> getRecordsConsumer() {
        return t -> routeInvoice(t);
    }

    private void routeInvoice(T t) {
        final String group = groupNameSelector.groupName(t);
        if (groupToFileContext.containsKey(group)) {
            appendToContext(groupToFileContext.get(group), t);
        }
        else {

              if (groupToFileContext.keySet().size() < context.getConfig().getMaxOpenHandlers()) {
                  final FileContext fileContext = context.nextGroupContext();
                  try {
                      fileContext.getOrCreateOutputWriter().init();
                  } catch (IOException ioe) {
                      throw new RuntimeException("Unable to create output writer: " + fileContext, ioe);
                  }

                  groupToFileContext.put(group, fileContext);
                  appendToContext(fileContext, t);
              }
              else {

                  if (otherFileContext == null) {
                      final FileContext otherFileContext = context.nextOtherContext();
                      try {
                          otherFileContext.getOrCreateOutputWriter().init();
                      } catch (IOException ioe) {
                          throw new RuntimeException("Unable to create output writer: " + otherFileContext, ioe);
                      }
                  }

                  appendToContext(otherFileContext, t);
              }
        }
    }

    private void appendToContext(FileContext fileContext, T t) {
        try {
            fileContext.getOrCreateOutputWriter().process(t);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to append data to file context: " + fileContext, ioe);
        }
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

package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.transformer.consumer.io.OutputInfo;
import com.taulia.devtask1.transformer.context.FileContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.helper.GroupNameSelector;
import com.taulia.devtask1.transformer.context.helper.SplitHelper;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import com.taulia.devtask1.transformer.strategy.Strategy;
import com.taulia.devtask1.transformer.strategy.StrategySelector;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DiskConsumer<T> implements TransformerConsumer<T> {
    private final TransformerContext<T> context;
    private final SplitHelper splitHelper;
    private final GroupNameSelector<T, String> groupNameSelector;
    private final Map<String, OutputInfo<T>> groupToOutputInfoMap;
    private OutputInfo<T> otherOutputInfo;

    public DiskConsumer(TransformerContext<T> context) {
        this.context = context;
        this.splitHelper = new SplitHelper(new StrategySelector());
        this.groupNameSelector = context.getGroupNameSelector();
        this.groupToOutputInfoMap = new HashMap<>();
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

        for (Map.Entry<String, OutputInfo<T>> entry : groupToOutputInfoMap.entrySet()) {
            try {
                final OutputInfo<T> outputInfo = entry.getValue();
                outputInfo.getOutputWriter().close();
            } catch (Exception exc) {
                log.error("Unable to close file. ", exc);
                list.add(exc);
            }
        }

        if (otherOutputInfo != null) {
            try {
                otherOutputInfo.getOutputWriter().close();
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
        if (groupToOutputInfoMap.containsKey(group)) {
            appendToContext(groupToOutputInfoMap.get(group), t);
        }
        else {

              if (groupToOutputInfoMap.keySet().size() < context.getConfig().getMaxOpenWriteHandlers()) {
                  final FileContext fileContext = context.nextGroupContext();

                  final File outputFile = context.getFileNameProducer().apply(fileContext);
                  TransformerOutputWriter<T> outputWriter = null;

                  try {
                      outputWriter = context.getIoContext().buildWriter(outputFile);
                      outputWriter.init();
                  }
                  catch (IOException ioe) {
                      throw new RuntimeException("Unable to create output writer: " + fileContext, ioe);
                  }

                  final OutputInfo<T> outputInfo = new OutputInfo<>(outputFile, outputWriter);
                  groupToOutputInfoMap.put(group, outputInfo);
                  appendToContext(outputInfo, t);
              }
              else {

                  if (otherOutputInfo == null) {
                      final FileContext otherFileContext = context.nextOtherContext();
                      final File otherOutputFile = context.getFileNameProducer().apply(otherFileContext);
                      TransformerOutputWriter<T> otherOutputWriter = null;

                      try {
                          otherOutputWriter = context.getIoContext().buildWriter(otherOutputFile);
                          otherOutputWriter.init();
                      } catch (IOException ioe) {
                          throw new RuntimeException("Unable to create output writer: " + otherFileContext, ioe);
                      }

                      otherOutputInfo = new OutputInfo<>(otherOutputFile, otherOutputWriter);
                  }

                  appendToContext(otherOutputInfo, t);
              }
        }
    }

    private void appendToContext(OutputInfo<T> outputInfo, T t) {
        try {
            outputInfo.getOutputWriter().process(t);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to append data to output: " + outputInfo, ioe);
        }
    }

    public Split prepareSplit() throws IOException {
        if (otherOutputInfo == null) {
            return null;
        }

        final Split otherSplit = splitHelper.buildSplit(otherOutputInfo.getOutputFile(),
                                                        context.getCurrentSplit(),
                                                        context.getConfig());

        if (Strategy.SPLIT.equals(otherSplit.getStrategy())) {
            throw new IllegalStateException("Unexpected strategy is provided. ");
        }

        return otherSplit;
    }
}

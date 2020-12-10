package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.transformer.context.FileContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.helper.GroupNameSelector;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InMemoryConsumer<T> implements TransformerConsumer<T> {
    private final TransformerContext<T> context;
    private final GroupNameSelector<T, String> groupNameSelector;
    private final Map<String, List<T>> groupNameToListMap;

    public InMemoryConsumer(TransformerContext<T> context) {
        this.context = context;
        this.groupNameSelector = context.getGroupNameSelector();
        this.groupNameToListMap = new HashMap<>();
    }

    @Override
    public Split[] process(TransformerInputReader<T> inputReader) throws Exception {
        Consumer<T> recordsConsumer = getRecordsConsumer();
        try {
            inputReader.process(recordsConsumer);
            doProcess();
        }
        finally {
            finish();
        }

        return new Split[0];
    }

    private Consumer<T> getRecordsConsumer() {
        return t -> {
            String group = groupNameSelector.groupName(t);
            List<T> list = groupNameToListMap.get(group);
            if (list == null) {
                list = new LinkedList<>();
                groupNameToListMap.put(group, list);
            }
            list.add(t);
        };
    }

    private void doProcess() throws Exception {
        for (Map.Entry<String, List<T>> entry : groupNameToListMap.entrySet()) {
            final FileContext fileContext = context.nextGroupContext();
            TransformerOutputWriter<T> outputWriter = null;
            try {
                final File outputFile = context.getFileNameProducer().apply(fileContext);
                outputWriter = context.getIoContext().buildWriter(outputFile);
                outputWriter.init();

                final List<T> recordList = entry.getValue();
                for (T record : recordList) {
                    outputWriter.process(record);
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
        groupNameToListMap.clear();
    }
}

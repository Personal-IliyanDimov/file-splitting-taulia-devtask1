package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.splitter.Split;

import java.util.function.Consumer;

public interface TransformerConsumer {
    public Consumer<InvoiceRecord> getRecordsConsumer();
    public Split[] process() throws Exception;
    public void finish() throws Exception;
}

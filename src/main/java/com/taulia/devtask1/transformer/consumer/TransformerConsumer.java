package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.io.data.InvoiceRecord;

import java.util.function.Consumer;

public interface TransformerConsumer {
    public Consumer<InvoiceRecord> getRecordsConsumer();
    public void process() throws Exception;
}

package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.data.InvoiceRecord;
import com.taulia.devtask1.transformer.splitter.Split;

public interface TransformerConsumer {
    public Split[] process(InputReader<InvoiceRecord> inputReader) throws Exception;
}

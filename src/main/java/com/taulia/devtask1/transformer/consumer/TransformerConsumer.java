package com.taulia.devtask1.transformer.consumer;

import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.io.TransformerInputReader;

public interface TransformerConsumer<T> {
    public Split[] process(TransformerInputReader<T> inputReader) throws Exception;
}

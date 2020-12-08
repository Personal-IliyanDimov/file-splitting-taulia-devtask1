package com.taulia.devtask1.transformer.io;

import java.util.function.Consumer;

public interface TransformerInputReader<T> {
    public void process(Consumer<T> recordConsumer) throws Exception;
}

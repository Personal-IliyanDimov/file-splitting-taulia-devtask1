package com.taulia.devtask1.transformer.io;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class TransformerInputReaderAdapter<T> implements TransformerInputReader<T> {

    private final InputReader<? extends Object> inputReader;
    private final Function<Object, T> transformFunction;

    public void process(Consumer<T> recordConsumer) throws Exception {
        inputReader.process(p -> {
            final T t = transformFunction.apply(p);
            recordConsumer.accept(t);
        });
    }
}

package com.taulia.devtask1.transformer.io.helper;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.function.Function;

@RequiredArgsConstructor
public class TransformerOutputWriterAdapter<T,P> implements TransformerOutputWriter<T> {

    private final OutputWriter<P> delegate;
    private final Function<T, P> converter;
    private final OutputWriter.ImageContext imageContext;

    @Override
    public void init() throws IOException {
        delegate.init();
    }

    @Override
    public void process(T input) throws IOException {
        final P p = converter.apply(input);
        delegate.process(p, imageContext);
    }

    @Override
    public void end() throws IOException {
        delegate.end();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}

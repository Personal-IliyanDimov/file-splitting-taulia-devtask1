package com.taulia.devtask1.transformer.io;

import java.io.IOException;

public interface TransformerOutputWriter<T> {
    public void init() throws IOException;
    public void process(T item) throws IOException;
    public void end() throws IOException;
    public void close() throws IOException;
}

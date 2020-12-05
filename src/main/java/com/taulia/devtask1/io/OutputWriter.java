package com.taulia.devtask1.io;

import java.io.IOException;

public interface OutputWriter<T> {
    public void init() throws IOException;
    public void process(T input) throws IOException;
    public void end() throws IOException;
    public void close() throws IOException;
}

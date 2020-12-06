package com.taulia.devtask1.io;

import java.io.File;
import java.io.IOException;

public interface OutputWriter<T> {
    public void init() throws IOException;
    public void process(T input, ImageContext context) throws IOException;
    public void end() throws IOException;
    public void close() throws IOException;

    public static interface ImageContext {
        File generateFileName();
    }
}

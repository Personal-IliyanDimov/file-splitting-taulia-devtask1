package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;

import java.io.File;
import java.io.IOException;

public interface IOContext<T> {
    public TransformerInputReader<T> buildReader(File inputFile) throws IOException;
    public TransformerOutputWriter<T> buildWriter(File outputFile) throws IOException;
}

package com.taulia.devtask1.transformer.io.helper;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.io.helper.IOHelper;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

@RequiredArgsConstructor
public class TransformerIOHelper {
    private IOHelper ioHelper;

    public <T> TransformerInputReader<T> buildReader(File inputFile, Function<Object, T> converter) throws IOException {
        final InputReader<? extends Object> inputReader = ioHelper.buildReader(inputFile);
        final TransformerInputReaderAdapter<T> adapter = new TransformerInputReaderAdapter<>(inputReader, converter);
        return adapter;
    }

    public <T,P> TransformerOutputWriter<T> buildWriter(File outputFile, OutputWriter.ImageContext imageContext, Function<T, P> converter) throws IOException {
        final OutputWriter<?> outputWriter = ioHelper.buildWriter(outputFile);
        final TransformerOutputWriterAdapter adapter = new TransformerOutputWriterAdapter<T,P>(outputWriter, converter, imageContext);
        return adapter;
    }
}

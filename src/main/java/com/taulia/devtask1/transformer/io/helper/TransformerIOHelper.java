package com.taulia.devtask1.transformer.io.helper;

import com.taulia.devtask1.io.InputReader;
import com.taulia.devtask1.io.helper.IOHelper;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

@RequiredArgsConstructor
public class TransformerIOHelper {

    private final IOHelper ioHelper;

    public <T> TransformerInputReader<T> buildReader(File inputFile, Function<Object, T> wrapperFunction) throws IOException {
        final InputReader<? extends Object> inputReader = ioHelper.buildReader(inputFile);
        final TransformerInputReaderAdapter<T> adapter = new TransformerInputReaderAdapter<>(inputReader, wrapperFunction);
        return adapter;
    }

//    public <T,P> TransformerOutputWriter<T> buildWriter(File outputFile, OutputWriter.ImageContext imageContext, Function<T, Object> converter) throws IOException {
//        final OutputWriter<? extends Object> outputWriter = ioHelper.buildWriter(outputFile);
//        final TransformerOutputWriterAdapter adapter = new TransformerOutputWriterAdapter<T,? extends Object>(outputWriter, converter, imageContext);
//        return adapter;
//    }
}

package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.TransformerOutputWriter;
import com.taulia.devtask1.transformer.io.helper.TransformerIOHelper;

import java.io.File;
import java.io.IOException;

public class IOContextImpl<T> implements IOContext<T> {

    private final TransformerContext<T> context;
    private final TransformerIOHelper helper;

    public IOContextImpl(TransformerContext<T> context) {
        this.context = context;
        this.helper = new TransformerIOHelper();
    }

    @Override
    public TransformerInputReader<T> buildReader(File inputFile) throws IOException {
        return helper.buildReader(context.getCurrentSplit().getInputFile(), context.getWrapperFunction());
    }

    @Override
    public TransformerOutputWriter<T> buildWriter(File outputFile) throws IOException {
        return helper.<T, Object>buildWriter(outputFile, context.prepareImageContext(), context.getUnwrapperFunction());
    }
}

package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.io.TransformerInputReader;

import java.io.IOException;

public abstract class AbstractTransformer<T> implements Transformer<T> {

    @Override
    public Split[] transform(TransformerContext<T> context) throws Exception {
        final TransformerInputReader<T> transformerInputReader = findTransformerInputReader(context);
        final TransformerConsumer<T> transformerConsumer = getConsumer(context);
        return transformerConsumer.process(transformerInputReader);
    }

    protected TransformerInputReader<T> findTransformerInputReader(TransformerContext<T> context) throws IOException {
        return context.getIoContext().buildReader(context.getCurrentSplit().getInputFile());
    }

    protected abstract TransformerConsumer<T> getConsumer(TransformerContext<T> context);
}

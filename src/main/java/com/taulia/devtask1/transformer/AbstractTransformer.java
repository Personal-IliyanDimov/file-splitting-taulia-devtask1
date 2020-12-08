package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import com.taulia.devtask1.transformer.io.helper.TransformerIOHelper;

import java.io.IOException;
import java.util.function.Function;

public abstract class AbstractTransformer<T> implements Transformer {

    @Override
    public Split[] transform(TransformerContext context) throws Exception {
        final TransformerInputReader<T> transformerInputReader = findTransformerInputReader(context);
        final TransformerConsumer<T> transformerConsumer = getConsumer(context);
        return transformerConsumer.process(transformerInputReader);
    }

    protected TransformerInputReader<T> findTransformerInputReader(TransformerContext context) throws IOException {
        final TransformerIOHelper helper = new TransformerIOHelper();
        final TransformerInputReader<T> tiReader = helper.buildReader(context.getCurrentSplit().getInputFile(), getTransformFunction());
        return tiReader;
    }

    protected abstract TransformerConsumer<T> getConsumer(TransformerContext context);

    protected abstract Function<Object, T> getTransformFunction();

}

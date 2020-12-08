package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.io.TransformerInputReader;

public abstract class AbstractTransformer<T> implements Transformer {

    @Override
    public Split[] transform(TransformerContext context) throws Exception {
        final TransformerInputReader<T> transformerInputReader = context.findTransformerInputReader();
        final TransformerConsumer<T> transformerConsumer = getConsumer(context);
        return transformerConsumer.process(transformerInputReader);
    }

    protected abstract TransformerConsumer<T> getConsumer(TransformerContext context);

}

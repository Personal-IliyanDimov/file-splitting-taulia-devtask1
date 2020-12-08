package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.InMemoryConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class InMemoryTransformer<T> extends AbstractTransformer<T> {

    private final Function<Object, T> transformFunction;

    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new InMemoryConsumer(context);
    }

    @Override
    protected Function<Object, T> getTransformFunction() {
        return transformFunction;
    }
}

package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.SplittingConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class SplittingTransformer<T> extends AbstractTransformer<T> {

    private final Function<Object, T> transformFunction;

    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new SplittingConsumer(context);
    }

    @Override
    protected Function<Object, T> getTransformFunction() {
        return transformFunction;
    }
}

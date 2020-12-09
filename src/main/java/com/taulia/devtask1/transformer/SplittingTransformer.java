package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.SplittingConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SplittingTransformer<T> extends AbstractTransformer<T> {

    @Override
    protected TransformerConsumer getConsumer(TransformerContext<T> context) {
        return new SplittingConsumer(context);
    }
}

package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.InMemoryConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryTransformer<T> extends AbstractTransformer<T> {

    @Override
    protected TransformerConsumer getConsumer(TransformerContext<T> context) {
        return new InMemoryConsumer(context);
    }

}

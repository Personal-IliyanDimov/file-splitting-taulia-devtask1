package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.InMemoryConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;

public class InMemoryTransformer extends AbstractTransformer {

    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new InMemoryConsumer(context);
    }
}

package com.taulia.devtask1.transformer.memory;

import com.taulia.devtask1.transformer.AbstractTransformer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;

public class InMemoryTransformer extends AbstractTransformer {

    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new InMemoryConsumer(context);
    }
}

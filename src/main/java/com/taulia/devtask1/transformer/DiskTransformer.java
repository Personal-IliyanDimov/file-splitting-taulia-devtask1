package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.DiskConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;

public class DiskTransformer extends AbstractTransformer {

    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new DiskConsumer(context);
    }
}

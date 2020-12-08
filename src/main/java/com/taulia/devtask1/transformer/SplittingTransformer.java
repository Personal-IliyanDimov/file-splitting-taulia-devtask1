package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.SplittingConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;

public class SplittingTransformer extends AbstractTransformer {
    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new SplittingConsumer(context);
    }
}

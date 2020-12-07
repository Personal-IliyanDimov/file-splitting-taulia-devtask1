package com.taulia.devtask1.transformer.splitter;

import com.taulia.devtask1.transformer.AbstractTransformer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.helper.TransformerContext;

public class SplittingTransformer extends AbstractTransformer {
    @Override
    protected TransformerConsumer getConsumer(TransformerContext context) {
        return new SplittingConsumer(context);
    }
}

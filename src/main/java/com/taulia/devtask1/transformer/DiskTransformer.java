package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.consumer.DiskConsumer;
import com.taulia.devtask1.transformer.consumer.TransformerConsumer;
import com.taulia.devtask1.transformer.context.TransformerContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiskTransformer<T> extends AbstractTransformer<T> {

    @Override
    protected TransformerConsumer<T> getConsumer(TransformerContext context) {
        return new DiskConsumer(context);
    }
}

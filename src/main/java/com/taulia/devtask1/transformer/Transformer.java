package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.Split;

public interface Transformer<T> {
    Split[] transform(TransformerContext<T> context) throws Exception;
}

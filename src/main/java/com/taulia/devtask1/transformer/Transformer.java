package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.Split;

public interface Transformer {
    Split[] transform(TransformerContext context) throws Exception;
}

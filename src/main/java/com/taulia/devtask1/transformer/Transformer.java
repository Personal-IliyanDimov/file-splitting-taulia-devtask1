package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.helper.TransformerContext;

public interface Transformer {
    TransformerContext transform(TransformerContext context) throws Exception;
}

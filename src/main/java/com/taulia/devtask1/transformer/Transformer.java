package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.splitter.Split;

public interface Transformer {
    Split[] transform(TransformerContext context) throws Exception;
}

package com.taulia.devtask1.transformer;

import java.util.ArrayList;
import java.util.List;

public class DiskTransformer {

    public TransformerContext transform(TransformerContext context) {
        return doTransform(List.of(context));
    }



    private TransformerContext transformOnDisk(TransformerContext transformerContext) {

    }
}

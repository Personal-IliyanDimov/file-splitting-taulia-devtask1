package com.taulia.devtask1.transformer.context;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransformerConfig {
    private long maxInMemoryFileSizeInBytes;
    private long maxOpenHandlers;
    private TraversePolicy traverPolicy;

    public static interface TraversePolicy {
        public void addSplits(TransformerContext context, List<Split> splits);
        public Split nextSplit(TransformerContext context);
    }
}

package com.taulia.devtask1.transformer.strategy;

import com.taulia.devtask1.transformer.helper.TransformerConfig;
import com.taulia.devtask1.transformer.splitter.Split;

import java.io.File;

public class StrategySelector {

    public Strategy select(File inputFile, TransformerConfig transformerConfig) {
        Strategy strategy = null;
        if (inputFile.length() <  transformerConfig.getMaxInMemoryFileSizeInBytes()) {
            strategy = Strategy.IN_MEMORY;
        } else {
            strategy = Strategy.SPLIT;
        }

        return strategy;
    }

    public Strategy select(File inputFile, Split parentSplit, TransformerConfig transformerConfig) {
        Strategy strategy = null;
        if (inputFile.length() <  transformerConfig.getMaxInMemoryFileSizeInBytes()) {
            strategy = Strategy.IN_MEMORY;
        } else {
            if (Strategy.SPLIT.equals(parentSplit.getStrategy()) && parentSplit.getSplitDetails().getSplitFactor() > 2) {
                strategy = Strategy.SPLIT;
            }  else {
                strategy = Strategy.ON_DISK;
            }
        }

        return strategy;
    }
}

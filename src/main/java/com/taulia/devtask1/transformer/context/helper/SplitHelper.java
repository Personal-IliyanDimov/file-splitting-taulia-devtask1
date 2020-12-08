package com.taulia.devtask1.transformer.context.helper;

import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerConfig;
import com.taulia.devtask1.transformer.strategy.Strategy;
import com.taulia.devtask1.transformer.strategy.StrategySelector;

import java.io.File;
import java.util.function.Function;

public class SplitHelper {

    private StrategySelector strategySelector;

    public Split buildRootSplit(final File inputFile, final TransformerConfig config) {
        Split.SplitDetails splitDetails = null;

        final Strategy strategy = strategySelector.select(inputFile, config);
        if (Strategy.SPLIT.equals(strategy)) {
            splitDetails = new Split.SplitDetails();
            splitDetails.setSplitFactor(calculateSplitFactor(inputFile, config));
            splitDetails.setSplitFunctionFactory(buildSplitFunctionFactory());

            if (splitDetails.getSplitFactor() < 2) {
                throw new IllegalStateException("Split factor must be always gt 2");
            }
        }

        final Split split = new Split();
        split.setInputFile(inputFile);
        split.setDeleteInput(false);
        split.setStrategy(strategy);
        split.setSplitDetails(splitDetails);

        return split;
    }

    public Split buildSplit(File inputFile, Split parentSplit, TransformerConfig config) {
        Split.SplitDetails splitDetails = null;

        final Strategy strategy = strategySelector.select(inputFile, parentSplit, config);
        if (Strategy.SPLIT.equals(strategy)) {
            splitDetails = new Split.SplitDetails();
            splitDetails.setSplitFactor(parentSplit.getSplitDetails().getSplitFactor() - 1);
            splitDetails.setSplitFunctionFactory(parentSplit.getSplitDetails().getSplitFunctionFactory());

            if (splitDetails.getSplitFactor() < 2) {
                throw new IllegalStateException("Split factor must be always gt 2");
            }
        }

        final Split split = new Split();
        split.setInputFile(inputFile);
        split.setDeleteInput(true);
        split.setStrategy(strategy);
        split.setSplitDetails(splitDetails);

        return split;
    }

    private Long calculateSplitFactor(File inputFile, TransformerConfig config) {
        final long fileLength = inputFile.length();
        final long maxInMemoryFileSizeInBytes = config.getMaxInMemoryFileSizeInBytes();
        final long targetedSplitFactor = Math.floorDiv(fileLength, maxInMemoryFileSizeInBytes) + 1;
        final long realSplitFactor = Math.min(config.getMaxOpenHandlers() - 1, targetedSplitFactor);
        return realSplitFactor;
    }

    private Function<Long, Function<String, Long>> buildSplitFunctionFactory() {
        return l -> (s -> s.hashCode() % l);
    }
}

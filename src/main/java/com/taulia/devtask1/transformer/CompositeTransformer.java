package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.splitter.Split;
import com.taulia.devtask1.transformer.strategy.Strategy;
import com.taulia.devtask1.transformer.strategy.StrategySelector;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class CompositeTransformer implements Transformer {

    private final StrategySelector strategySelector;
    private final Transformer splittingTransformer;
    private final Transformer diskTransformer;
    private final Transformer inMemoryTransformer;

    @Override
    public Split[] transform(TransformerContext initialContext) throws Exception {
        doTransform(initialContext);
        return new Split[0];
    }

    private void doTransform(TransformerContext context) throws Exception {
        while (context.getCurrentSplit() != null) {
            final Strategy strategy = context.getCurrentSplit().getStrategy();

            Split[] splitArray = null;
            switch (strategy) {
                case SPLIT: {
                    splitArray = splittingTransformer.transform(context);
                    break;
                }
                case IN_MEMORY: {
                    splitArray = inMemoryTransformer.transform(context);
                    break;
                }
                case ON_DISK: {
                    splitArray = diskTransformer.transform(context);
                    break;
                }
                default: {
                    break;
                }
            }

            advanceContext(context, Arrays.asList(splitArray));
        }
    }

    private void advanceContext(TransformerContext context, List<Split> additionalSplits) throws IOException {
        context.addSplits(additionalSplits);
        context.rotateCurrentSplit();
    }
}

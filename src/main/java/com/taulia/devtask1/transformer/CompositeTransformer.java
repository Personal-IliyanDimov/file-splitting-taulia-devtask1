package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.strategy.Strategy;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class CompositeTransformer<T> implements Transformer<T> {

    private final Transformer<T> splittingTransformer;
    private final Transformer<T> diskTransformer;
    private final Transformer<T> inMemoryTransformer;

    @Override
    public Split[] transform(TransformerContext<T> initialContext) throws Exception {
        doTransform(initialContext);
        return new Split[0];
    }

    private void doTransform(TransformerContext<T> context) throws Exception {
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

    private void advanceContext(TransformerContext<T> context, List<Split> additionalSplits) throws IOException {
        context.addSplits(additionalSplits);
        context.rotateCurrentSplit();
    }
}

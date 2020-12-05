package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.strategy.Strategy;
import com.taulia.devtask1.transformer.strategy.StrategySelector;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CompositeTransformer implements Transformer {

    private final StrategySelector strategySelector;
    private final Transformer diskTransformer;
    private final Transformer inMemoryTransformer;

    @Override
    public TransformerContext transform(TransformerContext initialContext) throws Exception {
        doTransform(initialContext);
        return null;
    }

    private void doTransform(TransformerContext initialContext) throws Exception {
        final List<TransformerContext> pendingList = new ArrayList<>(List.of(initialContext));
        while (! pendingList.isEmpty()) {
            TransformerContext currentContext = pendingList.remove(0);
            final Strategy strategy = strategySelector.transform(currentContext);

            TransformerContext nextContext = null;
            switch (strategy) {
                case IN_MEMORY: {
                    nextContext = inMemoryTransformer.transform(currentContext);
                    break;
                }
                case ON_DISK: {
                    nextContext = diskTransformer.transform(currentContext);
                    break;
                }
                default: {
                    break;
                }
            }

            if (Objects.nonNull(nextContext)) {
                pendingList.add(nextContext);
            }
        }
    }
}

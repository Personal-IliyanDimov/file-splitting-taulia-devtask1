package com.taulia.devtask1.transformer;

import com.taulia.devtask1.transformer.strategy.Strategy;
import com.taulia.devtask1.transformer.strategy.StrategySelector;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class TransformerImpl {

    private final StrategySelector strategySelector;
    private final DiskTransformer diskTransformer;
    private final InMemoryTransformer inMemoryTransformer;

    public void transform(TransformerContext initialContext) {
        doTransform(initialContext);
    }

    private void doTransform(TransformerContext initialContext) {
        final List<TransformerContext> pendingList = new ArrayList<>(List.of(initialContext));
        while (! pendingList.isEmpty()) {
            TransformerContext currentContext = pendingList.remove(0);
            final Strategy strategy = strategySelector.transform(currentContext);

            TransformerContext nextContext = null;
            switch (strategy) {
                case IN_MEMORY: {
                    inMemoryTransformer.transform(currentContext);
                    nextContext = null;
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

    private TransformerContext transformInMemory(TransformerContext context) {
        inMemoryTransformer.transform(context);
        return null;
    }

    private TransformerContext transformOnDisk(TransformerContext context) {
        return diskTransformer.transform(context);
    }
}

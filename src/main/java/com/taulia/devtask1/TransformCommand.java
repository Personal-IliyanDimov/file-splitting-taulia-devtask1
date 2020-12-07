package com.taulia.devtask1;


import com.taulia.devtask1.transformer.CompositeTransformer;
import com.taulia.devtask1.transformer.Transformer;
import com.taulia.devtask1.transformer.disk.DiskTransformer;
import com.taulia.devtask1.transformer.helper.TransformerConfig;
import com.taulia.devtask1.transformer.helper.TransformerContext;
import com.taulia.devtask1.transformer.memory.InMemoryTransformer;
import com.taulia.devtask1.transformer.splitter.Split;
import com.taulia.devtask1.transformer.splitter.SplitHelper;
import com.taulia.devtask1.transformer.splitter.SplittingTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TransformCommand {

    private SplitHelper helper;
    private CompositeTransformer compositeTransformer;

    public TransformCommand() {
        helper = new SplitHelper();

        final Transformer splittingTransformer = new SplittingTransformer();
        final Transformer diskTransformer = new DiskTransformer();
        final Transformer inMemoryTransformer = new InMemoryTransformer();
        compositeTransformer = new CompositeTransformer(splittingTransformer, diskTransformer, inMemoryTransformer);
    }

    public void executeCommand(File inputFile, File outputFolder, TransformerContext.OutputType outputType) {
        final TransformerConfig config = prepareTransformerConfig();
        final TransformerContext context = getTransformerContext(inputFile, outputFolder, outputType, config);

        try {
            compositeTransformer.transform(context);
        }
        catch (Exception exc) {
            cleanUp(context);
        }
    }

    private void cleanUp(TransformerContext context) {
        // clean up the whole output folder
    }

    private TransformerConfig prepareTransformerConfig() {
        TransformerConfig config = new TransformerConfig();
        config.setMaxOpenHandlers(2);
        config.setMaxInMemoryFileSizeInBytes(512*1024);
        config.setTraverPolicy(new DeepFirstTraversePolicy());
        return config;
    }

    private TransformerContext getTransformerContext(File inputFile, File outputFolder, TransformerContext.OutputType outputType, TransformerConfig config) {
        final TransformerContext context = new TransformerContext();
        context.setCurrentSplit(prepareInitialSplit(inputFile, config));
        context.setSplitList(new ArrayList<>());
        context.setOutputFolder(outputFolder);
        context.setOutputType(outputType);
        context.setOutputBuyerPrefix("buyer");
        context.setOutputBuyerIndex(0L);
        context.setOutputOtherPrefix("other");
        context.setOutputOtherIndex(0L);
        context.setImagePrefix("image");
        context.setImageIndex(0L);
        context.setConfig(config);
        return context;
    }

    private Split prepareInitialSplit(File inputFile, TransformerConfig config) {
        return helper.buildRootSplit(inputFile, config);
    }

    private static class DeepFirstTraversePolicy implements TransformerConfig.TraversePolicy {

        @Override
        public void addSplits(TransformerContext context, List<Split> splits) {
            context.getSplitList().addAll(0, splits);
        }

        @Override
        public Split nextSplit(TransformerContext context) {
            Split result = null;

            if (context.getSplitList().size() > 0) {
                result = context.getSplitList().remove(0);
            }

            return result;
        }
    }
}
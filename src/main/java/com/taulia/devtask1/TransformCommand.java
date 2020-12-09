package com.taulia.devtask1;


import com.taulia.devtask1.transformer.CompositeTransformer;
import com.taulia.devtask1.transformer.DiskTransformer;
import com.taulia.devtask1.transformer.InMemoryTransformer;
import com.taulia.devtask1.transformer.SplittingTransformer;
import com.taulia.devtask1.transformer.Transformer;
import com.taulia.devtask1.transformer.context.NamingContext;
import com.taulia.devtask1.transformer.context.Split;
import com.taulia.devtask1.transformer.context.TransformerConfig;
import com.taulia.devtask1.transformer.context.TransformerContext;
import com.taulia.devtask1.transformer.context.helper.SplitHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TransformCommand<T> {

    private SplitHelper helper;
    private CompositeTransformer<T> compositeTransformer;

    public TransformCommand() {
        helper = new SplitHelper();

        final Transformer<T> splittingTransformer = new SplittingTransformer<T>();
        final Transformer<T> diskTransformer = new DiskTransformer<T>();
        final Transformer<T> inMemoryTransformer = new InMemoryTransformer<T>();
        compositeTransformer = new CompositeTransformer<T>(splittingTransformer, diskTransformer, inMemoryTransformer);
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

    private TransformerContext getTransformerContext(File inputFile,
                                                     File outputFolder,
                                                     TransformerContext.OutputType outputType,
                                                     TransformerConfig config) {

        final NamingContext namingContext = new NamingContext();
        namingContext.setOutputGroupPrefix("buyer");
        namingContext.setOutputGroupIndex(0L);
        namingContext.setOutputOtherPrefix("other");
        namingContext.setOutputOtherIndex(0L);
        namingContext.setImagePrefix("image");
        namingContext.setImageIndex(0L);

        final TransformerContext context = new TransformerContext();
        context.setCurrentSplit(prepareInitialSplit(inputFile, config));
        context.setSplitList(new ArrayList<>());
        context.setOutputFolder(outputFolder);
        context.setOutputType(outputType);
        context.setNamingContext(namingContext);
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
                result = (Split) context.getSplitList().remove(0);
            }

            return result;
        }
    }
}
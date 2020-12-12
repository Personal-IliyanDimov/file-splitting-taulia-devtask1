package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.transformer.context.helper.GroupNameSelector;
import com.taulia.devtask1.transformer.context.helper.SplitSourceSelector;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class TransformerContext<T> {
    private Split currentSplit;
    private ArrayList<Split> splitList;
    private File outputFolder;
    private OutputType outputType;
    private IOContext<T> ioContext;
    private NamingContext namingContext;
    private GenericContext<T> genericContext;
    private TransformerConfig config;


    public FileContext nextSplitContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(namingContext.getOutputSplitPrefix());
        fileContext.setOutputIndex(namingContext.getAndIncrementSplitIndex());
        return fileContext;
    }

    public FileContext nextGroupContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(namingContext.getOutputGroupPrefix());
        fileContext.setOutputIndex(namingContext.getAndIncrementGroupIndex());
        return fileContext;
    }

    public FileContext nextOtherContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(namingContext.getOutputOtherPrefix());
        fileContext.setOutputIndex(namingContext.getAndIncrementOtherIndex());
        return fileContext;
    }

    public File nextImageFile() {
        final long currentImageIndex = namingContext.getAndIncrementImageIndex();
        return new File(outputFolder, namingContext.getImagePrefix() + "-" + currentImageIndex + ".img");

    }

    public void addSplits(List<Split> splits) {
        config.getTraversePolicy().addSplits(this, splits);
    }

    public void rotateCurrentSplit() throws IOException {
        if (this.currentSplit.isDeleteInput()) {
            final boolean success = currentSplit.getInputFile().delete();
            if (! success) {
                throw new RuntimeException("Unable to delete current split file: " +  currentSplit.getInputFile().toString());
            }
        }
        this.splitList.remove(currentSplit);

        final Split nextSplit = config.getTraversePolicy().nextSplit(this);
        this.currentSplit = nextSplit;
    }

    public OutputWriter.ImageContext prepareImageContext() {
        return new OutputWriter.ImageContext() {
            @Override
            public File generateFileName() {
                return nextImageFile();
            }
        };
    }

    public Function<Object, T> getWrapperFunction() {
        return genericContext.getWrapperFunction();
    }

    public Function<T, Object> getUnwrapperFunction() {
        return genericContext.getUnwrapperFunction();
    }

    public GroupNameSelector<T, String> getGroupNameSelector() {
        return genericContext.getGroupNameSelector();
    }

    public SplitSourceSelector<T, String> getSplitSourceSelector() {
        return genericContext.getSplitSourceSelector();
    }

    public Function<FileContext, File> getFileNameProducer() {
        return genericContext.getFileNameProducer();
    }

    public static enum OutputType {
        CSV,
        XML,
        INTXML;
    }
}

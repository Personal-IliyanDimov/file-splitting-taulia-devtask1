package com.taulia.devtask1.transformer.context;

import com.taulia.devtask1.io.OutputWriter;
import com.taulia.devtask1.transformer.context.helper.GroupNameSelector;
import com.taulia.devtask1.transformer.io.TransformerInputReader;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TransformerContext {
    private Split currentSplit;
    private ArrayList<Split> splitList;
    private File outputFolder;
    private OutputType outputType;
    private String outputSplitPrefix;
    private long outputSplitIndex;
    private String outputGroupPrefix;
    private long outputGroupIndex;
    private String outputOtherPrefix;
    private long outputOtherIndex;
    private String imagePrefix;
    private long imageIndex;

    private TransformerConfig config;

    public FileContext nextSplitContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputSplitPrefix);
        fileContext.setOutputIndex(outputSplitIndex ++);
        return fileContext;
    }

    public FileContext nextGroupContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputGroupPrefix);
        fileContext.setOutputIndex(outputGroupIndex ++);
        return fileContext;
    }

    public FileContext nextOtherContext() {
        final FileContext fileContext = new FileContext();
        fileContext.setOutputFolder(outputFolder);
        fileContext.setOutputType(outputType);
        fileContext.setOutputPrefix(outputOtherPrefix);
        fileContext.setOutputIndex(outputOtherIndex ++);
        return fileContext;
    }

    public File nextImageFile() {
        final long currentImageIndex = imageIndex ++;
        return new File(outputFolder, imagePrefix + "-" + currentImageIndex + ".img");

    }

    public void addSplits(List<Split> splits) {
        config.getTraverPolicy().addSplits(this, splits);
    }

    public void rotateCurrentSplit() throws IOException {
        if (this.currentSplit.isDeleteInput()) {
            final boolean success = currentSplit.getInputFile().delete();
            if (! success) {
                throw new RuntimeException("Unable to delete current split file: " +  currentSplit.getInputFile().toString());
            }
        }
        this.splitList.remove(currentSplit);

        final Split nextSplit = config.getTraverPolicy().nextSplit(this);
        this.currentSplit = nextSplit;
    }

    public static enum OutputType {
        CSV,
        XML
    }

    public OutputWriter.ImageContext prepareImageContext() {
        return new OutputWriter.ImageContext() {
            @Override
            public File generateFileName() {
                return nextImageFile();
            }
        };
    }


    public <T> GroupNameSelector<T, String> getGroupNameSelector() {
        return null;
    }

    public <T> TransformerInputReader<T> findTransformerInputReader() {
        return null;
    }
}

package com.taulia.devtask1.transformer.strategy;

import com.taulia.devtask1.transformer.helper.TransformerConfig;
import com.taulia.devtask1.transformer.helper.TransformerContext;

import java.io.File;

public class StrategySelector {
    public Strategy transform(TransformerContext context) {
        final TransformerConfig config = context.getConfig();
        final File inputFile = context.getInputFile();
        final File outputFolder = context.getOutputFolder();

        if (! inputFile.isFile()) {
            throw new IllegalArgumentException("Input file must be a file: " + inputFile.toPath().toString());
        }
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Input file does not exist: " + inputFile.toPath().toString());
        }

        if (! outputFolder.isDirectory()) {
            throw new IllegalArgumentException("Output file must be a folder: " + outputFolder.toPath().toString());
        }

        if (! outputFolder.exists()) {
            throw new IllegalArgumentException("Output folder does not exist: " + outputFolder.toPath().toString());
        }

        Strategy strategy = null;
        if (inputFile.length() <  config.getMaxInMemoryFileSizeInBytes()) {
            strategy = Strategy.IN_MEMORY;
        } else {
            strategy = Strategy.ON_DISK;
        }

        return strategy;
    }
}

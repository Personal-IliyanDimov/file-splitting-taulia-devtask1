package com.taulia.devtask1.transformer;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class TransformerContext {
    private File inputFile;
    private File outputFolder;
    private TransformerConfig config;
}

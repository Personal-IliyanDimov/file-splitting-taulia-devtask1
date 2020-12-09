package com.taulia.devtask1.transformer.context;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@Getter
@Setter
@ToString
public class FileContext {
    private File outputFolder;
    private String outputPrefix;
    private long outputIndex;
    private TransformerContext.OutputType outputType;
}

package com.taulia.devtask1.transformer.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransformerConfig {
    private long maxInMemoryFileSizeInBytes;
    private long maxOpenHandlers;
}

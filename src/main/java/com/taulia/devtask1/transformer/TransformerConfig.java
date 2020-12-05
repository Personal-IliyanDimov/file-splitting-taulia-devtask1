package com.taulia.devtask1.transformer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransformerConfig {
    private long maxInMemoryFileSizeInBytes;
    private long maxOpenHandlers;
}

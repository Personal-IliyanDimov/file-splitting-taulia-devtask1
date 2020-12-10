package com.taulia.devtask1.transformer.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NamingContext {
    private final String outputSplitPrefix;
    private long outputSplitIndex;
    private final String outputGroupPrefix;
    private long outputGroupIndex;
    private final String outputOtherPrefix;
    private long outputOtherIndex;
    private final String imagePrefix;
    private long imageIndex;

    public long getAndIncrementSplitIndex() {
        return outputSplitIndex++;
    }

    public long getAndIncrementGroupIndex() {
        return outputGroupIndex++;
    }

    public long getAndIncrementOtherIndex() {
        return outputOtherIndex++;
    }

    public long getAndIncrementImageIndex() {
        return imageIndex++;
    }
}

package com.taulia.devtask1.transformer.context;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NamingContext {
    private String outputSplitPrefix;
    private long outputSplitIndex;
    private String outputGroupPrefix;
    private long outputGroupIndex;
    private String outputOtherPrefix;
    private long outputOtherIndex;
    private String imagePrefix;
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

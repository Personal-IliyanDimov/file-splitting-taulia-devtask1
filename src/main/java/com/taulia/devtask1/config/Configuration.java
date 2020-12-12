package com.taulia.devtask1.config;

public class Configuration {

    public static final  int MAX_ALLOWED_AMOUNT_OF_HANDLES = 512;
    public static final long MAX_MEMORY_IN_BYTES           = Runtime.getRuntime().maxMemory();

    public static final long SAMPLE_MAX_OPEN_WRITE_HANDLERS    = 128;
    public static final long SAMPLE_MAX_IN_MEMORY_FS_IN_BYTES  = Math.floorDiv(MAX_MEMORY_IN_BYTES, 3);

    public static final String SAMPLE_SPLIT_PREFIX = "split";
    public static final String SAMPLE_GROUP_PREFIX = "group";
    public static final String SAMPLE_OTHER_PREFIX = "other";
    public static final String SAMPLE_IMAGE_PREFIX = "image";


}

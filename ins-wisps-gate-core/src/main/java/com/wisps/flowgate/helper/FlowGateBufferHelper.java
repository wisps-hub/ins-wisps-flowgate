package com.wisps.flowgate.helper;

public class FlowGateBufferHelper {

    public static final String FLUSHER = "FLUSHER";
    public static final String MPMC = "MPMC";

    public static boolean isMpmc(String bufferType){
        return MPMC.equals(bufferType);
    }

    public static boolean isFlusher(String bufferType){
        return FLUSHER.equals(bufferType);
    }
}
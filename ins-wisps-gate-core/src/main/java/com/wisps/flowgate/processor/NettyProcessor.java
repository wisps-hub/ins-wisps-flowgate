package com.wisps.flowgate.processor;

import com.wisps.flowgate.context.HttpRequestWrapper;

public interface NettyProcessor {

    void process(HttpRequestWrapper httpRequestWrapper);

    void start();

    void shutdown();
}

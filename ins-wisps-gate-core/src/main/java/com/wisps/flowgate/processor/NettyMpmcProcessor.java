package com.wisps.flowgate.processor;

import com.wisps.flowgate.context.HttpRequestWrapper;
import com.wisps.flowgate.core.FlowGateConfig;

/**
 * mpmc 缓冲队列核心实现
 */
public class NettyMpmcProcessor implements NettyProcessor{

    private FlowGateConfig config;
    private NettyCoreProcessor coreProcessor;

    public NettyMpmcProcessor(FlowGateConfig config, NettyCoreProcessor coreProcessor) {
        this.config = config;
        this.coreProcessor = coreProcessor;
    }

    @Override
    public void process(HttpRequestWrapper event) {
         
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
package com.wisps.flowgate.processor;

import com.wisps.flowgate.context.HttpRequestWrapper;
import com.wisps.flowgate.core.FlowGateConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * flusher 缓冲队列核心实现
 */
public class NettyBatchEventProcessor implements NettyProcessor{

    private FlowGateConfig config;
    private NettyCoreProcessor coreProcessor;

    public NettyBatchEventProcessor(FlowGateConfig config, NettyCoreProcessor coreProcessor) {
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
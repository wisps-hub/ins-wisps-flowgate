package com.wisps.flowgate.core;

import com.wisps.flowgate.netty.NettyHttpServer;
import com.wisps.flowgate.processor.NettyBatchEventProcessor;
import com.wisps.flowgate.processor.NettyCoreProcessor;
import com.wisps.flowgate.processor.NettyMpmcProcessor;
import com.wisps.flowgate.processor.NettyProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowGateContainer implements LifeCycle{

    private final FlowGateConfig config;
    private NettyHttpServer nettyHttpServer;
    private NettyProcessor processor;

    public FlowGateContainer(FlowGateConfig config) {
        this.config = config;
        init();
    }

    @Override
    public void init() {
        // 构建核心处理器
        NettyCoreProcessor coreProcessor = new NettyCoreProcessor();
        //是否开启缓存
        String bufferType = config.getBufferType();
        if (FlowGateBufferHelper.isFlusher(bufferType)){
            processor = new NettyBatchEventProcessor(config, coreProcessor);
        }else if (FlowGateBufferHelper.isMpmc(bufferType)){
            processor = new NettyMpmcProcessor(config, coreProcessor);
        }else {
            this.processor = coreProcessor;
        }
        // 创建httpserver
        nettyHttpServer = new NettyHttpServer(config, processor);
    }

    @Override
    public void start() {
        processor.start();
        nettyHttpServer.start();
        log.info("FlowGateContainer start !");
    }

    @Override
    public void shutdown() {
        processor.shutdown();
        nettyHttpServer.shutdown();
        log.info("FlowGateContainer shutdown !");
    }
}
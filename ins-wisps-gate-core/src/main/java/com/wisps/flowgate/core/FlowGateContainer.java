package com.wisps.flowgate.core;

import com.wisps.flowgate.helper.FlowGateBufferHelper;
import com.wisps.flowgate.netty.NettyHttpClient;
import com.wisps.flowgate.netty.NettyHttpServer;
import com.wisps.flowgate.processor.NettyBatchEventProcessor;
import com.wisps.flowgate.processor.NettyCoreProcessor;
import com.wisps.flowgate.processor.NettyMpmcProcessor;
import com.wisps.flowgate.processor.NettyProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowGateContainer implements LifeCycle{

    /** 核心配置类 */
    private final FlowGateConfig config;
    /** 接收http请求的sever */
    private NettyHttpServer nettyHttpServer;
    /** http转发核心处理类 */
    private NettyHttpClient nettyHttpClient;
    /** 核心处理器 */
    private NettyProcessor processor;

    public FlowGateContainer(FlowGateConfig config) {
        this.config = config;
        init();
    }

    @Override
    public void init() {
        // 1、构建核心处理器
        NettyCoreProcessor coreProcessor = new NettyCoreProcessor();
        // 2、是否开启缓存
        String bufferType = config.getBufferType();
        if (FlowGateBufferHelper.isFlusher(bufferType)){
            processor = new NettyBatchEventProcessor(config, coreProcessor);
        }else if (FlowGateBufferHelper.isMpmc(bufferType)){
            processor = new NettyMpmcProcessor(config, coreProcessor);
        }else {
            this.processor = coreProcessor;
        }
        // 3、创建httpserver
        nettyHttpServer = new NettyHttpServer(config, processor);
        // 4、创建httpclient
        nettyHttpClient = new NettyHttpClient(config, nettyHttpServer.getEventLoopGroupWork());
    }

    @Override
    public void start() {
        processor.start();
        nettyHttpServer.start();
        nettyHttpClient.start();
        log.info("FlowGateContainer start !");
    }

    @Override
    public void shutdown() {
        processor.shutdown();
        nettyHttpServer.shutdown();
        nettyHttpClient.shutdown();
        log.info("FlowGateContainer shutdown !");
    }
}
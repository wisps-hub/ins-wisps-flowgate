package com.wisps.flowgate.netty;

import com.wisps.flowgate.core.FlowGateConfig;
import com.wisps.flowgate.core.LifeCycle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;

public class NettyHttpServer implements LifeCycle {

    private final FlowGateConfig config;
    private int port = 8888;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup EventLoopGroupBoss;
    private EventLoopGroup EventLoopGroupWork;


    public NettyHttpServer(FlowGateConfig config) {
        this.config = config;
        port = config.getPort() > 0 && config.getPort() < 65535? config.getPort() : port;
        init();
    }

    @Override
    public void init() {
        this.serverBootstrap = new ServerBootstrap();

        this.EventLoopGroupBoss = null;
        this.EventLoopGroupWork = null;
    }

    public boolean useEPoll(){
        
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}

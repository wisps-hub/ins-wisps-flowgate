package com.wisps.flowgate.netty;

import com.wisps.flowgate.core.FlowGateConfig;
import com.wisps.flowgate.core.LifeCycle;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

/**
 * http 客户端实现类 请求下游服务
 */
public class NettyHttpClient implements LifeCycle {

    private AsyncHttpClient asyncHttpClient;
    private DefaultAsyncHttpClientConfig.Builder cli
    private FlowGateConfig config;

    @Override
    public void init() {
        
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}

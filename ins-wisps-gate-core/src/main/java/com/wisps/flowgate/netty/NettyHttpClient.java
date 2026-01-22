package com.wisps.flowgate.netty;

import com.wisps.flowgate.core.FlowGateConfig;
import com.wisps.flowgate.core.LifeCycle;
import com.wisps.flowgate.helper.AsyncHttpHelper;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;

/**
 * http 客户端实现类 请求下游服务
 */
@Slf4j
public class NettyHttpClient implements LifeCycle {

    private AsyncHttpClient asyncHttpClient;
    private DefaultAsyncHttpClientConfig.Builder clientBuilder;
    private FlowGateConfig config;
    private EventLoopGroup eventLoopGroupWork;

    public NettyHttpClient(FlowGateConfig config, EventLoopGroup eventLoopGroupWork) {
        this.config = config;
        this.eventLoopGroupWork = eventLoopGroupWork;
        init();
    }

    @Override
    public void init() {
        this.clientBuilder = new DefaultAsyncHttpClientConfig.Builder()
                .setFollowRedirect(false)
                .setEventLoopGroup(eventLoopGroupWork)
                .setConnectTimeout(config.getHttpConnectTimeout())
                .setRequestTimeout(config.getHttpRequestTimeout())
                .setMaxRequestRetry(config.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(config.getHttpMaxConnections())
                .setMaxConnectionsPerHost(config.getHttpMaxConnectionsPerHost())
                .setPooledConnectionIdleTimeout(config.getHttpPooledConnectionIdleTimeout());
    }

    @Override
    public void start() {
        this.asyncHttpClient = new DefaultAsyncHttpClient(clientBuilder.build());
        AsyncHttpHelper.getInstance().initialized(asyncHttpClient);
        log.info("NettyHttpClient start");
    }

    @Override
    public void shutdown() {
        if (asyncHttpClient != null){
            try {
                asyncHttpClient.close();
                log.info("NettyHttpClient shutdown");
            } catch (IOException e) {
                log.error("asyncHttpClient close fail");
                throw new RuntimeException(e);
            }
        }
    }
}

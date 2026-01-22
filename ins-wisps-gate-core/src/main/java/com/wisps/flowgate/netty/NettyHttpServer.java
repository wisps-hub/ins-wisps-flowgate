package com.wisps.flowgate.netty;

import com.wisps.flowgate.common.utils.RemotingHelper;
import com.wisps.flowgate.common.utils.RemotingUtil;
import com.wisps.flowgate.core.FlowGateConfig;
import com.wisps.flowgate.core.LifeCycle;
import com.wisps.flowgate.processor.NettyProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * http 服务端实现类 接受并处理请求
 */
@Slf4j
public class NettyHttpServer implements LifeCycle {

    private final FlowGateConfig config;
    private int port = 8888;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup eventLoopGroupBoss;
    private EventLoopGroup eventLoopGroupWork;
    private NettyProcessor processor;

    public NettyHttpServer(FlowGateConfig config, NettyProcessor processor) {
        this.config = config;
        this.processor = processor;
        port = config.getPort() > 0 && config.getPort() < 65535? config.getPort() : port;
        init();
    }

    @Override
    public void init() {
        this.serverBootstrap = new ServerBootstrap();
        if (useEPoll()){
            this.eventLoopGroupBoss = new EpollEventLoopGroup(config.getEventLoopGroupBossCnt(),
                    new DefaultThreadFactory("Netty-boss-epoll"));
            this.eventLoopGroupWork = new EpollEventLoopGroup(config.getEventLoopGroupWorkCnt(),
                    new DefaultThreadFactory("Netty-work-epoll"));
        }else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(config.getEventLoopGroupBossCnt(),
                    new DefaultThreadFactory("Netty-boss-nio"));
            this.eventLoopGroupWork = new NioEventLoopGroup(config.getEventLoopGroupWorkCnt(),
                    new DefaultThreadFactory("Netty-work-nio"));
        }
    }

    @Override
    public void start() {
        ServerBootstrap bootstrap = this.serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWork)
                .channel(useEPoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024) // async + accept = backlog
                .option(ChannelOption.SO_REUSEADDR, true) //tcp端口重绑定
                .option(ChannelOption.SO_KEEPALIVE, false) //如果2H内无数据通讯，tcp会自动发送探活报文
                .childOption(ChannelOption.TCP_NODELAY, true) //禁用nagle算法 （小数据传输合并）
                .childOption(ChannelOption.SO_SNDBUF, 65535) //设置发送数据缓冲区大小
                .childOption(ChannelOption.SO_RCVBUF, 65535) //设置接受数据缓冲区大小
                .localAddress(new InetSocketAddress(this.port))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(config.getMaxCountLength()),
                                new HttpServerExpectContinueHandler(),
                                new NettyServerConnectManagerHandler(),
                                new NettyHttpServerHandler(processor)
                        );
                    }
                });
        if (config.isNettyAllocator()){
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
        try {
            this.serverBootstrap.bind().sync();
            log.info("============ wispFlowGate startup on port {} ============", port);
        } catch (InterruptedException e) {
            log.info("============ wispFlowGate startup fail ============", e);
        }
    }

    @Override
    public void shutdown() {
        if (eventLoopGroupBoss != null){
            eventLoopGroupBoss.shutdownGracefully();
        }
        if (eventLoopGroupWork != null){
            eventLoopGroupWork.shutdownGracefully();
        }
    }

    public EventLoopGroup getEventLoopGroupWork() {
        return eventLoopGroupWork;
    }

    public boolean useEPoll(){
        return config.isUseEpoll() && RemotingUtil.isLinuxPlatform() && Epoll.isAvailable();
    }

    //链接管理器handler
    static class NettyServerConnectManagerHandler extends ChannelDuplexHandler{

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoterAddr(ctx.channel());
            log.info("netty server pipeline: channelRegistered {}", remoteAddr);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoterAddr(ctx.channel());
            log.info("netty server pipeline: channelUnregistered {}", remoteAddr);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoterAddr(ctx.channel());
            log.info("netty server pipeline: channelActive {}", remoteAddr);
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoterAddr(ctx.channel());
            log.info("netty server pipeline: channelInactive {}", remoteAddr);
            super.channelInactive(ctx);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent){
                IdleStateEvent event = (IdleStateEvent) evt;
                if (IdleState.ALL_IDLE.equals(event.state())) {
                    String remoteAddr = RemotingHelper.parseChannelRemoterAddr(ctx.channel());
                    log.warn("netty server pipeline: userEventTriggered IDLE {}", remoteAddr);
                    ctx.channel().close();
                }
            }
            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            String remoteAddr = RemotingHelper.parseChannelRemoterAddr(ctx.channel());
            log.warn("netty server pipeline: exceptionCaught {}, {}", remoteAddr, cause);
            ctx.channel().close();
        }
    }
}

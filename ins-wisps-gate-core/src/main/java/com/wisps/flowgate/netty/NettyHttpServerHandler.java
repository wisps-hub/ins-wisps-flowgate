package com.wisps.flowgate.netty;

import com.wisps.flowgate.common.utils.ReferenceContentUtil;
import com.wisps.flowgate.context.HttpRequestWrapper;
import com.wisps.flowgate.processor.NettyProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpRequest;

//SimpleChannelInboundHandler
@Slf4j
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private NettyProcessor processor;

    public NettyHttpServerHandler(NettyProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper();
            httpRequestWrapper.setFullHttpRequest(request);
            httpRequestWrapper.setCtx(ctx);
            processor.process(httpRequestWrapper);
        }else {
            //never go this way
            log.warn("message type is not HttpRequest");
            boolean release = ReferenceContentUtil.release(msg);
            if (!release){
                log.error("release faile");
            }
        }
        super.channelRead(ctx, msg);
    }
}
package com.wisps.flowgate.processor;

import com.wisps.flowgate.context.HttpRequestWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyCoreProcessor implements NettyProcessor{
    @Override
    public void process(HttpRequestWrapper event) {
        FullHttpRequest fullHttpRequest = event.getFullHttpRequest();
        ChannelHandlerContext ctx = event.getCtx();
        try {
            // todo hlp
            // 解析request
            //
        }catch (Throwable t){}
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
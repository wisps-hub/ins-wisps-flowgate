package com.wisps.flowgate.core;

import com.wisps.flowgate.common.consts.BaseConst;
import com.wisps.flowgate.common.utils.NetUtil;
import com.wisps.flowgate.helper.FlowGateBufferHelper;
import lombok.Data;

@Data
public class FlowGateConfig {
    /** 服务端口 */
    private int port = 8888;
    /** 服务端口 */
    private String flowGateId = NetUtil.getLocalIp() + BaseConst.COLON_SEPARATOR + port;
    /** 网关的注册中心地址 */
    private String registerAddress = "http://127.0.0.1:2379,http://127.0.0.1:2379,http://127.0.0.1:2379";
    /** 网关的命名空间 */
    private String nameSpace = "flowGate-dev";
    /** 网关服务的CPU核数映射的线程数 */
    private int processThread = Runtime.getRuntime().availableProcessors();
    /** Netty的Boss线程数 */
    private int eventLoopGroupBossCnt = 1;
    /** Netty的Work线程数 */
    private int eventLoopGroupWorkCnt = processThread;
    /** 是否开启EPOLL */
    private boolean useEpoll = true;
    /** 是否开启Netty的内存分配机制 */
    private boolean nettyAllocator = true;
    /** http body 报文最大大小 64M*/
    private int maxCountLength = 1024 * 1024 * 64;
    /** httpAsync 参数选项*/

    /** dubbo 开启链接数数量*/
    private int dubboConnections = processThread;
    /** 设置响应模式 默认单异步模式；CompletableFuture回调处理结果：WhenComplete or WhenCompleteAsync */
    private boolean whenComplete = true;

    /** 网关队列-缓冲模式 */
    private String bufferType = FlowGateBufferHelper.FLUSHER;
    /** 网关队列-内存大小 */
    private int bufferSize = 1024 * 16;
    /** 网关队列-阻塞策略 */
    private String waitStrategy = "blocking";

    /** todo http async 参数 */
    /** 连接超时 默认30s*/
    private int httpConnectTimeout = 30_000;
    /** 请求超时 默认30s*/
    private int httpRequestTimeout = 30_000;
    /** 客户端最大请求重试次数 */
    private int httpMaxRequestRetry = 2;
    /** 客户端最大连接数 */
    private int httpMaxConnections = 10000;
    /** 客户端每个地址支持的最大连接数 */
    private int httpMaxConnectionsPerHost = 8000;
    /** 客户端空闲连接超时时间 默认60s */
    private int httpPooledConnectionIdleTimeout = 60_000;
}
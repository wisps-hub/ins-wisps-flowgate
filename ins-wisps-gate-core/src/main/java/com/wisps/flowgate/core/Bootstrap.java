package com.wisps.flowgate.core;

/**
 * 项目启动主入口
 */
public class Bootstrap {
    public static void main(String[] args) {
        //加载网关配置信息
        FlowGateConfig config = FlowGateConfigLoader.getInstance().load(args);
        //插件初始化工作

        //初始化服务注册管理中心，监听动态配置新增、修改、删除

        //启动容器
        FlowGateContainer flowGateContainer = new FlowGateContainer(config);
        flowGateContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(flowGateContainer::shutdown));
    }
}

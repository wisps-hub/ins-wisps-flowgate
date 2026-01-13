package com.wisps.flowgate.core;

import com.wisps.flowgate.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 网关配置信息加载类
 * 运行参数 > jvm参数 > 环境变量 > 配置文件 > 默认对象值
 */
@Slf4j
public class FlowGateConfigLoader {
    private static final String CONFIG_FILE = "flowgate.properties";

    private static final FlowGateConfigLoader LOADER = new FlowGateConfigLoader();

    private FlowGateConfig config = new FlowGateConfig();

    private FlowGateConfigLoader() {
    }

    public static FlowGateConfigLoader getInstance(){
        return LOADER;
    }

    public static FlowGateConfig getFlowGateConfig(){
        return LOADER.config;
    }

    public FlowGateConfig load(String args){
        //读取配置文件
        try (InputStream is = FlowGateConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)){
            if (is != null){
                Properties props = new Properties();
                props.load(is);
                PropertiesUtil.prop2Object(props, config);
            }
        }catch (Exception e){
            log.warn("FlowGateConfigLoader load config file fail", e);
        }
        //环境变量
        //jvm参数
        //运行参数
        return config;
    }
}

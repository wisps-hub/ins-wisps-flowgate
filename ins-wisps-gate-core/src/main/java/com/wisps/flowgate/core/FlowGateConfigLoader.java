package com.wisps.flowgate.core;

import com.wisps.flowgate.common.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 网关配置信息加载类
 * 运行参数 > jvm参数 > 环境变量 > 配置文件 > 默认对象值
 */
@Slf4j
public class FlowGateConfigLoader {
    private static final String CONFIG_ENV_PREFIX = "FLOWGATE_";
    private static final String CONFIG_JVM_PREFIX = "flowgate.";
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

    public FlowGateConfig load(String[] args){
        //读取配置文件
        try (InputStream is = FlowGateConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)){
            if (is != null){
                Properties cfProps = new Properties();
                cfProps.load(is);
                PropertiesUtil.prop2Object(cfProps, config);
            }
        }catch (Exception e){
            log.warn("FlowGateConfigLoader load config file fail", e);
        }
        //环境变量
        Map<String, String> env = System.getenv();
        Properties envProps = new Properties();
        envProps.putAll(env);
        PropertiesUtil.prop2Object(envProps, config, CONFIG_ENV_PREFIX);
        //jvm参数
        Properties jvmProps = System.getProperties();
        PropertiesUtil.prop2Object(jvmProps, config, CONFIG_JVM_PREFIX);
        //运行参数
        if (args != null && args.length > 0){
            Properties RunProps = new Properties();
            for (String arg : args) {
                if (arg.startsWith("--") && arg.contains("==")){
                    RunProps.put(arg.substring(2, arg.indexOf("=")), arg.substring(arg.indexOf("=") + 1));
                }
            }
            PropertiesUtil.prop2Object(RunProps, config);
        }
        return config;
    }
}
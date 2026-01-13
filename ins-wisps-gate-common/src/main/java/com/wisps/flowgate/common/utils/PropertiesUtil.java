package com.wisps.flowgate.common.utils;

import java.lang.reflect.Method;
import java.util.Properties;

public class PropertiesUtil {
    public static void prop2Object(Properties props, Object object){
        prop2Object(props, object, "");
    }

    private static void prop2Object(Properties props, Object object, String prefix) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set")){
                try {
                    String mPrefix = methodName.substring(4);
                    String first = methodName.substring(3, 4);

                    String key = prefix + first.toLowerCase() + mPrefix;
                    String property = props.getProperty(key);
                    if (property != null){
                        Class<?>[] paramTypes = method.getParameterTypes();
                        if (paramTypes != null && paramTypes.length > 0){
                            String sn = paramTypes[0].getSimpleName();
                            Object arg = null;
                            if(sn.equals("int") || sn.equals("Integer")){
                                arg = Integer.parseInt(sn);
                            }else if (sn.equals("long") || sn.equals("Long")){
                                arg = Long.parseLong(sn);
                            }else if (sn.equals("double") || sn.equals("Double")){
                                arg = Double.parseDouble(sn);
                            }else if (sn.equals("boolean") || sn.equals("Boolean")){
                                arg = Boolean.parseBoolean(sn);
                            }else if (sn.equals("float") || sn.equals("Float")){
                                arg = Float.parseFloat(sn);
                            }else if (sn.equals("String")){
                                arg = sn;
                            }
                            method.invoke(object, arg);
                        }
                    }

                }catch (Exception ignored){}
            }
        }

    }
}

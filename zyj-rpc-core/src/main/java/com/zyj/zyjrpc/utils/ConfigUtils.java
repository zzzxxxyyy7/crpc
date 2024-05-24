package com.zyj.zyjrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {

    public static <T> T loadConfig(Class<T> tClass , String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    public static <T>T loadConfig(Class<T> tclass, String prefix, String environment){
        StringBuilder configFileBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".yaml");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tclass,prefix);
    }
}
package com.zyj.zyjrpc.registry;

import com.zyj.zyjrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心客户端本地缓存
 */
public class RegisterServiceCache {
    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceMetaInfoList;

    /**
     * 写入缓存
     * @param newServiceMetaInfoList
     */
    public void writCache(List<ServiceMetaInfo> newServiceMetaInfoList) {
        this.serviceMetaInfoList = newServiceMetaInfoList;
    }

    /**
     * 读取缓存
     * @return
     */
    public List<ServiceMetaInfo> readCache() {
        return this.serviceMetaInfoList;
    }

    /**
     * 清空客户端服务缓存
     */
    public void clearCache() {
        this.serviceMetaInfoList = null;
    }
}
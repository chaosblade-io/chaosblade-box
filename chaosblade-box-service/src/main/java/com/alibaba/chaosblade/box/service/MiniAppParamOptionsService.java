package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.annotation.ExtensionPoint;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;

import java.util.Set;

/**
 * @author haibin
 *
 *
 */
@ExtensionPoint
public interface MiniAppParamOptionsService {

    /**
     * 获取所有的网络设备
     *
     * @return
     */
    public Set<String> queryNetworkDevice(ParamOptionsQueryRequest paramOptionsQueryRequest);

    /**
     * 获取所有的磁盘
     *
     * @return
     */
    public Set<String> queryDiskDevice(ParamOptionsQueryRequest paramOptionsQueryRequest);

    /**
     * 获取io hang的块设备
     *
     * @param paramOptionsQueryRequest
     * @return
     */
    public Set<String> queryDiskBlockDevice(ParamOptionsQueryRequest paramOptionsQueryRequest);

    /**
     * 通用的参数描述信息
     *
     * @param paramOptionsQueryRequest
     * @return
     */
    public Set<String> queryParamOptions(ParamOptionsQueryRequest paramOptionsQueryRequest);

}

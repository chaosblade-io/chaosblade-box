package com.alibaba.chaosblade.box.dao.infrastructure.app.function;


import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;

/**
 * 用来对参数进行加密和解密操作
 *
 * @author haibin
 *
 *
 */
public interface SceneFunctionParameterEncoderAndDecoder {

    /**
     * 加密参数值
     *
     * @param appCode                         小程序code
     * @param alias                           参数名
     * @param value                           参数原始值
     * @param sceneFunctionParameterComponent 参数组件信息
     * @return 编码后的参数值
     * @throws Exception
     */
    public String encodeValue(String appCode, String alias, String value,
        SceneFunctionParameterComponent sceneFunctionParameterComponent) throws Exception;

    /**
     * 解密参数值
     *
     * @param appCode                         小程序code
     * @param alias                           参数名
     * @param value                           参数原始值
     * @param sceneFunctionParameterComponent 参数组件信息
     * @return 解码后的值
     * @throws Exception
     */
    public String decodeValue(String appCode, String alias, String value,
        SceneFunctionParameterComponent sceneFunctionParameterComponent) throws Exception;

    /**
     * 判断当前参数是否需要进行编码或者解码操作
     *
     * @param sceneFunctionParameterComponent 参数组件信息
     * @return true需要
     */
    public boolean supportSceneFunctionParameterComponent(
        SceneFunctionParameterComponent sceneFunctionParameterComponent);

}

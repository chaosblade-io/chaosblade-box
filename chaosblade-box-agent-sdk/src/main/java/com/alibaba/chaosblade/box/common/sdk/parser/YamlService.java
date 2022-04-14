package com.alibaba.chaosblade.box.common.sdk.parser;

import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;

import java.io.InputStream;

/**
 * @author Changjun Xiao
 */
public interface YamlService {

    /**
     * yaml inputstream
     *
     * @param source
     * @return
     */
    InputStream getInputStream(String source);

    /**
     * 关闭流
     */
    void close();

    /**
     * 解析 yaml 为 bladeModels
     *
     * @param source
     * @return
     */
    ChaosModels parse(String source);
}

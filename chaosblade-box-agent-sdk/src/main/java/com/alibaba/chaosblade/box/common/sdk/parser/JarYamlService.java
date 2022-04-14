package com.alibaba.chaosblade.box.common.sdk.parser;

import java.io.InputStream;

/**
 * @author Changjun Xiao
 */
public class JarYamlService extends BaseYamlService {

    @Override
    public InputStream getInputStream(String source) {
        inputStream = getClass().getClassLoader().getResourceAsStream(source);
        return inputStream;
    }

}

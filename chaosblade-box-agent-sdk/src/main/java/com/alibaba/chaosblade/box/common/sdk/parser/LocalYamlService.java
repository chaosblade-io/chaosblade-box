package com.alibaba.chaosblade.box.common.sdk.parser;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Changjun Xiao
 */
public class LocalYamlService extends BaseYamlService {

    @Override
    public InputStream getInputStream(String source) {
        try {
            inputStream = new FileInputStream(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}

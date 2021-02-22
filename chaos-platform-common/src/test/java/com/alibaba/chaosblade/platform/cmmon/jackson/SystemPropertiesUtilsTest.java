package com.alibaba.chaosblade.platform.cmmon.jackson;

import com.alibaba.chaosblade.platform.cmmon.utils.SystemPropertiesUtils;
import org.junit.Test;

/**
 * @author yefei
 */
public class SystemPropertiesUtilsTest {

    @Test
    public void getEncoding() {
        String propertiesValue = SystemPropertiesUtils.getPropertiesValue("file.encoding");
        System.out.println(propertiesValue);
    }
}

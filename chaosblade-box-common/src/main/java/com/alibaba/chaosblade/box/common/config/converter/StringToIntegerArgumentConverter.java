package com.alibaba.chaosblade.box.common.config.converter;


import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;

/**
 * @author haibin.lhb
 *
 *
 */
public class StringToIntegerArgumentConverter implements ArgumentTypeConverter<String, Integer> {
    @Override
    public Integer convert(String from) {
        return Integer.valueOf(from);
    }
}

package com.alibaba.chaosblade.box.common.config.converter;


import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;

/**
 * @author haibin.lhb
 *
 * 
 */
public class StringToBooleanArgumentConverter implements ArgumentTypeConverter<String,Boolean> {
    @Override
    public Boolean convert(String from) {
        return Boolean.parseBoolean(from);
    }
}

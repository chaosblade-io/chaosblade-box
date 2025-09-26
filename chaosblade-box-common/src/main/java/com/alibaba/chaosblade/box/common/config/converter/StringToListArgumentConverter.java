package com.alibaba.chaosblade.box.common.config.converter;

import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/** @author haibin.lhb */
public class StringToListArgumentConverter implements ArgumentTypeConverter<String, List<String>> {

  @Override
  public List<String> convert(String from) {
    return Lists.newArrayList(StringUtils.splitByWholeSeparator(from, ","));
  }
}

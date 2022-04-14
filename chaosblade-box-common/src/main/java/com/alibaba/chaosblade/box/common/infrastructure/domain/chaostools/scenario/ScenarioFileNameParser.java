package com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.model.PluginSpecBean;

/**
 * @author yefei
 */
public class ScenarioFileNameParser {

    public static PluginSpecBean parse(String fileName) {
        String[] split = StrUtil.split(fileName, "-");
        return PluginSpecBean.builder()
                .kind(split[0])
                .type(split[1])
                .version(split[3].replace(".yaml", ""))
                .build();
    }

    public static String toFileName(PluginSpecBean pluginSpecBean) {
        String specFileName = String.format("%s-%s-spec-%s.yaml",
                pluginSpecBean.getKind(),
                pluginSpecBean.getType(),
                pluginSpecBean.getVersion()
        );
        return specFileName;
    }
}

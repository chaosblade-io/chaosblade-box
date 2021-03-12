package com.alibaba.chaosblade.box.scenario.litmus;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.chaosblade.box.common.enums.ChaosTools;
import com.alibaba.chaosblade.box.common.model.chaos.PluginSpecBean;
import com.alibaba.chaosblade.box.common.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.box.scenario.api.ScenarioFileNameParser;
import com.alibaba.chaosblade.box.scenario.api.ScenarioRequest;
import com.alibaba.chaosblade.box.scenario.api.model.ScenarioOriginal;
import com.alibaba.chaosblade.box.scenario.api.model.ToolsVersion;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yefei
 */
public class LitmusScenarioParserMain {

    public static void main(String[] args) {
        File[] ls = FileUtil.ls(ChaosTools.LITMUS_CHAOS.getName());
        if (ArrayUtil.isEmpty(ls)) {
            return;
        }
        Representer representer = new Representer();
        representer.setPropertyUtils(new PropertyUtils() {
            @Override
            public Property getProperty(Class<? extends Object> type, String name) {
                if (name.indexOf('-') > -1) {
                    name = name.replace('-', '_');
                }
                return super.getProperty(type, name);
            }
        });

        for (File file : ls) {
            if (file.isFile()) {
                continue;
            }

            ToolsVersion toolsVersion = new Yaml(representer).loadAs(
                    FileUtil.getInputStream(file.getPath() + "/version.yaml")
                    , ToolsVersion.class);

            for (String scenarioFile : toolsVersion.getScenarioFiles()) {
                PluginSpecBean bean = ScenarioFileNameParser.parse(scenarioFile);

                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
                LitmusScenarioParser parser = context.getBean(LitmusScenarioParser.class);
                ArrayList<ScenarioOriginal> litmus = new ArrayList<>();

                litmus.add(ScenarioOriginal.builder()
                        .name(bean.getType())
                        .version(toolsVersion.getVersion())
                        .url(String.format("https://hub.litmuschaos.io/api/chaos/%s?file=charts/%s/experiments.yaml", bean.getVersion(), bean.getType()))
                        .build()
                );

                parser.setLitmus(litmus);
                List<PluginSpecBean> parse = parser.parse(ScenarioRequest.builder().build());

                parse.forEach(pluginSpecBean -> {

                    String path = pluginSpecBean.getKind() + File.separatorChar + pluginSpecBean.getVersion();

                    Yaml yaml = new Yaml();
                    String dump = yaml.dumpAs(pluginSpecBean, Tag.MAP, DumperOptions.FlowStyle.BLOCK);

                    FileUtil.writeString(dump, path + "/" + ScenarioFileNameParser.toFileName(pluginSpecBean), SystemPropertiesUtils.getPropertiesFileEncoding());
                });
            }
        }
    }

    @ComponentScan("com.alibaba.chaosblade.box.scenario.litmus")
    private static class Config {

    }
}

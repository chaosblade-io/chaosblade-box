package com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.model.PluginSpecBean;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.model.ToolsVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author yefei
 */
@Slf4j
@Component
public class DefaultScenarioYamlProvider implements ScenarioYamlProvider, InitializingBean {

    private final static String VERSION_YAML = Constants.VERSION_YAML;

    protected Map<String, String> versionYaml = new ConcurrentHashMap<>();

    protected Map<String, String> specYaml = new ConcurrentHashMap<>();

    private Map<String, ScenarioParser> parserMap = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    private String sceneMarket;

    @Override
    public void afterPropertiesSet() {
        if (StrUtil.isBlank(sceneMarket)) {
            Map<String, ScenarioParser> beansOfType = applicationContext.getBeansOfType(ScenarioParser.class);
            for (Map.Entry<String, ScenarioParser> entry : beansOfType.entrySet()) {
                Original original = entry.getValue().getClass().getAnnotation(Original.class);
                if (original != null) {
                    parserMap.put(original.value().getName(), entry.getValue());
                }
            }
        }
    }

    public void generateSpec(List<PluginSpecBean> pluginSpecBeanList) {

        Consumer<PluginSpecBean> pluginSpecBeanConsumer = pluginSpecBean -> {

            String path = pluginSpecBean.getKind() + File.separatorChar + pluginSpecBean.getVersion();

            Yaml yaml = new Yaml();
            String dump = yaml.dumpAs(pluginSpecBean, Tag.MAP, DumperOptions.FlowStyle.BLOCK);

            String specFileName = String.format("%s-%s-spec-%s.yaml",
                    pluginSpecBean.getKind(),
                    pluginSpecBean.getType(),
                    pluginSpecBean.getVersion()
            );
            specYaml.put(specFileName, dump);

            Representer representer = new Representer();
            representer.setPropertyUtils(new PropertyUtils() {
                @Override
                public Property getProperty(Class<? extends Object> type, String name) throws IntrospectionException {
                    if (name.indexOf('-') > -1) {
                        name = name.replace('-', '_');
                    }
                    return super.getProperty(type, name);
                }
            });

            String versionYamlFilePath = path + File.separatorChar + VERSION_YAML;
            ToolsVersion toolsVersion = new Yaml(representer).loadAs(ResourceUtil.getStream(versionYamlFilePath), ToolsVersion.class);

            if (toolsVersion.getScenarioFiles() == null) {
                toolsVersion.setScenarioFiles(new ArrayList<>());
            }

            if (!toolsVersion.getScenarioFiles().contains(specFileName)) {
                toolsVersion.getScenarioFiles().add(specFileName);
                versionYaml.put(pluginSpecBean.getVersion(), yaml.dumpAs(toolsVersion, Tag.MAP, DumperOptions.FlowStyle.BLOCK));
            }
        };
        pluginSpecBeanList.forEach(pluginSpecBeanConsumer);
    }

    @Override
    public String versionYaml(ScenarioRequest scenarioRequest) {
        String path = String.format("%s/%s/%s",
                scenarioRequest.getChaosTools(),
                scenarioRequest.getVersion(),
                Constants.VERSION_YAML);

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), Charset.defaultCharset());
        }

        String yaml = versionYaml.get(scenarioRequest.getVersion());
        if (yaml == null) {

            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, Charset.defaultCharset());
            }
        } else {
            return yaml;
        }
    }

    @Override
    public String specYaml(ScenarioRequest scenarioRequest) {
        String path = String.format("%s/%s/%s",
                scenarioRequest.getChaosTools(),
                scenarioRequest.getVersion(),
                scenarioRequest.getSpec());

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), Charset.defaultCharset());
        }

        String yaml = specYaml.get(scenarioRequest.getSpec());
        if (yaml == null) {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                ScenarioParser scenarioParser = parserMap.get(scenarioRequest.getChaosTools());
                if (scenarioParser != null) {
                    List<PluginSpecBean> parse = scenarioParser.parse(ScenarioRequest.builder().build());
                    generateSpec(parse);
                    return specYaml.get(scenarioRequest.getSpec());
                }
                return null;
            } else {
                return IoUtil.read(inputStream, Charset.defaultCharset());
            }
        } else {
            return yaml;
        }
    }


    @Override
    public String configuration(ScenarioRequest scenarioRequest) {
        String path = String.format("%s", Constants.CONFIGURATION_YAML);

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), Charset.defaultCharset());
        } else {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, Charset.defaultCharset());
            }
        }
    }

    @Override
    public String overview(ScenarioRequest scenarioRequest) {
        String path = String.format("tools/%s/%s",
                scenarioRequest.getChaosTools(),
                Constants.OVERVIEW_YAML);

        if (StrUtil.isNotBlank(sceneMarket)) {
            HttpRequest request = HttpUtil.createGet(sceneMarket + path);
            HttpResponse execute = request.execute();
            return IoUtil.read(execute.bodyStream(), Charset.defaultCharset());
        } else {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            } else {
                return IoUtil.read(inputStream, Charset.defaultCharset());
            }
        }
    }
}

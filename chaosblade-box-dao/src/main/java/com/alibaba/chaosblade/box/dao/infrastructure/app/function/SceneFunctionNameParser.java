package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.constant.ChaosConstant;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionDescription;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionDescriptionMessageLoadFactory;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author camix
 */
@Component
public class SceneFunctionNameParser {

    private static final Map<String, HashMap<String, String>> sceneType = new HashMap<String, HashMap<String, String>>(){{
        put("cpu", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"CPU Resources");
            put(ChaosConstant.LANGUAGE_ZH, "CPU资源");
        }});
        put("disk", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Disk Resources");
            put(ChaosConstant.LANGUAGE_ZH, "磁盘资源");
        }});
        put("delay", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Delay");
            put(ChaosConstant.LANGUAGE_ZH, "延迟");
        }});
        put("network", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Network Resources");
            put(ChaosConstant.LANGUAGE_ZH, "网络资源");
        }});
        put("mem", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Memory Resources");
            put(ChaosConstant.LANGUAGE_ZH, "内存资源");
        }});
        put("script", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Custom Fault");
            put(ChaosConstant.LANGUAGE_ZH, "自定义故障");
        }});
        put("sql", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Sql");
            put(ChaosConstant.LANGUAGE_ZH, "数据库");
        }});
        put("node", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Cluster Resources");
            put(ChaosConstant.LANGUAGE_ZH, "集群资源");
        }});
        put("pod", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Cluster Resources");
            put(ChaosConstant.LANGUAGE_ZH, "集群资源");
        }});
        put("container", new HashMap<String,String>(){{
            put(ChaosConstant.LANGUAGE_EN,"Cluster Resources");
            put(ChaosConstant.LANGUAGE_ZH, "集群资源");
        }});
    }};

    @Autowired
    private SceneFunctionDescriptionMessageLoadFactory sceneFunctionDescriptionMessageLoadFactory;

    public void parseSceneFunction(List<SceneFunctionDO> functions) {
        if(CollectionUtil.isNullOrEmpty(functions)) {
            return;
        }
        functions.forEach(sceneFunctionDO -> {
            String enName = sceneFunctionDO.getName();
            String functionName = ChaosBladeMetaData.getInstance().getFunctionNameParser(enName);
            sceneFunctionDO.setName(functionName);
        });
    }

    public void parseSceneFunctionOne(SceneFunctionDO sceneFunctionDO) {
        if(sceneFunctionDO == null) {
            return;
        }

        String enName = sceneFunctionDO.getName();
        String functionName = ChaosBladeMetaData.getInstance().getFunctionNameParser(enName);
        sceneFunctionDO.setName(functionName);

    }

    public String  parseFunctionName(String enName, String lang) {
        if (Strings.isNullOrEmpty(enName)|| lang.equals(ChaosConstant.LANGUAGE_EN)) {
            return enName;
        }

        return ChaosBladeMetaData.getInstance().getFunctionNameParser(enName);
    }

    public String getSceneTypeByTypeAndLang(String type, String lang) {
        if (sceneType.containsKey(type)) {
            if (sceneType.get(type).containsKey(lang)) {
                return sceneType.get(type).get(lang);
            }
        }
        return null;
    }
}

package com.alibaba.chaosblade.box.common.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 整个系统的动态配置中心
 *
 * @author haibin.lhb
 *
 *
 */
@Component
@Slf4j
public class ChaosSettings {

    /**
     * 配置信息,nameToInfo
     */
    private Map<String, ChaosSettingInfo> chaosSettingGroupItemInfoMap = new ConcurrentHashMap<>();

    private List<ConfigUpdateListener> configUpdateListeners = new ArrayList<>();

    public void batchUpdateConfigs(Map<String, String> settings) {
        if (settings == null) { return; }
        settings.forEach(this::updateConfig);
    }

    public List<ChaosSettingInfo> getConfigs() {
        return Lists.newArrayList(chaosSettingGroupItemInfoMap.values());
    }

    public void addConfigListeners(ConfigUpdateListener configUpdateListener) {
        configUpdateListeners.add(configUpdateListener);
    }

    /**
     * 获取某一项配置
     *
     * @param key
     * @return
     */
    public ChaosSettingInfo getConfig(String key) {
        return chaosSettingGroupItemInfoMap.get(key);
    }

    /**
     * 注册配置
     *
     * @param settingObject
     */
    public void registerSettings(Object settingObject) {
        ReflectionUtils.doWithFields(settingObject.getClass(), field -> {
            ChaosSettingDescriptor chaosSettingDescriptor = field.getAnnotation(
                ChaosSettingDescriptor.class);
            ChaosSettingInfo chaosSettingInfo = null;
            try {
                chaosSettingInfo = initConfig(settingObject, field,
                        chaosSettingDescriptor);
                chaosSettingGroupItemInfoMap.put(chaosSettingInfo.getName(),
                        chaosSettingInfo);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }, field -> field.getAnnotation(ChaosSettingDescriptor.class) != null);
    }

    /**
     * 初始化配置项目
     *
     * @param settingObject
     * @param field
     * @param chaosSettingDescriptor
     * @return
     * @throws IllegalAccessException
     */
    private ChaosSettingInfo initConfig(Object settingObject, java.lang.reflect.Field field,
                                        ChaosSettingDescriptor chaosSettingDescriptor) throws IllegalAccessException, InstantiationException {
        field.setAccessible(true);
        Object value = field.get(settingObject);
        String name = chaosSettingDescriptor.name();
        if (chaosSettingGroupItemInfoMap.containsKey(name)) {
            throw new IllegalArgumentException(
                "chaos settings duplicate,key:" + name);
        }
        ChaosSettingInfo chaosSettingInfo = new ChaosSettingInfo();
        ChaosSettingInfo.OwnerInfo ownerInfo = new ChaosSettingInfo.OwnerInfo();
        ownerInfo.setConverter(
            chaosSettingDescriptor.converters().length == 0 ? null
                : chaosSettingDescriptor.converters()[0].newInstance());
        ownerInfo.setObject(settingObject);
        ownerInfo.setField(field);
        chaosSettingInfo.setOwner(ownerInfo);
        chaosSettingInfo.setDefaultValue(value == null ? null : value.toString());
        chaosSettingInfo.setValue(chaosSettingInfo.getDefaultValue());
        chaosSettingInfo.setPreValue(chaosSettingInfo.getDefaultValue());
        chaosSettingInfo.setDescription(chaosSettingDescriptor.description());
        chaosSettingInfo.setEnabled(chaosSettingDescriptor.enabled());
        chaosSettingInfo.setName(name);
        chaosSettingInfo.setGroup(chaosSettingDescriptor.group());
        chaosSettingInfo.setPriority(chaosSettingDescriptor.priority().name());
        chaosSettingInfo.setDynamic(chaosSettingDescriptor.dynamic());
        return chaosSettingInfo;
    }

    /**
     * 只是支持string
     *
     * @param key
     * @param value
     */

    public void updateConfig(String key, String value) {
        if (key == null) { return; }
        ChaosSettingInfo chaosSettingInfo = chaosSettingGroupItemInfoMap.get(key);
        if (chaosSettingInfo == null) {
            log.error("config not exist:{}", key);
            return;
        }
        chaosSettingInfo.setPreValue(chaosSettingInfo.getValue());
        chaosSettingInfo.setValue(value);
        if (!chaosSettingInfo.isDynamic()) { return; }
        if (chaosSettingInfo.valueChanged()) {
            try {
                chaosSettingInfo.updateOwnerValue();
            } catch (IllegalAccessException e) {
                log.error("update value failed", e);
            }
            fireListeners(chaosSettingInfo);
        }
    }

    private void fireListeners(ChaosSettingInfo chaosSettingInfo) {
        for (ConfigUpdateListener configUpdateListener : configUpdateListeners) {
            if (configUpdateListener.support(chaosSettingInfo)) {
                try {
                    configUpdateListener.onUpdate(chaosSettingInfo);
                } catch (Exception exception) {
                    log.warn("handler config key failed,key:{},newValue:{}", chaosSettingInfo.getName(),
                        chaosSettingInfo.getValue(), exception
                    );
                }
            }
        }
    }

}

package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * 实验流程节点参数定义
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentNodeArgumentsDefinition {

    /**
     * 已废弃
     */
    @Deprecated
    Map<String, String> matcher = new HashMap<>();

    /**
     * 如果小程序是chaos.xxxx.xxx这样的类型，参数就填这里
     */
    Map<String, String> action = new HashMap<>();

    /**
     * 如果小程序不是以chaos开头，属于用户自定义的小程序，需要把参数填在这里
     */
    @JSONField(name = "user_args")
    Map<String, String> userArgs = new HashMap<>();

    Map<String, SceneFunctionParameterComponent> argsComponents = new HashMap<>();

    public void addArgs(String key, String value) {
        if (matcher != null) {
            matcher.remove(key);
        }
        if (action != null) {
            action.put(key, value);
        }
        if (userArgs != null) {
            userArgs.put(key, value);
        }
    }

    public void removeArg(String key) {
        if (matcher != null) {
            matcher.remove(key);
        }
        if (action != null) {
            action.remove(key);
        }
        if (userArgs != null) {
            userArgs.remove(key);
        }
    }

    @JSONField(serialize = false)
    public Map<String, String> getAllArguments() {
        Map<String, String> arguments = new HashMap<>();
        if (action != null) {
            arguments.putAll(action);
        }
        if (matcher != null) {
            arguments.putAll(matcher);
        }
        if (userArgs != null) {
            arguments.putAll(userArgs);
        }
        return arguments;
    }

}

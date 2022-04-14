package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import lombok.Data;

import java.util.List;

/**
 * 局部参数定义
 *
 * 用于解决chaosblade中不同含义的参数名称相同，由于全局参数已定义导致参数解析有误的情况
 *
 * @author sunpeng
 *
 *
 */
@Data
public class LocalSceneFunctionParam {

    private String name;

    /**
     * 场景类型，模糊匹配
     *
     * String contains 匹配，如果方法code包含则匹配
     */
    private List<String> includeScene;

    /**
     * 场景类型，模糊匹配排除
     *
     * String contains 匹配，如果方法code包含则匹配
     */
    private List<String> excludeScene;

    /**
     * 场景code，单个code精确匹配
     */
    private List<String> includeCode;

    /**
     * 排除场景code，单个code精确匹配
     */
    private List<String> excludeCode;

    /**
     * 参数内容
     */
    private SceneFunctionParameterDO param;


}

package com.alibaba.chaosblade.box.dao.infrastructure.application;

import java.util.List;
import java.util.Map;

/**
 * 参数过滤，如果演练时应用维度，很多的小程序参数是不需要给用户展示的,所以需要做一层过滤
 *
 * @author haibin
 *
 *
 */
public interface ApplicationSceneFunctionParamFilter {

    /**
     * 是否屏蔽这个参数
     *
     * @param appCode    小程序
     * @param paramAlias
     * @return
     */
    public boolean skip(String appCode, String paramAlias);

    /**
     * 加载配置
     *
     * @param configs
     */
    public void load(Map<String, List<String>> configs);
}

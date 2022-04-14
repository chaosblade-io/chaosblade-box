package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;

import java.util.List;

/**
 * 小程序参数过滤
 *
 * @author haibin
 *
 * 
 */
public interface SceneFunctionParametersInterceptor {

    /**
     * 小程序参数过滤
     *
     * @param sceneFunctionDO 小程序函数
     * @param parameters      小程序参数
     */
    public void afterQuery(SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters);

    /**
     * 启动时候会去同步小程序,在同步之前做拦截
     *
     * @param functionWrapper
     */
    public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper);

}

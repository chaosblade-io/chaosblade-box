package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Author: sunju
 *
 * Date:   2020/1/3
 */
@Component
public class MetricParametersInterceptor implements SceneFunctionParametersInterceptor {

    private static final String METRIC_CODE = "chaosapp.metric";

    @Override
    public void afterQuery(SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters) {
        if (null != sceneFunctionDO && sceneFunctionDO.getCode().startsWith(METRIC_CODE)) {
            parameters.removeIf(parameter -> Objects.equals(parameter.getAlias(), "from"));
            parameters.removeIf(parameter -> Objects.equals(parameter.getAlias(), "to"));
        }
    }

    @Override
    public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {

    }

}

package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.google.common.base.Predicate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 * 
 * 
 */
@Component
public class GlobalHiddenParamsInterceptor implements SceneFunctionParametersInterceptor {

    @Override
    public void afterQuery(SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters) {
        parameters.removeIf((Predicate<SceneFunctionParameterDO>)input -> {
            if (input == null) { return false; }
            return SceneSynchronousHelper.getGlobalHiddenParams().contains(input.getAlias());
        });

    }

    @Override
    public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {

    }
}

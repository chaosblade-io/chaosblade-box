package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author haibin
 *
 *
 */
@Component
public class SceneFunctionParameterInterceptors {

    @Autowired
    private List<SceneFunctionParametersInterceptor> sceneFunctionParametersInterceptors;

    public List<SceneFunctionParameterDO> doUserParameterFilter(SceneFunctionDO sceneFunctionDO,
                                                                List<SceneFunctionParameterDO> sceneFunctionParameterDOS) {
        for (SceneFunctionParametersInterceptor sceneFunctionParametersInterceptor :
            sceneFunctionParametersInterceptors) {
            sceneFunctionParametersInterceptor.afterQuery(sceneFunctionDO, sceneFunctionParameterDOS);
        }
        return sceneFunctionParameterDOS;
    }

    public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {
        sceneFunctionParametersInterceptors.forEach(new Consumer<SceneFunctionParametersInterceptor>() {
            @Override
            public void accept(SceneFunctionParametersInterceptor sceneFunctionParametersInterceptor) {
                sceneFunctionParametersInterceptor.beforeSync(functionWrapper);
            }
        });
    }

}

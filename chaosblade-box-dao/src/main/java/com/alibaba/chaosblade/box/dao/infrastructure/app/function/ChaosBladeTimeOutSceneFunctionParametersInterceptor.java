package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class ChaosBladeTimeOutSceneFunctionParametersInterceptor implements SceneFunctionParametersInterceptor {
    @Override
    public void afterQuery(SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters) {
        if (MiniAppUtils.isFromChaosBlade(sceneFunctionDO.getCode())) {
            parameters.removeIf(parameter -> Objects
                .equals(parameter.getAlias(), ChaosBladeTimeOutSetterActivityInvokeInterceptor.param));
        }
    }

    @Override
    public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {

    }
}

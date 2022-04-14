package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionParameterEncoderAndDecoders;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionMeta;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentActivityDefinitionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

/**
 * 如果是密码类型，需要对密码进行加密
 *
 * @author haibin
 *
 *
 */
@Component
public class ParamValueEncryptionActivityDefinitionInterceptor implements ExperimentActivityDefinitionInterceptor {

    @Autowired
    private SceneFunctionParameterEncoderAndDecoders sceneFunctionParameterEncoderAndDecoders;

    @Override
    public void preInterceptor(ExperimentActivityDefinition originDefinition, PhaseType phaseType,
                               ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta) {
        ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = originDefinition.getArguments();
        experimentNodeArgumentsDefinition.getAllArguments().forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String key, String value) {
                experimentNodeArgumentsDefinition.addArgs(key,
                    sceneFunctionParameterEncoderAndDecoders
                        .encode(originDefinition.getExecutableAppCode(), key,
                            value,
                            experimentNodeArgumentsDefinition.getArgsComponents().get(key)));
            }
        });
    }
}

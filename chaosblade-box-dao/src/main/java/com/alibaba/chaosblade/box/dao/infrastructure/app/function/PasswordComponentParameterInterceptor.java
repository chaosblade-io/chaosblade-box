package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 * 
 *
 */
@Component
public class PasswordComponentParameterInterceptor implements SceneFunctionParameterEncoderAndDecoder {

    @Override
    public String encodeValue(String appCode, String alias, String value,
        SceneFunctionParameterComponent sceneFunctionParameterComponent) throws Exception {
        return SceneParamPasswordHandler.encrypt(value);
    }

    @Override
    public String decodeValue(String appCode, String alias, String value,
        SceneFunctionParameterComponent sceneFunctionParameterComponent) throws Exception {
        return SceneParamPasswordHandler.decrypt(value);
    }

    @Override
    public boolean supportSceneFunctionParameterComponent(
        SceneFunctionParameterComponent sceneFunctionParameterComponent) {
        return SceneFunctionParameterComponent.TYPE_PASSWORD.equals(sceneFunctionParameterComponent.getType());
    }
}

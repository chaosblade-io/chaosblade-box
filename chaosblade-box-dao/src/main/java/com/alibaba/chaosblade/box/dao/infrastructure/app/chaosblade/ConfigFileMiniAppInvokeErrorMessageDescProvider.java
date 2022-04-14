package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.infrastructure.util.ObjectUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.MiniAppInvokeErrorMessageDescProvider;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author haibin
 *
 *
 */
@Component
public class ConfigFileMiniAppInvokeErrorMessageDescProvider implements MiniAppInvokeErrorMessageDescProvider,
        InitializingBean {

    private Map<String, CodeMetaData> codeToMetaData = new HashMap<>();

    private List<ChaosbladeErrorMessageDesc> chaosbladeErrorMessageDescs = new ArrayList<>();

    public static CodeMetaData AGENT_INTERNAL_ERROR = new CodeMetaData();

    static {
        AGENT_INTERNAL_ERROR.setType("AgentInternalError");
        AGENT_INTERNAL_ERROR.setCnDesc("Internal Error");
        AGENT_INTERNAL_ERROR.setCnDesc("故障命令下发错误");
    }

    private static Logger logger = LoggerFactory.getLogger(ChaosBladeResultCodeRepo.class);

    public String getAgentCodePath() {
        return "config/scene/chaos-blade/agent_code_solution.json";
    }

    public String getCommonErrorCodePath() {
        return "miniapp/error_code_metadata.json";
    }

    public String getErrorMessagePath() {
        return "config/scene/chaos-blade/error_message.json";
    }

    @Override
    public ChaosbladeErrorMessageDesc getDescByErrorMessage(MiniAppInvokeContext miniAppInvokeContext,
                                                            Response response) {
        Optional<ChaosbladeErrorMessageDesc> chaosbladeErrorMessageDesc = chaosbladeErrorMessageDescs.stream().filter(
                        chaosbladeErrorMessageDesc1 -> response.getError().contains(chaosbladeErrorMessageDesc1.getPartError()))
                .findFirst();
        return chaosbladeErrorMessageDesc.orElse(null);
    }

    @Override
    public CodeMetaData getMetaDataByChaosBladeCode(Integer code) {
        return codeToMetaData.get(String.valueOf(code));
    }

    @Override
    public CodeMetaData getMetaDataByErrorCode(String errorCode) {
        return codeToMetaData.get(errorCode);
    }

    public void init() {
        try {
            if (getAgentCodePath() != null) {
                Type type = new TypeReference<Map<String, CodeMetaData>>() {
                }.getType();
                codeToMetaData = ObjectUtil.parseResourceObject(getAgentCodePath(), type);
            }
        } catch (Throwable throwable) {
            logger.error("load chaos-blade error message failed", throwable);
        }
        try {
            if (getErrorMessagePath() != null) {
                Type type = new TypeReference<List<ChaosbladeErrorMessageDesc>>() {
                }.getType();
                chaosbladeErrorMessageDescs = ObjectUtil.parseResourceObject(getErrorMessagePath(), type);
                logger.info("load chaos-blade error-message file success,content size:" + codeToMetaData.size());
            }
        } catch (Throwable throwable) {
            logger.error("load chaos-blade error message failed", throwable);
        }

        try {
            Type type = new TypeReference<Map<String, CodeMetaData>>() {
            }.getType();
            codeToMetaData.putAll(ObjectUtil.parseResourceObject(getCommonErrorCodePath(), type));
        } catch (Throwable throwable) {
            logger.error("load common-errorcode error message failed", throwable);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}

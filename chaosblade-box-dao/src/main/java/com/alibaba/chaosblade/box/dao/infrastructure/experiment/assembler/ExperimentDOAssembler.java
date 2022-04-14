package com.alibaba.chaosblade.box.dao.infrastructure.experiment.assembler;

import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosIdGenerator;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.fastjson.JSON;

/**
 * @author haibin
 *
 * 
 */
public class ExperimentDOAssembler {

    public static ExperimentDO assembleExperimentDO(BaseExperimentRequest baseExperimentRequest) {
        ExperimentDO experimentDO = new ExperimentDO();
        //experimentDO.setExperimentId(IdWorker.getIdStr());
        experimentDO.setName(baseExperimentRequest.getName());
        experimentDO.setDescription(baseExperimentRequest.getDescription());
        experimentDO.setExperimentId(ChaosIdGenerator.generateId());
        experimentDO.setNamespace(baseExperimentRequest.getNamespace());
        experimentDO.setMiniAppDesc(JSON.toJSONString(baseExperimentRequest.getMiniAppDesc()));

        ChaosUser user = baseExperimentRequest.getUser();
        if (null != user) {
            experimentDO.setUserId(user.getUserId());
        }

        return experimentDO;
    }

}

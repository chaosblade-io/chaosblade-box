package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;


import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.MiniAppInvokeFlowThreadLocalContext;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
@Component
public class ActivityAsyncCheckCommand extends SpringBeanCommand<AsyncCallBackContext, Boolean> {

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Autowired
    private List<ActivityAsyncCallback> activityAsyncCallbackList;

    @Override
    public Boolean execute(AsyncCallBackContext asyncCallBackContext) {
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = asyncCallBackContext.getExperimentMiniAppTaskDO();
        ChaosBladeExpUidDO chaosBladeExpUidDO = getChaosBladeExpUidDO(asyncCallBackContext, experimentMiniAppTaskDO);
        if (chaosBladeExpUidDO == null) {return false;}
        String appCode = chaosBladeExpUidDO.getAppCode();
        doCallback(asyncCallBackContext, appCode);
        return null;
    }

    private void doCallback(AsyncCallBackContext asyncCallBackContext, String appCode) {
        MiniAppInvokeFlowThreadLocalContext.startMiniAppInvoke(asyncCallBackContext.getExperimentMiniAppTaskDO(), true);
        try {
            RecordsRepo.getMiniAppInvocationRecorder().log(asyncCallBackContext.getExperimentMiniAppTaskDO(),
                "Start handle async callback",true);
            activityAsyncCallbackList.forEach(activityAsyncCallback -> {
                if (activityAsyncCallback.supportByAppCode(appCode)) {
                    activityAsyncCallback.execute(asyncCallBackContext);
                }
            });
        } finally {
            RecordsRepo.getMiniAppInvocationRecorder().log(asyncCallBackContext.getExperimentMiniAppTaskDO(),
                "End handle async callback",true);
            MiniAppInvokeFlowThreadLocalContext.endMiniAppInvoke();
        }
    }

    private ChaosBladeExpUidDO getChaosBladeExpUidDO(AsyncCallBackContext asyncCallBackContext,
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = asyncCallBackContext.getChaosBladeExpUidDO();
        if (null == chaosBladeExpUidDO) {
            chaosBladeExpUidDO = chaosBladeExpUidRepository.findByActivityTargetTaskId(
                experimentMiniAppTaskDO.getTaskId());
            asyncCallBackContext.setChaosBladeExpUidDO(chaosBladeExpUidDO);
        }
        return chaosBladeExpUidDO;
    }

}

package com.alibaba.chaosblade.box.service.command.guard;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosAppInvoker;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface GuardExecutableInterface {

    /**
     * 调用metric的小程序,一般都是以chaosapp.metric.xxx开头
     *
     * @param experimentTaskDO          演练任务
     * @param appCode                   小程序code
     * @param experimentGuardInstanceDO 守护实例
     * @param arguments                 变量
     * @param from                      开始时间
     * @param to                        截止时间
     * @param hosts                     机器列表
     * @return 返回每台机器在区间内的监控指标
     */
    default Map<Scope, List<ChaosMetricEntity>> invokeMetricMiniApp(ExperimentTaskDO experimentTaskDO, String appCode,
                                                                 ExperimentGuardInstanceDO experimentGuardInstanceDO,
                                                                 List<SceneArgumentDefinition> arguments, Date from, Date to, List<Host> hosts) {
        //获取到所有的host
        ChaosAppRequest chaosAppRequest = new ChaosAppRequest();
        Map<String, String> userArgs = new HashMap<>();
        for (SceneArgumentDefinition sceneArgumentDefinition : arguments) {
            userArgs.put(sceneArgumentDefinition.getAlias(), sceneArgumentDefinition.getValue());
        }
        userArgs.put("from", String.valueOf(from.getTime()));
        userArgs.put("to", String.valueOf(to.getTime()));
        chaosAppRequest.setUserArgs(userArgs);
        Map<Scope, List<ChaosMetricEntity>> stringListMap = new HashMap<>();
        for (Scope host : hosts) {
            ChaosAppContext chaosAppContext = new ChaosAppContext();
            chaosAppContext.setUserId(experimentTaskDO.getUserId());
            chaosAppContext.setNamespace(experimentTaskDO.getNamespace());
            chaosAppRequest.setScope(host);
            ChaosAppResponse chaosAppResponse = internalInvoke(chaosAppContext, chaosAppRequest, appCode,
                experimentGuardInstanceDO);
            if (chaosAppResponse != null && chaosAppResponse.isSuccess()) {
                List<ChaosMetricEntity> chaosMetricEntities = (List<ChaosMetricEntity>)chaosAppResponse.getData().get("response");
                stringListMap.put(host, chaosMetricEntities);
            }
        }
        return stringListMap;
    }

    default ChaosAppResponse internalInvoke(ChaosAppContext chaosAppContext, ChaosAppRequest chaosAppRequest, String appCode,
                                         ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        return ChaosAppInvoker.invokeByGuard(chaosAppContext, chaosAppRequest, appCode, experimentGuardInstanceDO);
    }
}

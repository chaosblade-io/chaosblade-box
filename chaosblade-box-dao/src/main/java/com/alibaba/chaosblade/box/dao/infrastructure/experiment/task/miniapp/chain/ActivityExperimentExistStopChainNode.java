package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeDestroyExpRequest;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeHostQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.sdk.entity.ExpStatusBean;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author haibin
 *
 *
 */
@Component
public class ActivityExperimentExistStopChainNode extends ActivityTargetRetryInvokeChainNode {

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    @Override
    protected boolean internalExecute(ActivityTargetRetryInvokeContext activityTargetRetryInvokeContext) {
        ChaosAppResponse preResponse = activityTargetRetryInvokeContext.getPreAppResponse();
        MiniAppInvokeContext miniAppInvokeContext = activityTargetRetryInvokeContext.getMiniAppInvokeContext();
        if (!MiniAppUtils.isFromChaosBlade(miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode())) {
            return false;
        }
        if (MiniAppUtils.isJvmAgentInstall(miniAppInvokeContext.getActivityInvokeContext().getExecutableAppCode())) {
            return false;
        }
        ChaosBladeAppResponse chaosBladeAppResponse = (ChaosBladeAppResponse) preResponse;
        if (chaosBladeAppResponse.getChaosBladeResponse().getCode() == ChaosBladeMetaData.AGENT_DUMPLICATE_CODE) {
            ChaosBladeAppResponse newChaosBladeAppResponse = doRetry(activityTargetRetryInvokeContext);
            if (newChaosBladeAppResponse != null) {
                activityTargetRetryInvokeContext.setCurAppResponse(newChaosBladeAppResponse);
            }
        }
        return false;
    }

    protected ChaosBladeAppResponse doRetry(ActivityTargetRetryInvokeContext activityTargetRetryInvokeContext) {
        //首先卸载掉原来的
        revoke(activityTargetRetryInvokeContext.getMiniAppInvokeContext().getHost(),
                activityTargetRetryInvokeContext.getMiniAppInvokeContext().getActivityInvokeContext()
                        .getExecutableAppCode());
        //然后再次安装
        return (ChaosBladeAppResponse) activityTargetRetryInvokeContext.getInvokeReference()
                .invoke(
                        activityTargetRetryInvokeContext.getMiniAppInvokeContext()).getChaosAppResponse();
    }

    public void revoke(Host host, String appCode) {
        Response<List<ExpStatusBean>> response = chaosBladeInvoker.getExpStatusList(
                new ChaosBladeHostQueryRequest(host));
        Collections.reverse(response.getResult());
        response.getResult().stream().filter(new Predicate<ExpStatusBean>() {
            @Override
            public boolean test(ExpStatusBean expStatusBean) {
                return ChaosBladeInvoker.STATUS_RUNNING.equalsIgnoreCase(expStatusBean.getStatus());
            }
        }).forEach(new Consumer<ExpStatusBean>() {
            @Override
            public void accept(ExpStatusBean expStatusBean) {
                chaosBladeInvoker.destroyExp(new ChaosBladeDestroyExpRequest(host, expStatusBean.getUid(), appCode));
            }
        });
    }
}

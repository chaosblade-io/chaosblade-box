package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.common.infrastructure.OnceInvokeReference;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain.ActivityTargetRetryInvokeChainNode;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain.ActivityTargetRetryInvokeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 真实执行远程调用
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class MiniAppRemoteInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
        return true;
    }

    @Override
    public void postHandle(OnceInvoke<MiniAppInvokeContext, ChaosAppResponseReference> onceInvoke,
                           MiniAppInvokeContext miniAppInvokeContext,
                           ChaosAppResponseReference chaosAppResponseReference) {
        new ChaosAppRunner(onceInvoke, miniAppInvokeContext, chaosAppResponseReference).run();
    }

    private class ChaosAppRunner implements Runnable {

        private OnceInvoke<MiniAppInvokeContext, ChaosAppResponseReference> onceInvoke;

        private MiniAppInvokeContext miniAppInvokeContext;

        private ChaosAppResponseReference chaosAppResponseReference;

        public ChaosAppRunner(OnceInvoke<MiniAppInvokeContext, ChaosAppResponseReference> onceInvoke,
                           MiniAppInvokeContext miniAppInvokeContext,
                           ChaosAppResponseReference chaosAppResponseReference) {
            this.onceInvoke = onceInvoke;
            this.miniAppInvokeContext = miniAppInvokeContext;
            this.chaosAppResponseReference = chaosAppResponseReference;
        }

        @Override
        public void run() {
            ChaosAppResponse result = null;
            try {
                ChaosAppResponseReference responseReference = onceInvoke.invoke(miniAppInvokeContext);
                result = responseReference.getChaosAppResponse();
                if (!result.isSuccess()) {
                    ActivityTargetRetryInvokeChainNode chainNode = ChainFactory.createSpringChain(applicationContext,
                            ActivityTargetRetryInvokeChainNode.class);
                    ActivityTargetRetryInvokeContext activityTargetRetryInvokeContext
                            = new ActivityTargetRetryInvokeContext(
                            new OnceInvokeReference<>(onceInvoke), miniAppInvokeContext, result);
                    try {
                        chainNode.invoke(activityTargetRetryInvokeContext);
                        result = activityTargetRetryInvokeContext.getCurAppResponse();
                    } catch (Exception e) {
                        log.error("chain invoke failed", e);
                    }
                }
            } catch (Throwable exception) {
                log.error("chain invoke failed", exception);
                result = new ChaosAppResponse(false, null, exception.getMessage(), miniAppInvokeContext.getHost());
            }
            chaosAppResponseReference.setChaosAppResponse(result);
        }
    }

    @Override
    public void afterHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse,
                            Throwable throwable) {

    }

    @Override
    public Integer getOrder() {
        return Integer.MIN_VALUE;
    }
}

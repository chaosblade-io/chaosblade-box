package com.alibaba.chaosblade.box.dao.infrastructure.app;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;

/**
 *
 * @author sunju
 */
public interface AppCallback {

    /**
     * invoke after agent attached succeed if agent required
     *
     * @param scope target device which agent attached
     * @param phase phase
     * @param response result of agent attached, include some response data from agent
     *
     */
    void agentAttached(Scope scope, PhaseType phase, ChaosAppResponse response);

    /**
     * invoke after agent attached failed if agent required
     *
     * @param scope target device which agent attached
     * @param phase phase
     * @param throwable error
     *
     */
    void agentAttachFailed(Scope scope, PhaseType phase, Throwable throwable);

    /**
     * invoke after chaosapp running finished
     *
     * @param scope target device which chaosapp running
     * @param phase phase
     * @param response result of chaosapp executing
     */
    void completed(Scope scope, PhaseType phase, ChaosAppResponse response);

    /**
     * invoke after chaosapp running failed
     *
     * @param scope target device which chaosapp running
     * @param phase phase
     * @param throwable error
     */
    void failed(Scope scope, PhaseType phase, Throwable throwable);
}

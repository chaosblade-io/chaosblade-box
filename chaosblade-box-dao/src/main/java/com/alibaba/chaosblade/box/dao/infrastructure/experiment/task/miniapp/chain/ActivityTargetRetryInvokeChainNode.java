package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp.chain;

import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainNode;

/**
 * activity target实际运行
 *
 * @author haibin
 *
 *
 */
public abstract class ActivityTargetRetryInvokeChainNode extends ChainNode<Boolean, ActivityTargetRetryInvokeContext> {

    @Override
    public Boolean invoke(ActivityTargetRetryInvokeContext context) throws Exception {
        if (context.getInvokeReference().isInvoked()) { return false; }
        return internalExecute(context);
    }

    /**
     * 执行node
     *
     * @param activityTargetRetryInvokeContext
     * @return
     */
    protected abstract boolean internalExecute(ActivityTargetRetryInvokeContext activityTargetRetryInvokeContext);

}

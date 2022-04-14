package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author haibin
 *
 *
 */
public class OnceInvokeReference<Re, Rs> implements OnceInvoke<Re, Rs> {

    private AtomicBoolean invoked = new AtomicBoolean(false);

    private OnceInvoke<Re, Rs> onceInvoke;

    public OnceInvokeReference(OnceInvoke<Re, Rs> onceInvoke) {
        this.onceInvoke = onceInvoke;
    }

    public boolean isInvoked() {
        return this.invoked.get();
    }

    @Override
    public Rs invoke(Re re) {
        if (this.invoked.get()) {
            throw new IllegalStateException("the method only invoke once");
        }
        this.invoked.set(true);
        return this.onceInvoke.invoke(re);
    }
}

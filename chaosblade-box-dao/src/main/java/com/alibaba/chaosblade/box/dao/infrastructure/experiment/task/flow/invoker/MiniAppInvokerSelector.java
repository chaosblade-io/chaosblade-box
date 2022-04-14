package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author haibin
 *
 * 
 */
public class MiniAppInvokerSelector {

    private static MiniAppInvoker Default = new DefaultChaosAppInvoker();

    private List<MiniAppInvoker> miniAppInvokers;

    public MiniAppInvokerSelector(
        List<MiniAppInvoker> miniAppInvokers) {
        this.miniAppInvokers = Objects.requireNonNull(miniAppInvokers);
    }

    public MiniAppInvoker select(MiniAppInvokeContext miniAppInvokeContext) {
        return this.miniAppInvokers.stream().filter(new Predicate<MiniAppInvoker>() {
            @Override
            public boolean test(MiniAppInvoker miniAppInvoker) {
                return miniAppInvoker.support(miniAppInvokeContext);
            }
            
        }).findFirst().orElse(Default);
    }
}

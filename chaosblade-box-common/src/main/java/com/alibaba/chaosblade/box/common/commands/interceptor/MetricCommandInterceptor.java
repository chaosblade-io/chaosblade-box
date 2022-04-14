package com.alibaba.chaosblade.box.common.commands.interceptor;

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.InvocationCommand;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricCommandInterceptor implements CommandInterceptor {

    @Override
    public void onStarted(Command<?> command) {
    }

    @Override
    public void onReturn(InvocationCommand command, Object result) {
        //ChaosApplicationMetric.getCommandInvocationCompass().record(command.getCost(TimeUnit.MILLISECONDS),
        //    ChaosApplicationMetric.SUCCESS);
    }

    @Override
    public void onError(InvocationCommand command, Throwable throwable) {
        //ChaosApplicationMetric.getCommandInvocationCompass().record(command.getCost(TimeUnit.MILLISECONDS),
        //    ChaosApplicationMetric.ERROR);
    }
}

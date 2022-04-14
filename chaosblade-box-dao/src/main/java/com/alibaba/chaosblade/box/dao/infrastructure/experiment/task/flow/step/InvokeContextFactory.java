package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step;

/**
 * @author haibin
 *
 *
 */
public class InvokeContextFactory {

    public static <T extends InvokeContext> T create(StepExecuteContext stepExecuteContext, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            t.setStepExecuteContext(stepExecuteContext);
            return t;
        } catch (Throwable e) {
            throw new RuntimeException(
                "create invokeContext failed,must have no argument Constructor context class:" + tClass);
        }
    }
}

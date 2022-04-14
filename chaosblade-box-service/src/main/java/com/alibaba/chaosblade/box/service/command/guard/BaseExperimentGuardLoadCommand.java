package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentGuardInstanceMapper;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Slf4j
public abstract class BaseExperimentGuardLoadCommand<T> extends SpringBeanCommand<ExperimentGuardResultLoadRequest, T> {

    @Autowired
    protected ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Autowired
    protected ExperimentGuardInstanceMapper experimentGuardInstanceMapper;

    @Override
    public T execute(ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
        try {
            return internalExecute(experimentGuardResultLoadRequest);
        } catch (Throwable throwable) {
            log.error("handle guard instance failed,experiment taskId:{},guard instanceId:{}",
                experimentGuardResultLoadRequest.getExperimentTaskDO().getTaskId(),
                experimentGuardResultLoadRequest.getExperimentGuardInstanceDO().getInstanceId(), throwable);
        }
        return null;
    }

    /**
     * 内部执行流程
     *
     * @param experimentGuardResultLoadRequest
     * @return
     */
    protected abstract T internalExecute(ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest);

    /**
     * 计算时间区间
     *
     * @param offsetInSeconds 时间偏移量
     * @return
     */
    protected static Pair<Date, Date> calcTimeRange(int offsetInSeconds) {
        Calendar calendar = Calendar.getInstance();
        Date endTime = calendar.getTime();
        calendar.add(Calendar.SECOND, -offsetInSeconds);
        Date startTime = calendar.getTime();
        return Pair.of(startTime, endTime);
    }

}

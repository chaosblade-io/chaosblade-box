package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.experiment.request.UserExperimentStatRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentStatCommand extends SpringBeanCommand<UserExperimentStatRequest, ExperimentStat> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Override
    public ExperimentStat execute(UserExperimentStatRequest userExperimentStatRequest) {
        ExperimentQuery experimentQuery = new ExperimentQuery();
        experimentQuery.setNamespace(userExperimentStatRequest.getNamespace());
        experimentQuery.setUser(userExperimentStatRequest.getUser());

        List<ExperimentDO> experiments = experimentRepository.find(experimentQuery);

        ExperimentStat experimentStat = new ExperimentStat();
        experimentStat.setTotal(experiments.size());

        long active = 0L, running = 0L, failed = 0L, success = 0L, idle = 0L, finishedCount = 0L;

        for (ExperimentDO experiment : experiments) {
            if (experiment.getExperimentTaskId() == null) {
                idle++;
            } else {
                active++;
                ExperimentStateEnum experimentStateEnum = experiment.getExperimentStateEnum();
                if (ExperimentStateEnum.RUNNING.equals(experimentStateEnum)) {
                    running++;
                } else {
                    finishedCount++;
                    if (experiment.isSuccess()) {
                        success++;
                    } else {
                        failed++;
                    }
                }
            }
        }

        experimentStat.setIdle(idle);
        experimentStat.setFinished(finishedCount);
        experimentStat.setRunning(running);
        experimentStat.setActive(active);
        experimentStat.setFailure(failed);
        experimentStat.setSuccess(success);
        //老数据
        experimentStat.setTotalCount(experiments.size());
        experimentStat.setFailureCount(experimentStat.getFailure());
        experimentStat.setFinished(experimentStat.getFinishedCount());
        experimentStat.setRunningCount(experimentStat.getRunningCount());
        return experimentStat;
    }

}

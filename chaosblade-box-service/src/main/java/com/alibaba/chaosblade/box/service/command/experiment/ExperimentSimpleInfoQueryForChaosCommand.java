package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowSimpleInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentSimpleInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityQueryManager;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentToBasicInfoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Component
public class ExperimentSimpleInfoQueryForChaosCommand extends SpringBeanCommand<String, Response<ExperimentSimpleInfo>> {

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private ExperimentToBasicInfoConverter experimentToBasicInfoConverter;

    @Autowired
    private ActivityQueryManager activityQueryManager;

    @Override
    public Response<ExperimentSimpleInfo> execute(String experimentId) {

        ExperimentSimpleInfo experimentSimpleInfo = new ExperimentSimpleInfo();
        experimentSimpleInfo.setExperimentId(experimentId);
        Response<ExperimentDO> experimentDOResponse = experimentChecker.assertExperimentExist(experimentId);
        if (!experimentDOResponse.isSuccess()) {
            return Response.failedWith(experimentDOResponse.getError());
        }
        ExperimentDO experimentDO = experimentDOResponse.getResult();
        ExperimentBasicInfo experimentBasicInfo = experimentToBasicInfoConverter.convert(experimentDO);
        experimentSimpleInfo.setBasicInfo(experimentBasicInfo);

        if (experimentDO.isDraft()) {
            return Response.okWithData(experimentSimpleInfo);
        }
        List<ExperimentActivityDO> experimentActivityDOS = activityQueryManager.findActivitiesByExperimentId(experimentId);
        dealActivityDO(experimentSimpleInfo, experimentActivityDOS);


        return Response.okWithData(experimentSimpleInfo);
    }

    private void dealActivityDO(ExperimentSimpleInfo experimentSimpleInfo, List<ExperimentActivityDO> experimentActivityDOS) {
        List<ExperimentFlowSimpleInfo> prepare = new ArrayList<>();
        List<ExperimentFlowSimpleInfo> attack = new ArrayList<>();
        List<ExperimentFlowSimpleInfo> check = new ArrayList<>();
        List<ExperimentFlowSimpleInfo> recover = new ArrayList<>();

        for (ExperimentActivityDO experimentActivityDO : experimentActivityDOS) {

            switch (experimentActivityDO.getPhase()) {
                case PREPARE:
                    prepare.add(new ExperimentFlowSimpleInfo(experimentActivityDO.getAppCode(),experimentActivityDO.getActivityName()));
                    break;
                case ATTACK:
                    attack.add(new ExperimentFlowSimpleInfo(experimentActivityDO.getAppCode(),experimentActivityDO.getActivityName()));
                    break;
                case CHECK:
                    check.add(new ExperimentFlowSimpleInfo(experimentActivityDO.getAppCode(),experimentActivityDO.getActivityName()));
                    break;
                case RECOVER:
                    recover.add(new ExperimentFlowSimpleInfo(experimentActivityDO.getAppCode(),experimentActivityDO.getActivityName()));
                    break;
            }
        }
        experimentSimpleInfo.setPrepare(prepare);
        experimentSimpleInfo.setAttack(attack);
        experimentSimpleInfo.setCheck(check);
        experimentSimpleInfo.setRecover(recover);
    }


}

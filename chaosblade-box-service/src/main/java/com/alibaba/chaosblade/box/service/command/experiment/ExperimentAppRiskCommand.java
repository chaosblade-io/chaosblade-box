package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityQueryManager;
import com.alibaba.chaosblade.box.common.experiment.risk.AppRiskMessage;
import com.alibaba.chaosblade.box.common.experiment.risk.AppRiskMessageLoadFactory;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentAppRisk;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sunpeng
 *
 *
 */
@Slf4j
@Component
public class ExperimentAppRiskCommand extends SpringBeanCommand<String, Response<List<ExperimentAppRisk>>> {

    @Autowired
    private ActivityQueryManager activityQueryManager;

    @Autowired
    private AppRiskMessageLoadFactory appRiskMessageLoadFactory;

    @Override
    public Response<List<ExperimentAppRisk>> execute(String experimentId) {
        //查询演练内包含的小程序
        List<ExperimentActivityDO> experimentActivities = activityQueryManager.findActivitiesByExperimentId(experimentId);
        List<ExperimentAppRisk> result = experimentActivities.stream().map(experimentActivityDO -> {
            AppRiskMessage appRiskMessage = appRiskMessageLoadFactory.getAppRiskMessage(experimentActivityDO.getAppCode());
            if(null != appRiskMessage) {
                ExperimentAppRisk experimentAppRisk = new ExperimentAppRisk();
                experimentAppRisk.setAppCode(experimentActivityDO.getAppCode());
                experimentAppRisk.setAppName(experimentActivityDO.getActivityName());
                experimentAppRisk.setMessage(appRiskMessage.getMessage());
                return experimentAppRisk;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //获取小程序风险信息
        return Response.okWithData(result);
    }
}

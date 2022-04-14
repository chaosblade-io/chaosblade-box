package com.alibaba.chaosblade.box.dao.infrastructure.manager;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.activity.MiniAppTask;
import com.alibaba.chaosblade.box.common.common.domain.activity.MiniAppTaskSummary;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.google.common.base.Strings;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface MiniAppTaskManager {

    /**
     * 初始化小程序执行记录
     *
     * @param activityTaskDO
     * @param host
     * @return
     */
    default ExperimentMiniAppTaskDO initExperimentMiniAppTaskDO(ActivityTaskDO activityTaskDO, Host host) {
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = new ExperimentMiniAppTaskDO();
        experimentMiniAppTaskDO.setUserId(activityTaskDO.getUserId());
        experimentMiniAppTaskDO.setActivityTaskId(activityTaskDO.getTaskId());
        experimentMiniAppTaskDO.setAppCode(activityTaskDO.getAppCode());
        experimentMiniAppTaskDO.setExecutableAppCode(activityTaskDO.getExecutableAppCode());
        experimentMiniAppTaskDO.setActivityTaskId(activityTaskDO.getTaskId());
        experimentMiniAppTaskDO.setState(StateEnum.READY.getValue());
        experimentMiniAppTaskDO.setPhase(activityTaskDO.getPhase());
        experimentMiniAppTaskDO.setExperimentTaskId(activityTaskDO.getExperimentTaskId());
        //如果是异步下发，需要标记一下
        if (null != activityTaskDO.getRunParam() && null != activityTaskDO.getRunParam().getArguments()) {
            String async = activityTaskDO.getRunParam().getArguments().getAllArguments().get("async");
            experimentMiniAppTaskDO.setAsync(!Strings.isNullOrEmpty(async) && Boolean.parseBoolean(async));
        }
        if (host != null) {
            experimentMiniAppTaskDO.setHostIp(host.getIp());
            experimentMiniAppTaskDO.setNodegroup(host.getNodeGroup());
            experimentMiniAppTaskDO.setDeviceId(host.getDeviceId());
            experimentMiniAppTaskDO.setDeviceConfigurationId(host.getDeviceConfigurationId());
            experimentMiniAppTaskDO.setAppConfigurationId(
                host.getAppConfigurationId() == null ? host.getDeviceConfigurationId() : host
                    .getAppConfigurationId());
            experimentMiniAppTaskDO.setDeviceName(host.getDeviceName());
        }
        return experimentMiniAppTaskDO;
    }

    /**
     * 查找小程序任务，找不到的话就创建新的任务
     *
     * @param activityTaskDO
     * @param host
     * @return
     */
    public ExperimentMiniAppTaskDO findOrCreateMiniAppTaskIfNotExist(ActivityTaskDO activityTaskDO,
                                                                     Host host);

    /**
     * 放弃小程序任务的执行
     *
     * @param activityTaskId activityTaskId
     * @return
     */
    public long rejectMiniAppTasksByActivityTaskId(String activityTaskId, String rejectReason);

    /**
     * @param activityTaskId
     * @return
     */
    public List<MiniAppTaskSummary> findMiniAppTasksByActivityTaskId(String activityTaskId);

    public MiniAppTask findMiniAppTask(String miniAppTaskId);

    /**
     * 重置节点的运行记录
     *
     * @param activityTaskId
     */
    public void resetMiniAppTasksByActivityTask(String activityTaskId);

}

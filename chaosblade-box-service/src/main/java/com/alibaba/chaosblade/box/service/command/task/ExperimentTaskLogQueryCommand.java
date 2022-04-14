package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.commands.BasePoolCommand;
import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.experiment.PhaseInfo;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskLog;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author haibin
 *
 *
 */
public class ExperimentTaskLogQueryCommand extends BasePoolCommand<Response<ExperimentTaskLog>> {

    private String experimentTaskId;

    public ExperimentTaskLogQueryCommand(String experimentTaskId) {
        this.experimentTaskId = experimentTaskId;
    }

    @Override
    public String getCommandExecutorName() {
        return CommandExecutorConstant.EXECUTOR_DEFAULT;
    }

    @Override
    public Response<ExperimentTaskLog> execute() {
        ExperimentTaskLog experimentTaskLog = new ExperimentTaskLog();
        Response<ExperimentTask> experimentTaskResponse = getPool().syncRun(ExperimentTaskQueryCommand.class,
            new ExperimentTaskQueryRequest(experimentTaskId));
        if (!experimentTaskResponse.isSuccess()) { return Response.failedWith(experimentTaskResponse.getError());}
        ExperimentTask experimentTask = experimentTaskResponse.getResult();
        experimentTaskLog.setExperimentId(experimentTask.getExperimentId());
        experimentTaskLog.setExperimentName(experimentTask.getExperimentName());
        experimentTaskLog.setTaskId(experimentTask.getTaskId());
        experimentTaskLog.setStartTime(experimentTask.getStartTime());
        experimentTaskLog.setEndTime(experimentTask.getEndTime());
        experimentTaskLog.setResult(experimentTask.getResult());
        experimentTaskLog.setAoneApps(experimentTask.getAoneApps());

        ChaosUser user = experimentTask.getCreator();
        String userName = user.getUserId();
        if (null != user.getUserName()) {
            userName = user.getUserName();
        }
        experimentTaskLog.setUserName(userName);

        Map<Long, String> logs = new TreeMap<>();

        List<PhaseInfo> phaseInfos = experimentTask.getPhases();

        logs.put(experimentTaskLog.getStartTime().getTime(), "开始演练");
        Long endTime = experimentTaskLog.getEndTime().getTime();
        for (PhaseInfo phaseInfo : phaseInfos) {
            List<ActivityTask> activityTasks = phaseInfo.getActivities();
            for (ActivityTask activityTask : activityTasks) {
                String logStr = activityTask.getMiniAppName();
                if (ResultEnum.SUCCESS.equals(activityTask.getRunResult())) {
                    logStr += "成功";
                } else if (ResultEnum.FAILED.equals(activityTask.getRunResult())) {
                    logStr += "失败";
                } else if (ResultEnum.REJECTED.equals(activityTask.getRunResult())) {
                    logStr += "跳过";
                } else if (ResultEnum.ERROR.equals(activityTask.getRunResult())) {
                    logStr += "异常中断";
                } else if (ResultEnum.STOPPED.equals(activityTask.getRunResult())) {
                    logStr += "中断";
                } else {
                    logStr += "停止失败";
                }
                if (logs.containsKey(activityTask.getStartTime().getTime())) {
                    int cnt = 0;
                    while (logs.containsKey(cnt + activityTask.getStartTime().getTime())) {
                        cnt++;
                    }
                    logs.put(activityTask.getStartTime().getTime() + cnt, logStr);
                    if (activityTask.getStartTime().getTime() + cnt > endTime) {
                        endTime = activityTask.getStartTime().getTime() + cnt;
                    }
                } else {
                    logs.put(activityTask.getStartTime().getTime(), logStr);
                    if (activityTask.getStartTime().getTime() > endTime) {
                        endTime = activityTask.getStartTime().getTime();
                    }
                }
            }
        }

        if (logs.containsKey(endTime)) {
            logs.put(endTime + 1, "结束演练");
        } else {
            logs.put(endTime, "结束演练");
        }

        experimentTaskLog.setLogs(logs);
        return Response.okWithData(experimentTaskLog);
    }
}

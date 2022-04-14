package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.service.UserService;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentAppRiskCommand;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Converter;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 * 
 *
 */
@Component
public class ExperimentDOTOUserExperimentConverter extends Converter<ExperimentDO, UserExperiment> {

    @Resource
    private TagManager tagManager;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private UserService userService;

    @Override
    protected UserExperiment doForward(ExperimentDO experimentDO) {
        UserExperiment userExperiment = new UserExperiment();
        userExperiment.setExperimentId(experimentDO.getExperimentId());
        userExperiment.setCreateTime(experimentDO.getGmtCreate());
        userExperiment.setName(experimentDO.getName());
        userExperiment.setState(experimentDO.getExperimentStateEnum());
        userExperiment.setSchedulerConfig(experimentDO.getSchedulerConfig());
        userExperiment.setTags(
                tagManager.findTagsByExperimentId(experimentDO.getExperimentId())
                        .stream()
                        .map(TagDO::getName)
                        .collect(Collectors.toList())
        );
        userExperiment.setMiniAppDesc(
                Strings.isNullOrEmpty(experimentDO.getMiniAppDesc())
                        ? null
                        : JSON.parseObject(experimentDO.getMiniAppDesc(), new TypeReference<ArrayList<String>>() {
                })
        );
        if (experimentDO.getExperimentTaskId() != null) {
            setTaskState(userExperiment, experimentDO.getExperimentTaskId());
        }
        userExperiment.setCreator(experimentDO.getUserId());
        userExperiment.setExperimentAppRisks(commandBus.syncRun(ExperimentAppRiskCommand.class,
                experimentDO.getExperimentId()).getResult());
        userExperiment.setPermission(PermissionResult.ALL.getPermission());
        return userExperiment;
    }

    @Override
    protected ExperimentDO doBackward(UserExperiment userExperiment) {
        return null;
    }


    private void setTaskState(UserExperiment userExperiment, String taskId) {
        experimentTaskRepository.findById(taskId).ifPresent(experimentTaskDO -> {
            ExperimentTask experimentTask = new ExperimentTask();
            experimentTask.setTaskId(experimentTaskDO.getTaskId());
            experimentTask.setCreator(userService.getUserByUserId(experimentTaskDO.getUserId()));
            experimentTask.setResult(experimentTaskDO.getResultEnum());
            experimentTask.setState(experimentTaskDO.getStateEnum());
            experimentTask.setStartTime(experimentTaskDO.getGmtCreate());
            userExperiment.setTask(experimentTask);
        });
    }
}

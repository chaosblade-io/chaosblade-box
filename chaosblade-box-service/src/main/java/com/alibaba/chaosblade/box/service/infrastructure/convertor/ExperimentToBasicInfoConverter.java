package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.service.WorkspaceService;
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
public class ExperimentToBasicInfoConverter extends Converter<ExperimentDO, ExperimentBasicInfo> {

    @Autowired
    private TagManager tagManager;

//    @Autowired
//    private WorkspaceService workspaceService;
    @Resource
    private WorkspaceService workspaceService;

    @Override
    protected ExperimentBasicInfo doForward(ExperimentDO experimentDO) {
        String experimentId = experimentDO.getExperimentId();
        ExperimentBasicInfo experimentBasicInfo = new ExperimentBasicInfo();
        experimentBasicInfo.setDescription(experimentDO.getDescription());
        experimentBasicInfo.setExperimentId(experimentId);
        experimentBasicInfo.setName(experimentDO.getName());
        experimentBasicInfo.setOwnerUserId(experimentDO.getOwnerUserId());
        experimentBasicInfo.setGmtCreate(experimentDO.getGmtCreate());
        experimentBasicInfo.setGmtModified(experimentDO.getGmtModified());
        experimentBasicInfo.setSchedulerConfig(experimentDO.getSchedulerConfig());
        experimentBasicInfo.setNamespace(experimentDO.getNamespace());
//        experimentBasicInfo.setRegionId(experimentDO.getRegionId());
        experimentBasicInfo.setState(experimentDO.getExperimentStateEnum());
        experimentBasicInfo.setExperimentTaskId(experimentDO.getExperimentTaskId());
        experimentBasicInfo.setLevel(experimentDO.getLevel());
        experimentBasicInfo.setSource(experimentDO.getSource());
        experimentBasicInfo.setMiniAppDesc(Strings.isNullOrEmpty(experimentDO.getMiniAppDesc())
                ? null
                : JSON.parseObject(experimentDO.getMiniAppDesc(), new TypeReference<ArrayList<String>>() {
        }));
        experimentBasicInfo.setTags(
                tagManager.findTagsByExperimentId(experimentDO.getExperimentId())
                        .stream()
                        .map(TagDO::getName)
                        .collect(Collectors.toList())
        );
        experimentBasicInfo.setWorkspaces(
            workspaceService.getWorkspacesShortInfoByExperimentId(experimentDO.getExperimentId()));
        return experimentBasicInfo;
    }

    @Override
    protected ExperimentDO doBackward(ExperimentBasicInfo experimentBasicInfo) {
        throw new UnsupportedOperationException("Not support yet");
    }
}

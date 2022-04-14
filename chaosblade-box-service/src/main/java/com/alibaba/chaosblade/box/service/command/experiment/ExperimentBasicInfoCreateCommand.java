package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentBaseInfoRequest;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.assembler.ExperimentDOAssembler;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class ExperimentBasicInfoCreateCommand extends SpringBeanCommand<ExperimentBaseInfoRequest, Response<String>> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private TagManager tagManager;

    @Override
    public Response<String> execute(ExperimentBaseInfoRequest experimentBasicInfoRequest) {
        ExperimentDO experimentDO = ExperimentDOAssembler.assembleExperimentDO(experimentBasicInfoRequest);
        experimentDO.setOuterId(experimentBasicInfoRequest.getOuterId());
        experimentDO.setState(ExperimentStateEnum.DRAFT.getValue());
        experimentRepository.add(experimentDO);
        handleTags(experimentBasicInfoRequest, experimentDO);
        return Response.okWithData(experimentDO.getExperimentId());
    }

    protected void handleTags(ExperimentBaseInfoRequest experimentBasicInfoRequest, ExperimentDO experimentDO) {
        List<String> tags = experimentBasicInfoRequest.getTags();
        if (!CollectionUtil.isNullOrEmpty(tags)) {
            tagManager.addTags(experimentBasicInfoRequest.getUserId(), experimentDO.getExperimentId(),
                TagDO.TAG_TYPE_EXPERIMENT, tags);
        }
    }

}

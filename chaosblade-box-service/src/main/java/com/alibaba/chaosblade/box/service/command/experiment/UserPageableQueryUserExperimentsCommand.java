package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentPageQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentDOTOUserExperimentConverter;
import com.alibaba.chaosblade.box.service.model.experiment.UserExperimentPageableQueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class UserPageableQueryUserExperimentsCommand
    extends SpringBeanCommand<UserExperimentPageableQueryRequest, Response<PageQueryResponse<UserExperiment>>> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentDOTOUserExperimentConverter experimentDOTOUserExperimentConverter;

    @Override
    public Response<PageQueryResponse<UserExperiment>> execute(
        UserExperimentPageableQueryRequest userExperimentPageableQueryRequest) {
        int page = userExperimentPageableQueryRequest.getPage();
        int size = userExperimentPageableQueryRequest.getSize();
        ExperimentPageQuery experimentPageQuery = new ExperimentPageQuery();
        experimentPageQuery.setUser(userExperimentPageableQueryRequest.getUser());
        experimentPageQuery.setNamespace(userExperimentPageableQueryRequest.getNamespace());
        experimentPageQuery.setNamespace(userExperimentPageableQueryRequest.getNamespace());
        experimentPageQuery.setTagNames(userExperimentPageableQueryRequest.getTagNames());
        experimentPageQuery.setScheduler(userExperimentPageableQueryRequest.isScheduler());
        if (!CollectionUtil.isNullOrEmpty(userExperimentPageableQueryRequest.getStates())) {
            userExperimentPageableQueryRequest.getStates().forEach(stateEnum -> {
                if (ExperimentStateEnum.FINISHED.equals(stateEnum)) {
                    experimentPageQuery.addStateValues(ExperimentStateEnum.READY);
                } else {
                    experimentPageQuery.addStateValues(stateEnum);
                }
            });
        } else if (null != userExperimentPageableQueryRequest.getState()) {
            //兼容旧的数据结构，因为openapi有依赖
            if (ExperimentStateEnum.FINISHED.equals(userExperimentPageableQueryRequest.getState())) {
                experimentPageQuery.addStateValues(ExperimentStateEnum.READY);
            } else {
                experimentPageQuery.addStateValues(userExperimentPageableQueryRequest.getState());
            }
        }
        experimentPageQuery.setRunResults(userExperimentPageableQueryRequest.getResults());
        experimentPageQuery.setPartName(userExperimentPageableQueryRequest.getSearchKey());
        IPage<ExperimentDO> experimentDOIPage = experimentRepository.findByPage(experimentPageQuery, page, size);
        PageQueryResponse<UserExperiment> pageQueryResponse = new PageQueryResponse<>();
        pageQueryResponse.setCurrentPage(experimentDOIPage.getCurrent());
        pageQueryResponse.setPages(experimentDOIPage.getPages());
        pageQueryResponse.setTotal(experimentDOIPage.getTotal());
        pageQueryResponse.setPageSize(experimentDOIPage.getSize());
        pageQueryResponse.setContent(
            experimentDOIPage.getRecords()
                .stream().map(experimentDO -> experimentDOTOUserExperimentConverter.convert(experimentDO))
                .collect(Collectors.toList())
        );
        return Response.okWithData(pageQueryResponse);
    }

}

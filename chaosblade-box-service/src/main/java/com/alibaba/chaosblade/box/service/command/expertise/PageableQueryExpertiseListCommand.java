package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.model.ExperimentRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRelationRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.UserService;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.service.model.expertise.AdminExpertiseView;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseSearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class PageableQueryExpertiseListCommand
    extends SpringBeanCommand<ExpertiseSearchRequest, Response<PageQueryResponse<AdminExpertiseView>>> {

    @Autowired
    private ExpertiseRepository expertiseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ExperimentRelationRepository experimentRelationRepository;

    @Autowired
    private TagManager tagManager;

    @Override
    public Response<PageQueryResponse<AdminExpertiseView>> execute(ExpertiseSearchRequest expertiseSearchRequest) {
        IPage<ExpertiseDO> expertiseDOIPage = expertiseRepository.findPageable(
                expertiseSearchRequest.getKey(),
                expertiseSearchRequest.getUser(),
                expertiseSearchRequest.getTagNames(),
                expertiseSearchRequest.getPage(),
                expertiseSearchRequest.getSize());
        PageQueryResponse<AdminExpertiseView> pageQueryResponse = new PageQueryResponse<>();
        pageQueryResponse.setPageSize(expertiseDOIPage.getSize());
        pageQueryResponse.setPages(expertiseDOIPage.getPages());
        pageQueryResponse.setTotal(expertiseDOIPage.getTotal());
        pageQueryResponse.setCurrentPage(expertiseDOIPage.getCurrent());
        pageQueryResponse.setContent(
            expertiseDOIPage.getRecords().stream().map(new Function<ExpertiseDO, AdminExpertiseView>() {
                @Override
                public AdminExpertiseView apply(ExpertiseDO expertiseDO) {
                    AdminExpertiseView adminExpertiseView = new AdminExpertiseView();
                    adminExpertiseView.setCreator(userService.getUserByUserId(expertiseDO.getUserId()));
                    adminExpertiseView.setExpertiseId(expertiseDO.getExpertiseId());
                    adminExpertiseView.setFunctionDesc(expertiseDO.getFunctionDesc());
                    adminExpertiseView.setName(expertiseDO.getName());
                    adminExpertiseView.setGmtCreate(expertiseDO.getGmtCreate());
                    adminExpertiseView.setGmtModified(expertiseDO.getGmtModified());
                    adminExpertiseView.setState(expertiseDO.getState());
                    adminExpertiseView.setExperimentCount(countExperimentByExpertiseId(expertiseDO.getExpertiseId()));
                    adminExpertiseView.setTags(tagManager.findTagsByExpertiseId(expertiseDO.getExpertiseId()).stream().map(
                            TagDO::getName).collect(Collectors.toSet()));
                    return adminExpertiseView;
                }
            }).collect(
                Collectors.toList()));
        return Response.okWithData(pageQueryResponse);
    }

    private Integer countExperimentByExpertiseId(String expertiseId) {
        return experimentRelationRepository.findByOuterIdAndRelationType(expertiseId,
            ExperimentRelationDO.RELATION_TYPE_EXPERTISE).size();
    }

}

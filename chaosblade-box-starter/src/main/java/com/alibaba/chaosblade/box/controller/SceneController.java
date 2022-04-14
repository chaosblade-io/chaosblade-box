package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.PageableSceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneDescriptionParser;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.SceneFunctionParameterService;
import com.alibaba.chaosblade.box.service.SceneFunctionService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.scene.SceneFunctionParameterVO;
import com.alibaba.chaosblade.box.service.model.scene.SceneFunctionVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(description = "和小程序相关")
public class SceneController extends BaseController {

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Resource
    private SceneFunctionService sceneFunctionService;

    @Resource
    private SceneFunctionParameterService sceneFunctionParameterService;

    @Autowired
    private SceneDescriptionParser sceneDescriptionParser;

    @PostMapping("QuerySceneFunctionByCategoryId")
    public RestResponse<PageableResponse<SceneFunctionVO>> querySceneFunctionByCategoryId(@LoginUser ChaosUser user,
                                                                                          @RequestBody PageableSceneQueryRequest pageableRequest) {
        SceneQueryRequest queryRequest = new SceneQueryRequest();
        queryRequest.setCategoryId(pageableRequest.getCategoryId());
        queryRequest.setPhase(pageableRequest.getPhase());
        queryRequest.setSupportScopeType(pageableRequest.getScopeType());
        queryRequest.setK8sResourceType(pageableRequest.getK8sResourceType());
        queryRequest.setOsType(pageableRequest.getOsType());

        queryRequest.setPermission(PermissionTypes.X);
        queryRequest.setUser(user);
        queryRequest.setIsPublic(true);

        PageableResponse<SceneFunctionVO> functions = convertSceneFunctions(sceneFunctionService
                .querySceneFunctions(pageableRequest.getPage(), pageableRequest.getSize(), queryRequest));
        return wrapResponse(
                functions,
                PageableResponse.of(pageableRequest.getPage(), pageableRequest.getSize(), Lists.newArrayList())
        );
    }

    @PostMapping("QuerySceneFunctionParameters")
    public RestResponse<List<SceneFunctionParameterVO>> getAllFunctionParameters(@LoginUser ChaosUser user,
                                                                                 @RequestBody SceneQueryRequest sceneQueryRequest) throws PermissionDeniedException {
        return wrapResponse(
                sceneFunctionParameterService.queryFilterParametersByFunctionId(sceneQueryRequest.getFunctionId())
                        .stream()
                        .map(SceneFunctionParameterVO::from)
                        .collect(Collectors.toList()),
                Lists.newArrayList()
        );
    }


    /**
     * 查询可修改的小程序（写权限）
     */
    @PostMapping("QueryUserSceneFunctions")
    public RestResponse<PageableResponse<SceneFunctionVO>> queryUserSceneFunctions(@LoginUser ChaosUser user,
                                                                                   @RequestBody PageableRequest pageableRequest) {
        SceneQueryRequest queryRequest = new SceneQueryRequest();
        queryRequest.setEnabled(null);
        queryRequest.setPhase(null);

        PageableResponse<SceneFunctionVO> functions = convertSceneFunctions(
                sceneFunctionService.querySceneFunctions(pageableRequest.getPage(), DEFAULT_PAGE_SIZE, queryRequest));

        return wrapResponse(
                functions,
                PageableResponse.of(pageableRequest.getPage(), DEFAULT_PAGE_SIZE, Lists.newArrayList())
        );
    }

    private List<SceneFunctionVO> convertSceneFunctions(List<SceneFunctionDO> functions) {
        if (CollectionUtil.isNullOrEmpty(functions)) {
            return Lists.newArrayList();
        }

        return functions.stream()
                .map(SceneFunctionVO::from)
                .collect(Collectors.toList());
    }

    private PageableResponse<SceneFunctionVO> convertSceneFunctions(
            PageableResponse<SceneFunctionDO> pageableResponse) {
        if (CollectionUtil.isNullOrEmpty(pageableResponse.data())) {
            return PageableResponse.clone(pageableResponse, Lists.newArrayList());
        }
        List<SceneFunctionDO> functions = Lists.newArrayList(pageableResponse.data());
        //重新构造小程序描述
        sceneDescriptionParser.parseSceneDescription(functions);
        return PageableResponse.clone(pageableResponse,
                convertSceneFunctions(functions));
    }

    private <T> RestResponse<T> wrapResponse(T data, T defaultData) {
        if (null == data) {
            return RestResponseUtil.okWithData(defaultData);
        }
        return RestResponseUtil.okWithData(data);
    }
}

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.LoadTestDefinitionRepository;
import com.alibaba.chaosblade.box.service.LoadTestDefinitionService;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 压测定义服务实现类
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Service
public class LoadTestDefinitionServiceImpl implements LoadTestDefinitionService {

    @Resource
    private LoadTestDefinitionRepository loadTestDefinitionRepository;

    @Override
    public Response<String> createLoadTestDefinition(LoadTestDefinitionCreateRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ? 
                    request.getNamespace() : "default";

            // 检查名称是否重复
            Optional<LoadTestDefinitionDO> existingDefinition = 
                    loadTestDefinitionRepository.findByName(request.getName(), userId, namespace);
            if (existingDefinition.isPresent()) {
                return Response.failedWith(CommonErrorCode.B_DUMP_EXPERIMENT_DEFINITION);
            }

            // 创建实体对象
            LoadTestDefinitionDO loadTestDefinition = new LoadTestDefinitionDO();
            loadTestDefinition.setDefinitionId("ldef_" + UUID.randomUUID().toString().replace("-", ""));
            loadTestDefinition.setName(request.getName());
            loadTestDefinition.setEngineType(request.getEngineType());
            loadTestDefinition.setEndpoint(request.getEndpoint());
            loadTestDefinition.setEntry(request.getEntry());
            loadTestDefinition.setContentRef(request.getContentRef());
            
            // 处理 urlCase
            if (request.getUrlCase() != null) {
                loadTestDefinition.setUrlCase(JSON.toJSONString(request.getUrlCase()));
            }
            
            loadTestDefinition.setCreatedBy(request.getUser().getUserName());
            loadTestDefinition.setUserId(userId);
            loadTestDefinition.setNamespace(namespace);
            loadTestDefinition.setIsDelete(0);

            // 保存到数据库
            boolean success = loadTestDefinitionRepository.add(loadTestDefinition);
            if (success) {
                return Response.okWithData(loadTestDefinition.getDefinitionId());
            } else {
                return Response.failedWith(CommonErrorCode.B_CREATE_EXPERIMENT_TASK_FAILED);
            }

        } catch (Exception e) {
            log.error("创建压测定义失败", e);
            return Response.failedWith(CommonErrorCode.B_CREATE_EXPERIMENT_TASK_FAILED);
        }
    }

    @Override
    public Response<Void> updateLoadTestDefinition(LoadTestDefinitionUpdateRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ?
                    request.getNamespace() : "default";

            // 查找现有记录
            Optional<LoadTestDefinitionDO> existingOptional =
                    loadTestDefinitionRepository.findById(request.getId());
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND);
            }

            LoadTestDefinitionDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            // 如果更新名称，检查是否重复
            if (StringUtils.hasText(request.getName()) && !request.getName().equals(existing.getName())) {
                Optional<LoadTestDefinitionDO> duplicateDefinition =
                        loadTestDefinitionRepository.findByName(request.getName(), userId, namespace);
                if (duplicateDefinition.isPresent()) {
                    return Response.failedWith(CommonErrorCode.B_DUMP_EXPERIMENT_DEFINITION);
                }
                existing.setName(request.getName());
            }

            // 更新其他字段
            if (StringUtils.hasText(request.getEngineType())) {
                existing.setEngineType(request.getEngineType());
            }
            if (StringUtils.hasText(request.getEndpoint())) {
                existing.setEndpoint(request.getEndpoint());
            }
            if (StringUtils.hasText(request.getEntry())) {
                existing.setEntry(request.getEntry());
            }
            if (request.getContentRef() != null) {
                existing.setContentRef(request.getContentRef());
            }
            if (request.getUrlCase() != null) {
                existing.setUrlCase(JSON.toJSONString(request.getUrlCase()));
            }

            // 保存更新
            boolean success = loadTestDefinitionRepository.update(existing);
            if (success) {
                return Response.ok();
            } else {
                return Response.failedWith(CommonErrorCode.B_EXPERIMENT_UPDATE);
            }

        } catch (Exception e) {
            log.error("更新压测定义失败", e);
            return Response.failedWith(CommonErrorCode.B_EXPERIMENT_UPDATE);
        }
    }

    @Override
    public Response<Void> deleteLoadTestDefinition(String definitionId, String userId, String namespace) {
        try {
            // 查找现有记录
            Optional<LoadTestDefinitionDO> existingOptional = 
                    loadTestDefinitionRepository.findById(definitionId);
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND);
            }

            LoadTestDefinitionDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            // 逻辑删除
            boolean success = loadTestDefinitionRepository.deleteById(definitionId);
            if (success) {
                return Response.ok();
            } else {
                return Response.failedWith(CommonErrorCode.S_DB_ERROR);
            }

        } catch (Exception e) {
            log.error("删除压测定义失败", e);
            return Response.failedWith(CommonErrorCode.S_DB_ERROR);
        }
    }

    @Override
    public Response<LoadTestDefinitionVO> getLoadTestDefinition(String definitionId, String userId, String namespace) {
        try {
            Optional<LoadTestDefinitionDO> existingOptional = 
                    loadTestDefinitionRepository.findById(definitionId);
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND);
            }

            LoadTestDefinitionDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            LoadTestDefinitionVO vo = LoadTestDefinitionVO.from(existing);
            return Response.okWithData(vo);

        } catch (Exception e) {
            log.error("查询压测定义失败", e);
            return Response.failedWith(CommonErrorCode.S_DB_ERROR);
        }
    }

    @Override
    public Response<PageableResponse<LoadTestDefinitionVO>> queryLoadTestDefinitions(LoadTestDefinitionQueryRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ?
                    request.getNamespace() : "default";

            // 分页查询
            PageableResponse<LoadTestDefinitionDO> pageResult = loadTestDefinitionRepository.findByPage(
                    request.getPageNum(), request.getPageSize(),
                    userId, namespace, request.getName(), request.getEngineType());

            // 转换为 VO
            List<LoadTestDefinitionVO> voList = pageResult.getData().stream()
                    .map(LoadTestDefinitionVO::from)
                    .collect(Collectors.toList());

            PageableResponse<LoadTestDefinitionVO> result = PageableResponse.of(
                    pageResult.page(), pageResult.pageSize(), voList)
                    .pages(pageResult.getPages())
                    .total(pageResult.getTotal());

            return Response.okWithData(result);

        } catch (Exception e) {
            log.error("分页查询压测定义失败", e);
            return Response.failedWith(CommonErrorCode.S_DB_ERROR);
        }
    }

    @Override
    public Response<List<LoadTestDefinitionVO>> listAllLoadTestDefinitions(String userId, String namespace) {
        try {
            String actualNamespace = StringUtils.hasText(namespace) ? namespace : "default";
            
            List<LoadTestDefinitionDO> definitions = loadTestDefinitionRepository.findAll(userId, actualNamespace);
            
            List<LoadTestDefinitionVO> voList = definitions.stream()
                    .map(LoadTestDefinitionVO::from)
                    .collect(Collectors.toList());

            return Response.okWithData(voList);

        } catch (Exception e) {
            log.error("查询所有压测定义失败", e);
            return Response.failedWith(CommonErrorCode.S_DB_ERROR);
        }
    }
}

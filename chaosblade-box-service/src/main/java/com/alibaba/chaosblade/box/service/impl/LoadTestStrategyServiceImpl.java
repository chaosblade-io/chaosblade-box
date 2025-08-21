package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.LoadTestDefinitionRepository;
import com.alibaba.chaosblade.box.dao.repository.LoadTestStrategyRepository;
import com.alibaba.chaosblade.box.service.LoadTestStrategyService;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestStrategyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 压测策略服务实现
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Service
public class LoadTestStrategyServiceImpl implements LoadTestStrategyService {

    @Resource
    private LoadTestStrategyRepository loadTestStrategyRepository;

    @Resource
    private LoadTestDefinitionRepository loadTestDefinitionRepository;

    @Resource
    private ExperimentRepository experimentRepository;

    @Override
    public Response<String> createLoadTestStrategy(LoadTestStrategyCreateRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ? 
                    request.getNamespace() : "default";

            // 检查压测定义是否存在
            Optional<LoadTestDefinitionDO> definitionOptional = 
                    loadTestDefinitionRepository.findById(request.getDefinitionId());
            if (!definitionOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测定义不存在");
            }

            LoadTestDefinitionDO definition = definitionOptional.get();
            // 权限检查
            if (!definition.getUserId().equals(userId) || !definition.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            // 检查实验是否存在
            Optional<ExperimentDO> experimentOptional = 
                    experimentRepository.findById(request.getExperimentId());
            if (!experimentOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "实验不存在");
            }

            ExperimentDO experiment = experimentOptional.get();
            // 权限检查
            if (!experiment.getUserId().equals(userId) || !experiment.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            // 检查实验是否已绑定策略
            if (loadTestStrategyRepository.existsByExperimentId(request.getExperimentId(), null)) {
                return Response.failedWith(CommonErrorCode.B_DUMP_EXPERIMENT_DEFINITION, "该实验已绑定压测策略");
            }

            // 创建策略
            LoadTestStrategyDO strategy = new LoadTestStrategyDO();
            strategy.setStrategyId("lstrategy_" + UUID.randomUUID().toString().replace("-", ""));
            strategy.setEnable(request.getEnable() ? 1 : 0);
            strategy.setDefinitionId(request.getDefinitionId());
            strategy.setExperimentId(request.getExperimentId());
            strategy.setStartBeforeFaultSec(request.getStartBeforeFaultSec());
            strategy.setTrafficDurationSec(request.getTrafficDurationSec());
            strategy.setAbortOnLoadFailure(request.getAbortOnLoadFailure() ? 1 : 0);
            strategy.setUserId(userId);
            strategy.setNamespace(namespace);
            strategy.setIsDelete(0);

            boolean success = loadTestStrategyRepository.add(strategy);
            if (success) {
                return Response.okWithData(strategy.getStrategyId());
            } else {
                return Response.failedWith(CommonErrorCode.S_DB_ERROR);
            }

        } catch (Exception e) {
            log.error("创建压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<Void> updateLoadTestStrategy(LoadTestStrategyUpdateRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ? 
                    request.getNamespace() : "default";

            // 查找现有记录
            Optional<LoadTestStrategyDO> existingOptional = 
                    loadTestStrategyRepository.findById(request.getId());
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测策略不存在");
            }

            LoadTestStrategyDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            // 更新字段
            boolean hasChanges = false;

            if (request.getEnable() != null) {
                existing.setEnable(request.getEnable() ? 1 : 0);
                hasChanges = true;
            }

            if (StringUtils.hasText(request.getDefinitionId()) && 
                !request.getDefinitionId().equals(existing.getDefinitionId())) {
                // 检查新的压测定义是否存在
                Optional<LoadTestDefinitionDO> definitionOptional = 
                        loadTestDefinitionRepository.findById(request.getDefinitionId());
                if (!definitionOptional.isPresent()) {
                    return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测定义不存在");
                }

                LoadTestDefinitionDO definition = definitionOptional.get();
                if (!definition.getUserId().equals(userId) || !definition.getNamespace().equals(namespace)) {
                    return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
                }

                existing.setDefinitionId(request.getDefinitionId());
                hasChanges = true;
            }

            if (StringUtils.hasText(request.getExperimentId()) && 
                !request.getExperimentId().equals(existing.getExperimentId())) {
                // 检查新的实验是否存在
                Optional<ExperimentDO> experimentOptional = 
                        experimentRepository.findById(request.getExperimentId());
                if (!experimentOptional.isPresent()) {
                    return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "实验不存在");
                }

                ExperimentDO experiment = experimentOptional.get();
                if (!experiment.getUserId().equals(userId) || !experiment.getNamespace().equals(namespace)) {
                    return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
                }

                // 检查新实验是否已绑定其他策略
                if (loadTestStrategyRepository.existsByExperimentId(request.getExperimentId(), request.getId())) {
                    return Response.failedWith(CommonErrorCode.B_DUMP_EXPERIMENT_DEFINITION, "该实验已绑定其他压测策略");
                }

                existing.setExperimentId(request.getExperimentId());
                hasChanges = true;
            }

            if (request.getStartBeforeFaultSec() != null) {
                existing.setStartBeforeFaultSec(request.getStartBeforeFaultSec());
                hasChanges = true;
            }

            if (request.getTrafficDurationSec() != null) {
                existing.setTrafficDurationSec(request.getTrafficDurationSec());
                hasChanges = true;
            }

            if (request.getAbortOnLoadFailure() != null) {
                existing.setAbortOnLoadFailure(request.getAbortOnLoadFailure() ? 1 : 0);
                hasChanges = true;
            }

            if (!hasChanges) {
                return Response.ok();
            }

            boolean success = loadTestStrategyRepository.update(existing);
            if (success) {
                return Response.ok();
            } else {
                return Response.failedWith(CommonErrorCode.S_DB_ERROR);
            }

        } catch (Exception e) {
            log.error("更新压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<Void> deleteLoadTestStrategy(String strategyId, String userId, String namespace) {
        try {
            // 查找现有记录
            Optional<LoadTestStrategyDO> existingOptional = 
                    loadTestStrategyRepository.findById(strategyId);
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测策略不存在");
            }

            LoadTestStrategyDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            // 逻辑删除
            boolean success = loadTestStrategyRepository.deleteById(strategyId);
            if (success) {
                return Response.ok();
            } else {
                return Response.failedWith(CommonErrorCode.S_DB_ERROR);
            }

        } catch (Exception e) {
            log.error("删除压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<LoadTestStrategyVO> getLoadTestStrategy(String strategyId, String userId, String namespace) {
        try {
            Optional<LoadTestStrategyDO> existingOptional =
                    loadTestStrategyRepository.findById(strategyId);
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测策略不存在");
            }

            LoadTestStrategyDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            LoadTestStrategyVO vo = LoadTestStrategyVO.from(existing);
            return Response.okWithData(vo);

        } catch (Exception e) {
            log.error("查询压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<LoadTestStrategyVO> getLoadTestStrategyByExperimentId(String experimentId, String userId, String namespace) {
        try {
            Optional<LoadTestStrategyDO> existingOptional =
                    loadTestStrategyRepository.findByExperimentId(experimentId);
            if (!existingOptional.isPresent()) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "该实验未绑定压测策略");
            }

            LoadTestStrategyDO existing = existingOptional.get();

            // 权限检查
            if (!existing.getUserId().equals(userId) || !existing.getNamespace().equals(namespace)) {
                return Response.failedWith(CommonErrorCode.P_USER_PERMISSION_DENIED);
            }

            LoadTestStrategyVO vo = LoadTestStrategyVO.from(existing);
            return Response.okWithData(vo);

        } catch (Exception e) {
            log.error("根据实验ID查询压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<List<LoadTestStrategyVO>> getLoadTestStrategiesByDefinitionId(String definitionId, String userId, String namespace) {
        try {
            List<LoadTestStrategyDO> strategies = loadTestStrategyRepository.findByDefinitionId(definitionId);

            // 过滤权限
            List<LoadTestStrategyVO> voList = strategies.stream()
                    .filter(strategy -> strategy.getUserId().equals(userId) && strategy.getNamespace().equals(namespace))
                    .map(LoadTestStrategyVO::from)
                    .collect(Collectors.toList());

            return Response.okWithData(voList);

        } catch (Exception e) {
            log.error("根据压测定义ID查询压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<PageableResponse<LoadTestStrategyVO>> queryLoadTestStrategies(LoadTestStrategyQueryRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            String userId = request.getUser().getUserId();
            String namespace = StringUtils.hasText(request.getNamespace()) ?
                    request.getNamespace() : "default";

            Integer enable = null;
            if (request.getEnable() != null) {
                enable = request.getEnable() ? 1 : 0;
            }

            // 分页查询
            PageableResponse<LoadTestStrategyDO> pageResult = loadTestStrategyRepository.findByPage(
                    request.getPageNum(), request.getPageSize(),
                    userId, namespace, request.getDefinitionId(), request.getExperimentId(), enable);

            // 转换为 VO
            List<LoadTestStrategyVO> voList = pageResult.getData().stream()
                    .map(LoadTestStrategyVO::from)
                    .collect(Collectors.toList());

            PageableResponse<LoadTestStrategyVO> result = PageableResponse.of(
                    pageResult.page(), pageResult.pageSize(), voList)
                    .pages(pageResult.pages())
                    .total(pageResult.total());

            return Response.okWithData(result);

        } catch (Exception e) {
            log.error("分页查询压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }

    @Override
    public Response<List<LoadTestStrategyVO>> listAllLoadTestStrategies(String userId, String namespace) {
        try {
            List<LoadTestStrategyDO> strategies = loadTestStrategyRepository.findAll(userId, namespace);

            List<LoadTestStrategyVO> voList = strategies.stream()
                    .map(LoadTestStrategyVO::from)
                    .collect(Collectors.toList());

            return Response.okWithData(voList);

        } catch (Exception e) {
            log.error("查询所有压测策略失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }
}

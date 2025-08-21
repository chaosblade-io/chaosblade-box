package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.JmxFileUploadRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.JmxFileUploadResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionVO;

import java.util.List;

/**
 * 压测定义服务接口
 *
 * @author ZhengMingZhuo
 */
public interface LoadTestDefinitionService {

    /**
     * 创建压测定义
     *
     * @param request 创建请求
     * @return 创建结果
     */
    Response<String> createLoadTestDefinition(LoadTestDefinitionCreateRequest request);

    /**
     * 更新压测定义
     *
     * @param request 更新请求
     * @return 更新结果
     */
    Response<Void> updateLoadTestDefinition(LoadTestDefinitionUpdateRequest request);

    /**
     * 删除压测定义
     *
     * @param definitionId 压测定义ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 删除结果
     */
    Response<Void> deleteLoadTestDefinition(String definitionId, String userId, String namespace);

    /**
     * 根据ID查询压测定义
     *
     * @param definitionId 压测定义ID
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测定义详情
     */
    Response<LoadTestDefinitionVO> getLoadTestDefinition(String definitionId, String userId, String namespace);

    /**
     * 分页查询压测定义
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Response<PageableResponse<LoadTestDefinitionVO>> queryLoadTestDefinitions(LoadTestDefinitionQueryRequest request);

    /**
     * 查询所有压测定义（不分页）
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测定义列表
     */
    Response<List<LoadTestDefinitionVO>> listAllLoadTestDefinitions(String userId, String namespace);

    /**
     * 上传JMX文件到压测引擎
     *
     * @param request 文件上传请求
     * @return 上传结果
     */
    Response<JmxFileUploadResponse> uploadJmxFile(JmxFileUploadRequest request);
}

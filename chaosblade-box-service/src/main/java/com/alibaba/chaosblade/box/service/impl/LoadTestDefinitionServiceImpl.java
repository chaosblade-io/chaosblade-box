package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.LoadTestDefinitionRepository;
import com.alibaba.chaosblade.box.service.LoadTestDefinitionService;
import com.alibaba.chaosblade.box.service.model.loadtest.JmxFileUploadRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.JmxFileUploadResponse;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionCreateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionQueryRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionUpdateRequest;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestDefinitionVO;
import com.alibaba.chaosblade.box.service.model.loadtest.LoadTestEngineResponse;
import com.alibaba.chaosblade.box.service.util.MultipartFormDataBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    @Override
    public Response<JmxFileUploadResponse> uploadJmxFile(JmxFileUploadRequest request) {
        try {
            // 参数校验
            if (request.getUser() == null) {
                return Response.failedWith(CommonErrorCode.P_LOGIN_MISSED);
            }

            if (request.getFile() == null || request.getFile().isEmpty()) {
                return Response.failedWith(CommonErrorCode.P_ARGUMENT_ILLEGAL, "文件不能为空");
            }

            if (!StringUtils.hasText(request.getEndpoint())) {
                return Response.failedWith(CommonErrorCode.P_ARGUMENT_ILLEGAL, "压测引擎端点不能为空");
            }

            // 文件格式校验
            String fileExtension = request.getFile().getFileExtension();
            if (!"jmx".equals(fileExtension)) {
                return Response.failedWith(CommonErrorCode.P_ARGUMENT_ILLEGAL, "文件格式必须为.jmx");
            }

            // 文件大小校验（500MB = 500 * 1024 * 1024 bytes）
            long maxFileSize = 500L * 1024 * 1024;
            if (request.getFile().getFileSize() > maxFileSize) {
                return Response.failedWith(CommonErrorCode.P_ARGUMENT_ILLEGAL, "文件大小不能超过500MB");
            }

            // 端点格式校验
            if (!isValidEndpoint(request.getEndpoint())) {
                return Response.failedWith(CommonErrorCode.P_ARGUMENT_ILLEGAL, "压测引擎端点格式无效");
            }

            // 构建上传URL
            String uploadUrl = buildUploadUrl(request.getEndpoint());
            log.info("开始上传JMX文件到压测引擎: {}, 文件名: {}, 文件大小: {} bytes",
                    uploadUrl, request.getFile().getFileName(), request.getFile().getFileSize());

            // 上传文件到压测引擎
            LoadTestEngineResponse<JmxFileUploadResponse.EngineFileUploadData> engineResponse =
                    uploadFileToEngine(uploadUrl, request.getFile());

            if (engineResponse == null || !engineResponse.isSuccess()) {
                String errorMsg = engineResponse != null && engineResponse.getMessage() != null ?
                        engineResponse.getMessage() : "压测引擎响应异常";
                log.error("文件上传到压测引擎失败: {}", errorMsg);
                return Response.failedWith(CommonErrorCode.S_REQUEST_OUTER_SYSTEM_FAILED,
                        "文件上传失败: " + errorMsg);
            }

            // 转换响应
            JmxFileUploadResponse response = JmxFileUploadResponse.fromEngineResponse(engineResponse.getData());
            log.info("JMX文件上传成功: {}", JSON.toJSONString(response));

            return Response.okWithData(response);

        } catch (Exception e) {
            log.error("上传JMX文件失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 验证端点格式
     *
     * @param endpoint 端点
     * @return 是否有效
     */
    private boolean isValidEndpoint(String endpoint) {
        try {
            URL url = new URL(endpoint);
            return "http".equals(url.getProtocol()) || "https".equals(url.getProtocol());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 构建上传URL
     *
     * @param endpoint 端点
     * @return 上传URL
     */
    private String buildUploadUrl(String endpoint) {
        String baseUrl = endpoint.endsWith("/") ? endpoint.substring(0, endpoint.length() - 1) : endpoint;
        return baseUrl + "/api/files/upload/testplan";
    }

    /**
     * 上传文件到压测引擎
     *
     * @param uploadUrl 上传URL
     * @param fileData 文件数据
     * @return 压测引擎响应
     */
    private LoadTestEngineResponse<JmxFileUploadResponse.EngineFileUploadData> uploadFileToEngine(
            String uploadUrl, com.alibaba.chaosblade.box.service.model.loadtest.FileUploadData fileData) {

        CloseableHttpClient httpClient = null;
        MultipartFormDataBuilder builder = null;

        try {
            // 创建HTTP客户端
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(30000)
                    .setConnectTimeout(30000)
                    .setSocketTimeout(300000) // 5分钟超时，文件上传可能较慢
                    .build();

            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            // 构建multipart/form-data
            builder = new MultipartFormDataBuilder();
            builder.addFileField("file", fileData.getFileName(),
                    fileData.getContentType() != null ? fileData.getContentType() : "application/octet-stream",
                    fileData.getFileContent());

            byte[] multipartData = builder.build();

            // 创建HTTP请求
            HttpPost httpPost = new HttpPost(uploadUrl);
            httpPost.setHeader("Content-Type", builder.getContentType());
            httpPost.setEntity(new ByteArrayEntity(multipartData));

            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            log.info("压测引擎响应状态码: {}, 响应内容: {}", statusCode, responseBody);

            if (statusCode == 200) {
                // 解析响应
                return JSON.parseObject(responseBody,
                        new TypeReference<LoadTestEngineResponse<JmxFileUploadResponse.EngineFileUploadData>>() {});
            } else {
                log.error("压测引擎返回错误状态码: {}, 响应: {}", statusCode, responseBody);
                LoadTestEngineResponse<JmxFileUploadResponse.EngineFileUploadData> errorResponse =
                        new LoadTestEngineResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("HTTP " + statusCode + ": " + responseBody);
                return errorResponse;
            }

        } catch (Exception e) {
            log.error("调用压测引擎接口失败", e);
            LoadTestEngineResponse<JmxFileUploadResponse.EngineFileUploadData> errorResponse =
                    new LoadTestEngineResponse<>();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("网络请求失败: " + e.getMessage());
            return errorResponse;
        } finally {
            // 清理资源
            if (builder != null) {
                builder.close();
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.warn("关闭HTTP客户端失败", e);
                }
            }
        }
    }
}

package com.alibaba.chaosblade.box.dao.infrastructure.loadtest.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestEngineClient;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.model.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 压测引擎客户端实现类
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Component
public class LoadTestEngineClientImpl implements LoadTestEngineClient {

    @Resource(name = "loadTestRestTemplate")
    private RestTemplate restTemplate;

    @Value("${chaosblade.loadtest.engine.host:localhost:8008}")
    private String loadTestEngineHost;

    private static final String START_API = "/api/loadtest/start";
    private static final String STOP_API = "/api/loadtest/stop/{executionId}";
    private static final String STATUS_API = "/api/loadtest/status/{executionId}";
    private static final String RESULTS_API = "/api/loadtest/results/{executionId}";
    private static final String EVENTS_API = "/api/loadtest/events/{executionId}";

    @Override
    public Response<LoadTestExecutionResponse> startLoadTest(LoadTestStartRequest request) {
        try {
            String url = "http://" + loadTestEngineHost + START_API;

            // 添加详细的请求日志
            log.info("启动压测请求: URL={}, Request={}", url, request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LoadTestStartRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, entity, Map.class);

            log.info("压测引擎响应: Status={}, Body={}", responseEntity.getStatusCode(), responseEntity.getBody());

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = responseEntity.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                    LoadTestExecutionResponse executionResponse = convertToExecutionResponse(data);
                    return Response.okWithData(executionResponse);
                } else {
                    String message = responseBody != null ? (String) responseBody.get("message") : "启动压测失败";
                    log.error("压测引擎返回失败响应: {}", responseBody);
                    return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, message);
                }
            } else {
                log.error("压测引擎响应异常: Status={}, Body={}", responseEntity.getStatusCode(), responseEntity.getBody());
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "压测引擎响应异常: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("启动压测失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "启动压测失败: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> stopLoadTest(String executionId) {
        try {
            String url = "http://" + loadTestEngineHost + STOP_API;
            
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("executionId", executionId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, entity, Map.class, uriVariables);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = responseEntity.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                    Boolean data = (Boolean) responseBody.get("data");
                    return Response.okWithData(data != null ? data : true);
                } else {
                    String message = responseBody != null ? (String) responseBody.get("message") : "停止压测失败";
                    return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, message);
                }
            } else {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "压测引擎响应异常");
            }
        } catch (Exception e) {
            log.error("停止压测失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "停止压测失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestStatusResponse> getLoadTestStatus(String executionId) {
        try {
            String url = "http://" + loadTestEngineHost + STATUS_API;
            
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("executionId", executionId);
            
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class, uriVariables);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = responseEntity.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                    LoadTestStatusResponse statusResponse = convertToStatusResponse(data);
                    return Response.okWithData(statusResponse);
                } else {
                    String message = responseBody != null ? (String) responseBody.get("message") : "查询压测状态失败";
                    return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, message);
                }
            } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            } else {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "压测引擎响应异常");
            }
        } catch (Exception e) {
            log.error("查询压测状态失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "查询压测状态失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestResultResponse> getLoadTestResults(String executionId) {
        try {
            String url = "http://" + loadTestEngineHost + RESULTS_API;
            
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("executionId", executionId);
            
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class, uriVariables);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = responseEntity.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                    LoadTestResultResponse resultResponse = convertToResultResponse(data);
                    return Response.okWithData(resultResponse);
                } else {
                    String message = responseBody != null ? (String) responseBody.get("message") : "获取压测结果失败";
                    return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, message);
                }
            } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Response.failedWith(CommonErrorCode.P_EXPERIMENT_NOT_FOUND, "压测任务不存在");
            } else {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "压测引擎响应异常");
            }
        } catch (Exception e) {
            log.error("获取压测结果失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "获取压测结果失败: " + e.getMessage());
        }
    }

    @Override
    public Response<LoadTestEventsResponse> getLoadTestEvents(String executionId, Integer tail) {
        try {
            String url = "http://" + loadTestEngineHost + EVENTS_API;
            if (tail != null) {
                url += "?tail=" + tail;
            }
            
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("executionId", executionId);
            
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class, uriVariables);
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = responseEntity.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
                    Object data = responseBody.get("data");
                    LoadTestEventsResponse eventsResponse = convertToEventsResponse(executionId, data);
                    return Response.okWithData(eventsResponse);
                } else {
                    String message = responseBody != null ? (String) responseBody.get("message") : "获取压测事件失败";
                    return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, message);
                }
            } else {
                return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "压测引擎响应异常");
            }
        } catch (Exception e) {
            log.error("获取压测事件失败", e);
            return Response.failedWith(CommonErrorCode.S_SYSTEM_ERROR, "获取压测事件失败: " + e.getMessage());
        }
    }

    private LoadTestExecutionResponse convertToExecutionResponse(Map<String, Object> data) {
        LoadTestExecutionResponse response = new LoadTestExecutionResponse();
        response.setExecutionId((String) data.get("executionId"));
        response.setTestName((String) data.get("testName"));
        response.setDescription((String) data.get("description"));
        response.setTestPlanPath((String) data.get("testPlanPath"));
        response.setContainerId((String) data.get("containerId"));
        response.setContainerName((String) data.get("containerName"));
        response.setStatus((String) data.get("status"));
        response.setStartTime((String) data.get("startTime"));
        response.setEndTime((String) data.get("endTime"));
        response.setResultPath((String) data.get("resultPath"));
        response.setReportPath((String) data.get("reportPath"));
        response.setLogPath((String) data.get("logPath"));
        response.setParameters((Map<String, Object>) data.get("parameters"));
        response.setErrorMessage((String) data.get("errorMessage"));
        return response;
    }

    private LoadTestStatusResponse convertToStatusResponse(Map<String, Object> data) {
        LoadTestStatusResponse response = new LoadTestStatusResponse();
        response.setExecutionId((String) data.get("executionId"));
        response.setTestName((String) data.get("testName"));
        response.setDescription((String) data.get("description"));
        response.setTestPlanPath((String) data.get("testPlanPath"));
        response.setContainerId((String) data.get("containerId"));
        response.setContainerName((String) data.get("containerName"));
        response.setStatus((String) data.get("status"));
        response.setStartTime((String) data.get("startTime"));
        response.setEndTime((String) data.get("endTime"));
        response.setResultPath((String) data.get("resultPath"));
        response.setReportPath((String) data.get("reportPath"));
        response.setLogPath((String) data.get("logPath"));
        response.setParameters((Map<String, Object>) data.get("parameters"));
        response.setErrorMessage((String) data.get("errorMessage"));
        return response;
    }

    private LoadTestResultResponse convertToResultResponse(Map<String, Object> data) {
        LoadTestResultResponse response = new LoadTestResultResponse();
        response.setExecutionId((String) data.get("executionId"));
        response.setStatus((String) data.get("status"));
        response.setResultPath((String) data.get("resultPath"));
        response.setReportPath((String) data.get("reportPath"));
        response.setLogPath((String) data.get("logPath"));
        response.setResultUrl((String) data.get("resultUrl"));
        response.setReportUrl((String) data.get("reportUrl"));
        return response;
    }

    private LoadTestEventsResponse convertToEventsResponse(String executionId, Object data) {
        LoadTestEventsResponse response = new LoadTestEventsResponse();
        response.setExecutionId(executionId);
        if (data instanceof java.util.List) {
            response.setEvents((java.util.List<String>) data);
        } else {
            response.setEvents(java.util.Collections.emptyList());
        }
        return response;
    }
}

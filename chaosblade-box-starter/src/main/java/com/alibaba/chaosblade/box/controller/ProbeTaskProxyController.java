package com.alibaba.chaosblade.box.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 探测任务相关接口代理转发
 * 要求：保持原始请求的所有参数、请求头和请求体不变，直接转发到探测任务服务，并将响应原样返回
 */
@RestController
public class ProbeTaskProxyController extends BaseController {

    // 支持从配置读取，若未配置则使用文档中的默认地址
    @Value("${probe.api.base-url:http://1.94.151.57:8101}")
    private String probeBaseUrl;

    // 复用已有的 RestTemplate（具备超时配置）
    @Resource(name = "loadTestRestTemplate")
    private RestTemplate restTemplate;

    // 1. 获取系统信息 GET /api/systems
    @GetMapping("/systems")
    public ResponseEntity<byte[]> getSystems(HttpServletRequest request) {
        return forward(request, HttpMethod.GET, null, "/api/systems");
    }

    // 2. 获取API信息 GET /api/systems/{systemId}/apis
    @GetMapping("/systems/{systemId}/apis")
    public ResponseEntity<byte[]> getApis(@PathVariable("systemId") String systemId, HttpServletRequest request) {
        String targetPath = "/api/systems/" + systemId + "/apis";
        return forward(request, HttpMethod.GET, null, targetPath);
    }

    // 3. 获取拓扑 GET /api/topologies/{apiId}/topology
    @GetMapping("/topologies/{apiId}/topology")
    public ResponseEntity<byte[]> getTopology(@PathVariable("apiId") String apiId, HttpServletRequest request) {
        String targetPath = "/api/topologies/" + apiId + "/topology";
        return forward(request, HttpMethod.GET, null, targetPath);
    }

    // 获取故障类型 GET /api/fault-types
    @GetMapping("/fault-types")
    public ResponseEntity<byte[]> getFaultTypes(HttpServletRequest request) {
        return forward(request, HttpMethod.GET, null, "/api/fault-types");
    }

    // 4. 新增task POST /api/probe-tasks
    @PostMapping("/probe-tasks")
    public ResponseEntity<byte[]> createProbeTask(@RequestBody(required = false) byte[] body, HttpServletRequest request) {
        return forward(request, HttpMethod.POST, body, "/api/probe-tasks");
    }

    // v2：获取任务列表 GET /api/detection-tasks
    @GetMapping("/detection-tasks")
    public ResponseEntity<byte[]> listDetectionTasks(HttpServletRequest request) {
        return forward(request, HttpMethod.GET, null, "/api/detection-tasks");
    }

    // v2：获取任务详情（增强版，聚合）GET /api/detection-tasks/{taskId}
    @GetMapping("/detection-tasks/{taskId}")
    public ResponseEntity<byte[]> getDetectionTask(@PathVariable("taskId") String taskId, HttpServletRequest request) {
        String targetPath = "/api/detection-tasks/" + taskId;
        return forward(request, HttpMethod.GET, null, targetPath);
    }

    // v2：任务执行历史（分页）GET /api/detection-tasks/{taskId}/executions
    @GetMapping("/detection-tasks/{taskId}/executions")
    public ResponseEntity<byte[]> listDetectionTaskExecutions(@PathVariable("taskId") String taskId, HttpServletRequest request) {
        String targetPath = "/api/detection-tasks/" + taskId + "/executions";
        return forward(request, HttpMethod.GET, null, targetPath);
    }

    // --- 任务执行相关接口（docs/任务执行接口说明.md） ---

    // 1) 代理执行接口：POST /api/detection-tasks/{taskId}/execute
    @PostMapping("/detection-tasks/{taskId}/execute")
    public ResponseEntity<byte[]> executeDetectionTask(@PathVariable("taskId") String taskId,
                                                       @RequestBody(required = false) byte[] body,
                                                       HttpServletRequest request) {
        String targetPath = "/api/detection-tasks/" + taskId + "/execute";
        return forward(request, HttpMethod.POST, body, targetPath);
    }

    // 2) 任务执行详情：GET /api/task-executions/{executionId}
    @GetMapping("/task-executions/{executionId}")
    public ResponseEntity<byte[]> getTaskExecutionById(@PathVariable("executionId") String executionId,
                                                       HttpServletRequest request) {
        String targetPath = "/api/task-executions/" + executionId;
        return forward(request, HttpMethod.GET, null, targetPath);
    }

    // 2-别名) 任务执行详情：GET /api/executions/{executionId}
    @GetMapping("/executions/{executionId}")
    public ResponseEntity<byte[]> getTaskExecutionByIdAlias(@PathVariable("executionId") String executionId,
                                                            HttpServletRequest request) {
        String targetPath = "/api/executions/" + executionId;
        return forward(request, HttpMethod.GET, null, targetPath);
    }

    // 3) 执行列表分页查询：GET /api/task-executions
    @GetMapping("/task-executions")
    public ResponseEntity<byte[]> listTaskExecutions(HttpServletRequest request) {
        return forward(request, HttpMethod.GET, null, "/api/task-executions");
    }

    // 3-别名) 执行列表分页查询：GET /api/executions
    @GetMapping("/executions")
    public ResponseEntity<byte[]> listTaskExecutionsAlias(HttpServletRequest request) {
        return forward(request, HttpMethod.GET, null, "/api/executions");
    }


    // 新增：支持指定固定的上游路径，不跟随前端请求路径
    private ResponseEntity<byte[]> forward(HttpServletRequest request, HttpMethod method, byte[] body, String targetPath) {
        String targetUrl = buildTargetUrl(targetPath, request.getQueryString());
        HttpHeaders headers = extractRequestHeaders(request);
        headers.remove(HttpHeaders.HOST);
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(targetUrl, method, httpEntity, byte[].class);
        HttpHeaders respHeaders = filterResponseHeaders(responseEntity.getHeaders());
        return new ResponseEntity<>(responseEntity.getBody(), respHeaders, responseEntity.getStatusCode());
    }

    private String buildTargetUrl(String targetPath, String query) {
        StringBuilder url = new StringBuilder();
        url.append(probeBaseUrl);
        url.append(targetPath);
        if (query != null && !query.isEmpty()) {
            url.append("?").append(query);
        }
        return url.toString();
    }

    private HttpHeaders extractRequestHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) {
                headers.add(name, values.nextElement());
            }
        }
        return headers;
    }

    private HttpHeaders filterResponseHeaders(HttpHeaders original) {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, List<String>> entry : original.entrySet()) {
            String name = entry.getKey();
            if (name == null) continue;
            // 让容器自行处理的头部不透传
            if (HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(name)
                    || HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)
                    || "Connection".equalsIgnoreCase(name)) {
                continue;
            }
            headers.put(name, entry.getValue());
        }
        return headers;
    }
}


package com.alibaba.chaosblade.box.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * K8s 资源图谱与拓扑可视化反向代理控制器。
 * <p>
 * 将前端发出的以下路径透明转发到 svc-k8s-graph / svc-topo 服务（端口 8106）：
 * <ul>
 *   <li>{@code /xflow/**}          → {@code {topoBaseUrl}/api/**}（去掉 /xflow 前缀）</li>
 *   <li>{@code /k8s-graph/**}      → {@code {topoBaseUrl}/k8s-graph/**}</li>
 *   <li>{@code /risk-analysis/**}  → {@code {topoBaseUrl}/risk-analysis/**}</li>
 * </ul>
 * <p>
 * 本控制器 <b>不继承 BaseController</b>，因此没有 {@code /chaos/} 前缀，
 * 路径直接以 {@code /api/} 开头，与前端约定一致。
 */
@Slf4j
@RestController
public class K8sGraphProxyController {

    @Value("${probe.topo.base-url:http://1.94.151.57:8106}")
    private String topoBaseUrl;

    @Resource(name = "loadTestRestTemplate")
    private RestTemplate restTemplate;

    // ======================== /xflow/** ========================
    // 路径改写：/xflow/foo/bar  →  {topoBaseUrl}/api/foo/bar

    /**
     * 转发 /xflow/** 的 GET 请求。
     */
    @GetMapping("/xflow/**")
    public ResponseEntity<byte[]> xflowGet(HttpServletRequest request) {
        return forwardXflow(request, HttpMethod.GET, null);
    }

    /**
     * 转发 /xflow/** 的 POST 请求。
     */
    @PostMapping("/xflow/**")
    public ResponseEntity<byte[]> xflowPost(@RequestBody(required = false) byte[] body,
                                            HttpServletRequest request) {
        return forwardXflow(request, HttpMethod.POST, body);
    }

    /**
     * 转发 /xflow/** 的 PUT 请求。
     */
    @PutMapping("/xflow/**")
    public ResponseEntity<byte[]> xflowPut(@RequestBody(required = false) byte[] body,
                                           HttpServletRequest request) {
        return forwardXflow(request, HttpMethod.PUT, body);
    }

    /**
     * 转发 /xflow/** 的 DELETE 请求。
     */
    @DeleteMapping("/xflow/**")
    public ResponseEntity<byte[]> xflowDelete(HttpServletRequest request) {
        return forwardXflow(request, HttpMethod.DELETE, null);
    }

    // ======================== /k8s-graph/** ========================

    /**
     * 转发 /k8s-graph/** 的 GET 请求。
     */
    @GetMapping("/k8s-graph/**")
    public ResponseEntity<byte[]> k8sGraphGet(HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.GET, null);
    }

    /**
     * 转发 /k8s-graph/** 的 POST 请求。
     */
    @PostMapping("/k8s-graph/**")
    public ResponseEntity<byte[]> k8sGraphPost(@RequestBody(required = false) byte[] body,
                                               HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.POST, body);
    }

    /**
     * 转发 /k8s-graph/** 的 PUT 请求。
     */
    @PutMapping("/k8s-graph/**")
    public ResponseEntity<byte[]> k8sGraphPut(@RequestBody(required = false) byte[] body,
                                              HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.PUT, body);
    }

    /**
     * 转发 /k8s-graph/** 的 DELETE 请求。
     */
    @DeleteMapping("/k8s-graph/**")
    public ResponseEntity<byte[]> k8sGraphDelete(HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.DELETE, null);
    }

    // ======================== /risk-analysis/** ========================

    /**
     * 转发 /risk-analysis/** 的 GET 请求。
     */
    @GetMapping("/risk-analysis/**")
    public ResponseEntity<byte[]> riskAnalysisGet(HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.GET, null);
    }

    /**
     * 转发 /risk-analysis/** 的 POST 请求。
     */
    @PostMapping("/risk-analysis/**")
    public ResponseEntity<byte[]> riskAnalysisPost(@RequestBody(required = false) byte[] body,
                                                   HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.POST, body);
    }

    /**
     * 转发 /risk-analysis/** 的 PUT 请求。
     */
    @PutMapping("/risk-analysis/**")
    public ResponseEntity<byte[]> riskAnalysisPut(@RequestBody(required = false) byte[] body,
                                                  HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.PUT, body);
    }

    /**
     * 转发 /risk-analysis/** 的 DELETE 请求。
     */
    @DeleteMapping("/risk-analysis/**")
    public ResponseEntity<byte[]> riskAnalysisDelete(HttpServletRequest request) {
        return forwardDirect(request, HttpMethod.DELETE, null);
    }

    // ======================== 内部转发方法 ========================

    /**
     * 转发 /xflow/** 请求，去掉 /xflow 路径段。
     * 例如 /xflow/topology → {topoBaseUrl}/api/topology
     */
    private ResponseEntity<byte[]> forwardXflow(HttpServletRequest request, HttpMethod method, byte[] body) {
        // 原始路径: /xflow/xxx  →  目标路径: /api/xxx
        String requestUri = request.getRequestURI();
        String suffix = requestUri.substring("/xflow".length()); // 包含前导 /
        String targetPath = "/api" + suffix;
        return doForward(request, method, body, targetPath);
    }

    /**
     * 直接转发（路径不改写），用于 /k8s-graph/** 和 /risk-analysis/**。
     */
    private ResponseEntity<byte[]> forwardDirect(HttpServletRequest request, HttpMethod method, byte[] body) {
        // 请求 URI 是 /k8s-graph/... 或 /risk-analysis/...，需要加 /api 前缀匹配后端路由
        String targetPath = "/api" + request.getRequestURI();
        return doForward(request, method, body, targetPath);
    }

    /**
     * 执行实际的 HTTP 转发。
     *
     * @param request    原始 Servlet 请求
     * @param method     HTTP 方法
     * @param body       请求体（可为 null）
     * @param targetPath 目标服务上的路径（含 query string 之前的部分）
     * @return 上游响应（状态码 + 头 + 体原样返回）
     */
    private ResponseEntity<byte[]> doForward(HttpServletRequest request, HttpMethod method,
                                             byte[] body, String targetPath) {
        String targetUrl = buildUrl(targetPath, request.getQueryString());
        log.info("Proxy {} {} → {}", method, request.getRequestURI(), targetUrl);

        HttpHeaders headers = extractRequestHeaders(request);
        headers.remove(HttpHeaders.HOST);
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        HttpEntity<byte[]> httpEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(targetUrl, method, httpEntity, byte[].class);
            HttpHeaders respHeaders = filterResponseHeaders(response.getHeaders());
            return new ResponseEntity<>(response.getBody(), respHeaders, response.getStatusCode());
        } catch (HttpStatusCodeException e) {
            // 上游返回 4xx/5xx 时，也原样透传状态码和响应体
            log.warn("Upstream returned {} for {}", e.getStatusCode(), targetUrl);
            HttpHeaders respHeaders = filterResponseHeaders(e.getResponseHeaders());
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), respHeaders, e.getStatusCode());
        }
    }

    private String buildUrl(String targetPath, String queryString) {
        StringBuilder url = new StringBuilder(topoBaseUrl);
        url.append(targetPath);
        if (queryString != null && !queryString.isEmpty()) {
            url.append("?").append(queryString);
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
        if (original == null) {
            return new HttpHeaders();
        }
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, List<String>> entry : original.entrySet()) {
            String name = entry.getKey();
            if (name == null) {
                continue;
            }
            // 让 Servlet 容器自行处理的头部不透传
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

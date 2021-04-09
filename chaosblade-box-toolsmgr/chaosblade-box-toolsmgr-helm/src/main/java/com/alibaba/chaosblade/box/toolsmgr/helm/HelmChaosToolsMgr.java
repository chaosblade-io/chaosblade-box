package com.alibaba.chaosblade.box.toolsmgr.helm;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.chaosblade.box.common.DeviceMeta;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.utils.SystemPropertiesUtils;
import com.alibaba.chaosblade.box.toolsmgr.api.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yefei
 */
@Slf4j
@Component
@ChannelStrategy(ChannelType.HELM)
public class HelmChaosToolsMgr implements ChaosToolsMgr<HelmRequest>, InitializingBean {

    @Value("${chaos.helm.repo.url}")
    private String helmRepoUrl;

    @Value("${chaos.helm.repo.name}")
    private String helmRepoName;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StrUtil.isNotBlank(helmRepoUrl)) {
            String command = String.format("helm repo add %s %s", helmRepoName, helmRepoUrl);
            Process process = RuntimeUtil.exec(command);
            process.waitFor();
            if (process.getErrorStream().available() > 0) {
                throw new BizException(IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
            }
        }
    }

    @Override
    public Response<List<DeviceMeta>> listHosts(HelmRequest helmRequest) {
        throw new UnsupportedOperationException("helm not support");
    }

    @Override
    public Response<String> deployAgent(HelmRequest helmRequest) {
        throw new UnsupportedOperationException("helm not support");
    }

    @Override
    public Response<String> unDeployAgent(HelmRequest helmRequest) {
        throw new UnsupportedOperationException("helm not support");
    }

    @Override
    public Response<String> deployTools(HelmRequest helmRequest) {

        String command = String.format("helm install %s %s %s",
                helmRequest.getName(),
                helmRequest.getToolsName(),
                helmRequest.getCommandOptions());

        Process process = RuntimeUtil.exec(command);

        try {
            process.waitFor();
            if (process.getErrorStream().available() > 0) {
                return Response.ofFail(IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
            } else {
                String result = IoUtil.read(process.getInputStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
                if (result.startsWith("Error:")) {
                    return Response.ofFail(result);
                } else {
                    return Response.ofSuccess(result);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    @Override
    public Response<String> unDeployTools(HelmRequest helmRequest) {

        Process process = RuntimeUtil.exec("helm uninstall " + helmRequest.getName());

        try {
            process.waitFor();
            if (process.getErrorStream().available() > 0) {
                return Response.ofFail(IoUtil.read(process.getErrorStream(), SystemPropertiesUtils.getPropertiesFileEncoding()));
            } else {
                String result = IoUtil.read(process.getInputStream(), SystemPropertiesUtils.getPropertiesFileEncoding());
                if (result.startsWith("Error:")) {
                    return Response.ofFail(result);
                } else {
                    return Response.ofSuccess(result);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
    }

    public Response<String> getHelmValues(HelmRequest helmRequest) {
        try {
            String repoTgz = String.format("%s/charts/%s-%s.tgz", helmRepoUrl, helmRequest.getToolsName(), helmRequest.getToolsVersion());
            HttpRequest request = HttpUtil.createGet(repoTgz);
            HttpResponse execute = request.execute();

            CompressorInputStream in = new GzipCompressorInputStream(execute.bodyStream(), true);
            TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(in);
            TarArchiveEntry entry;
            while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith("values.yaml")) {
                    String s = IoUtil.readUtf8(tarArchiveInputStream);
                    return Response.ofSuccess(s);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.ofFail(e.getMessage());
        }
        return Response.ofFail("not found");
    }
}

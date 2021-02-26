/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.platform.service.probes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.AgentType;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.cmmon.utils.InetUtils;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.ProbesDO;
import com.alibaba.chaosblade.platform.dao.page.PageUtils;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.dao.repository.ProbesRepository;
import com.alibaba.chaosblade.platform.service.ToolsService;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsOverview;
import com.alibaba.chaosblade.platform.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.platform.scenario.api.model.ToolsVersion;
import com.alibaba.chaosblade.platform.service.probes.heartbeats.Heartbeats;
import com.alibaba.chaosblade.platform.service.probes.model.InstallProbesRequest;
import com.alibaba.chaosblade.platform.service.probes.model.ProbesRequest;
import com.alibaba.chaosblade.platform.service.probes.model.ProbesResponse;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChannelType;
import com.alibaba.chaosblade.platform.toolsmgr.api.ChaosToolsMgrStrategyContext;
import com.alibaba.chaosblade.platform.toolsmgr.api.Request;
import com.alibaba.chaosblade.platform.toolsmgr.api.Response;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.platform.cmmon.enums.DeviceStatus.UNINSTALLING;
import static com.alibaba.chaosblade.platform.cmmon.enums.DeviceStatus.UNINSTALL_FAIL;
import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.PROBES_UNINSTALL_FAIL;

/**
 * @author yefei
 */
@Service
public class ProbesServiceImpl implements ProbesService, InitializingBean {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ProbesRepository probesRepository;

    @Autowired
    private Heartbeats heartbeats;

    @Value("${server.port}")
    private String serverPort;

    @Value("${chaos.agent.release}")
    private String release;

    @Autowired
    private ToolsService toolsService;

    private ExecutorService executorService;

    @Autowired
    private ChaosToolsMgrStrategyContext chaosToolsMgrStrategyContext;

    @Override
    public void afterPropertiesSet() {
        executorService = new ThreadPoolExecutor(
                0,
                512,
                120L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactory() {
                    final AtomicInteger atomicInteger = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setDaemon(false);
                        thread.setName("PROBES-INSTALL-THREAD-" + atomicInteger.getAndIncrement());
                        return thread;
                    }
                }
        );
    }

    @Override
    public List<ProbesResponse> getAnsibleHosts() {

        Response<List<DeviceMeta>> listHosts = chaosToolsMgrStrategyContext.listHosts(Request.builder()
                .channel(ChannelType.ANSIBLE.name()).build());

        List<String> hosts = listHosts.getResult().stream().map(DeviceMeta::getIp).collect(Collectors.toList());

        List<ProbesDO> probesDOS = probesRepository.selectByHosts(hosts);
        Map<String, ProbesDO> map = probesDOS.stream().collect(Collectors.toMap(ProbesDO::getIp, u -> u));

        return hosts.stream().map(host -> ProbesResponse.builder()
                .host(host)
                .status(Optional.ofNullable(map.get(host)).map(ProbesDO::getStatus).orElse(UNINSTALLING.getStatus()))
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<ProbesResponse> getAnsibleHostsRegister(ProbesRequest probesRequest) {
        List<String> hosts = probesRequest.getHosts();
        List<ProbesDO> probesDOS = probesRepository.selectByHosts(hosts);
        return probesDOS.stream().map(probesDO -> ProbesResponse.builder()
                .probeId(probesDO.getId())
                .deviceId(probesDO.getDeviceId())
                .ip(probesDO.getIp())
                .host(probesDO.getIp())
                .status(probesDO.getStatus())
                .success(probesDO.getSuccess())
                .error(probesDO.getErrorMessage())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<ProbesResponse> getProbesPageable(ProbesRequest probesRequest) {
        List<ProbesDO> probesDOS = probesRepository.selectList(ProbesDO.builder()
                .hostname(probesRequest.getHostname())
                .ip(probesRequest.getIp())
                .status(probesRequest.getStatus())
                .agentType(probesRequest.getAgentType())
                .build());

        PageUtils.startPage(probesRequest);
        return probesDOS.stream().map(probesDO -> ProbesResponse.builder()
                .probeId(probesDO.getId())
                .deviceId(probesDO.getDeviceId())
                .hostname(probesDO.getHostname())
                .ip(probesDO.getIp())
                .createTime(probesDO.getGmtCreate())
                .modifyTime(probesDO.getGmtModified())
                .heartbeatTime(probesDO.getLastOnlineTime())
                .status(probesDO.getStatus())
                .version(probesDO.getVersion())
                .agentType(probesDO.getAgentType())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProbesResponse banProbe(ProbesRequest probesRequest) {
        ProbesDO probesDO = probesRepository.selectById(probesRequest.getProbeId())
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.PROBES_NO_FOUND));

        probesRepository.updateByPrimaryKey(probesRequest.getProbeId(), ProbesDO.builder()
                .status(DeviceStatus.FORBIDDEN.getStatus())
                .build());

        deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                .status(DeviceStatus.FORBIDDEN.getStatus()).build());

        return ProbesResponse.builder()
                .probeId(probesDO.getId())
                .deviceId(probesDO.getDeviceId())
                .hostname(probesDO.getHostname())
                .ip(probesDO.getIp())
                .createTime(probesDO.getGmtCreate())
                .heartbeatTime(probesDO.getLastOnlineTime())
                .status(DeviceStatus.FORBIDDEN.getStatus())
                .error(probesDO.getErrorMessage())
                .version(probesDO.getVersion())
                .agentType(probesDO.getAgentType())
                .build();
    }

    @Override
    @Transactional
    public ProbesResponse unbanProbe(ProbesRequest probesRequest) {
        ProbesDO probesDO = probesRepository.selectById(probesRequest.getProbeId())
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.PROBES_NO_FOUND));

        if (DateUtil.date().offset(DateField.MINUTE, -1).after(probesDO.getLastOnlineTime())) {
            probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                    .status(DeviceStatus.OFFLINE.getStatus())
                    .build());

            deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                    .status(DeviceStatus.OFFLINE.getStatus()).build());
        } else {
            probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                    .status(DeviceStatus.ONLINE.getStatus())
                    .build());

            deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                    .status(DeviceStatus.ONLINE.getStatus()).build());
        }

        probesDO = probesRepository.selectById(probesRequest.getProbeId())
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.PROBES_NO_FOUND));
        return ProbesResponse.builder()
                .probeId(probesDO.getId())
                .deviceId(probesDO.getDeviceId())
                .hostname(probesDO.getHostname())
                .ip(probesDO.getIp())
                .createTime(probesDO.getGmtCreate())
                .heartbeatTime(probesDO.getLastOnlineTime())
                .status(probesDO.getStatus())
                .error(probesDO.getErrorMessage())
                .version(probesDO.getVersion())
                .agentType(probesDO.getAgentType())
                .build();
    }

    @Override
    public List<ProbesResponse> installProbes(InstallProbesRequest installProbesRequest) {

        String entryPoint = InetUtils.getLocalHost() + ChaosConstant.COLON + serverPort;

        List<ProbesRequest> probes = installProbesRequest.getProbes();
        for (ProbesRequest probesRequest : probes) {
            probesRequest.setCommandOptions(String.format("-t %s -r %s %s", entryPoint, release, probesRequest.getCommandOptions()));

            List<ProbesDO> probesDOS = probesRepository.selectByStatus(probesRequest.getHost(),
                    CollUtil.newArrayList(DeviceStatus.ONLINE.getStatus()));

            if (CollUtil.isNotEmpty(probesDOS)) {
                continue;
            }

            Long id = probesRepository.selectByHost(probesRequest.getHost()).map(ProbesDO::getId)
                    .orElseGet(() -> probesRepository.insert((ProbesDO.builder()
                            .ip(probesRequest.getHost())
                            .deployBlade(installProbesRequest.isDeployBlade())
                            .agentType(AgentType.HOST.getCode())
                            .status(DeviceStatus.INSTALLING.getStatus())
                            .build())));
            probesRequest.setProbeId(id);

            executorService.execute(() -> {

                Response<String> deployAgent = chaosToolsMgrStrategyContext.deployAgent(Request.builder()
                        .host(probesRequest.getHost())
                        .probesId(id)
                        .commandOptions(probesRequest.getCommandOptions())
                        .channel(ChannelType.ANSIBLE.name()).build());

                if (!deployAgent.isSuccess()) {
                    probesRepository.updateByPrimaryKey(id, ProbesDO.builder()
                            .ip(probesRequest.getHost())
                            .status(DeviceStatus.INSTALL_FAIL.getStatus())
                            .errorMessage(deployAgent.getMessage())
                            .build());
                }
            });

        }

        return probesRepository.selectByIds(probes.stream().map(ProbesRequest::getProbeId).collect(Collectors.toList()))
                .stream().map(probesDO -> {
                    ProbesResponse probesResponse = ProbesResponse.builder()
                            .probeId(probesDO.getId())
                            .deviceId(probesDO.getDeviceId())
                            .hostname(probesDO.getHostname())
                            .ip(probesDO.getIp())
                            .createTime(probesDO.getGmtCreate())
                            .modifyTime(probesDO.getGmtModified())
                            .status(probesDO.getStatus())
                            .error(probesDO.getErrorMessage())
                            .version(probesDO.getVersion())
                            .agentType(probesDO.getAgentType())
                            .build();
                    heartbeats.addHeartbeats(probesDO);
                    return probesResponse;
                }).collect(Collectors.toList());
    }

    @EventListener
    public void listenProbesInstallSuccess(ProbesInstallSuccessEvent event) {
        executorService.execute(() -> {
            Long deviceId = (Long) event.getSource();
            Optional<ProbesDO> probesDO = probesRepository.selectByDeviceId(deviceId);
            probesDO.ifPresent(probes -> {
                if (probes.getDeployBlade()) {
                    ToolsOverview toolsOverview = toolsService.toolsOverview(ChaosConstant.DEFAULT_TOOLS);
                    ToolsVersion toolsVersion = toolsService.toolsVersion(toolsOverview.getName(), toolsOverview.getLatest());

                    ToolsRequest toolsRequest = ToolsRequest.builder()
                            .machineId(deviceId)
                            .name(toolsOverview.getName())
                            .version(toolsOverview.getLatest())
                            .url(toolsVersion.getDownloadUrl().get(0).getLinux_x86_64()).build();

                    try {
                        toolsService.deployChaostoolsToHost(toolsRequest);
                    } catch (BizException e) {
                        probesRepository.updateByDeviceId(deviceId, ProbesDO.builder()
                                .errorMessage(e.getData().toString())
                                .build());
                    }
                }
            });
        });
    }

    @Override
    public List<ProbesResponse> queryProbesInstallation(ProbesRequest probesRequest) {

        return probesRepository.selectByIds(CollUtil.newArrayList(probesRequest.getProbeIds()))
                .stream().map(probesDO ->
                        ProbesResponse.builder()
                                .probeId(probesDO.getId())
                                .deviceId(probesDO.getDeviceId())
                                .hostname(probesDO.getHostname())
                                .ip(probesDO.getIp())
                                .createTime(probesDO.getGmtCreate())
                                .modifyTime(probesDO.getGmtModified())
                                .status(probesDO.getStatus())
                                .error(probesDO.getErrorMessage())
                                .version(probesDO.getVersion())
                                .agentType(probesDO.getAgentType())
                                .build()
                ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProbesResponse uninstallProbe(InstallProbesRequest installProbesRequest) {
        ProbesDO probesDO = probesRepository.selectById(installProbesRequest.getProbeId())
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.PROBES_NO_FOUND));

        probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                .status(UNINSTALLING.getStatus())
                .build());

        Response<String> deployAgent = chaosToolsMgrStrategyContext.unDeployAgent(Request.builder()
                .host(probesDO.getIp())
                .channel(ChannelType.ANSIBLE.name()).build());

        if (!deployAgent.isSuccess()) {
            ProbesDO probes = ProbesDO.builder()
                    .status(UNINSTALL_FAIL.getStatus())
                    .errorMessage(deployAgent.getMessage())
                    .build();
            probesRepository.updateByPrimaryKey(probesDO.getId(), probes);

            throw new BizException(PROBES_UNINSTALL_FAIL, deployAgent.getMessage());
        } else {
            deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                    .status(DeviceStatus.WAIT_INSTALL.getStatus()).build());

            probesRepository.deleteById(probesDO.getId());
        }

        return ProbesResponse.builder().probeId(probesDO.getId()).build();
    }
}

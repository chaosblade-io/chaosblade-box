package com.alibaba.chaosblade.box.service.command.agent.kubernetes;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.service.command.scope.PrivateScope;
import com.alibaba.chaosblade.box.service.infrastructure.service.PodController;
import com.alibaba.chaosblade.box.service.model.agent.K8sPodsMetricRequest;
import com.alibaba.chaosblade.box.service.model.cluster.CacheRefreshDTO;
import com.alibaba.chaosblade.box.service.model.cluster.DeviceRealDTO;
import com.alibaba.chaosblade.box.service.model.cluster.IdentifierCollection;
import com.alibaba.chaosblade.box.service.model.cluster.KubPodExtInfoDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author: xinyuan.ymm
 * @create: 2019-07-03 10:51 AM
 */
@Service
public class PodReportHandler {
    @Autowired
    private PodController podController;

    private static final Logger LOGGER = LoggerFactory.getLogger(PodReportHandler.class);


    public Response<Map<String, String>> handler(K8sPodsMetricRequest k8sPodsMetricRequest, DeviceDO deviceDO) {
        try {
            Map<String, String> result = handlePodInfo(k8sPodsMetricRequest.getPods(), deviceDO);
            return Response.ofSuccess(result);
        } catch (RejectedExecutionException rejectedExecutionException) {
            return Response.ofFailure(Response.Code.FORBIDDEN, "server resource controller pool is full");
        }
    }

    public Map<String, String> handlePodInfo(String podsJson, DeviceDO hostDeviceDO) {
        if (StringUtils.isNotBlank(podsJson)) {
            String userId = hostDeviceDO.getUserId();
            String namespace = hostDeviceDO.getNamespace();
            Map<String, String> result = new HashMap<>(16);
            JSONArray jsonArray = JSON.parseArray(podsJson);
            int podSize = jsonArray.size();
            //待新增或者更新的POD信息
            List<DeviceRealDTO> waitInsertOrUpdateDeviceDOs = new ArrayList<>();
            //待删除的Pod ID
            IdentifierCollection waitDeleteCollection = IdentifierCollection.getInstance();
            //待更新心跳的POD
            Set<String> waitUpdateHeartBeatDeviceSet = new HashSet<>(8);
            //待更新缓存的设备数据
            List<CacheRefreshDTO> waitRefreshCacheDeviceList = new ArrayList<>();

            for (int i = 0; i < podSize; i++) {
                boolean needUpdateRichDevice = false;
                boolean needUpdateHeartbeatDevice = false;
                boolean needRemoveDevice = false;
                JSONObject itemJson = jsonArray.getJSONObject(i);
                String podUid = getLimitLengthField(itemJson.getString("uid"), 127);
                String podName = getLimitLengthField(itemJson.getString("name"), 127);
                Boolean exist = itemJson.getBoolean("exist");
                String podConfigurationId = itemJson.getString("cid");
                if (exist != null && StringUtils.isNotBlank(podConfigurationId)) {
                    if (!exist) {
                        //删除节点
                        needRemoveDevice = true;
                        waitDeleteCollection.add(podConfigurationId, hostDeviceDO.getHostConfigurationId());
                    } else if (StringUtils.isBlank(podName)) {
                        //更新心跳
                        needUpdateHeartbeatDevice = true;
                        waitUpdateHeartBeatDeviceSet.add(podConfigurationId);
                        result.put(podUid, podConfigurationId);
                    } else {
                        //更新节点
                        needUpdateRichDevice = true;
                    }

                }

                //只同步数据变化的设备数据，包括更新和删除，老版本agent不处理
                if (needRemoveDevice) {
                    CacheRefreshDTO podDeviceRefreshCache = new CacheRefreshDTO();
                    podDeviceRefreshCache.setUserId(hostDeviceDO.getUserId());
                    podDeviceRefreshCache.setResourceType("Pod");
                    podDeviceRefreshCache.setOperation("DELETE");
                    podDeviceRefreshCache.setConfigurationId(podConfigurationId);
                    waitRefreshCacheDeviceList.add(podDeviceRefreshCache);
                }

                if (needUpdateRichDevice) {
                    CacheRefreshDTO podDeviceRefreshCache = new CacheRefreshDTO();
                    podDeviceRefreshCache.setUserId(hostDeviceDO.getUserId());
                    podDeviceRefreshCache.setResourceType("Pod");
                    podDeviceRefreshCache.setOperation("RELOAD");
                    podDeviceRefreshCache.setConfigurationId(podConfigurationId);
                    waitRefreshCacheDeviceList.add(podDeviceRefreshCache);
                }

                if (needRemoveDevice || needUpdateHeartbeatDevice) {
                    continue;
                }

                KubPodExtInfoDTO kubPodExtInfoDO = buildKubPodExtInfoDO(podUid, podName, itemJson, hostDeviceDO);

                //pod device
                DeviceRealDTO podDeviceDTO = new DeviceRealDTO();
                podDeviceDTO.setUserId(hostDeviceDO.getUserId());
                podDeviceDTO.setEnvironment(namespace);
                podDeviceDTO.setVpcId(hostDeviceDO.getVpcId());
                podDeviceDTO.setEnable(true);
                podDeviceDTO.setClusterId(hostDeviceDO.getClusterId());
                podDeviceDTO.setClusterName(hostDeviceDO.getClusterName());
                podDeviceDTO.setClusterNamespace(kubPodExtInfoDO.getKubNamespace());

                podDeviceDTO.setVersion(hostDeviceDO.getVersion());
                podDeviceDTO.setPrivateIp(kubPodExtInfoDO.getIp());
                String parentIps = Joiner.on(",").skipNulls().join(
                    Lists.newArrayList(hostDeviceDO.getPrivateIp(), hostDeviceDO.getPublicIp()));
                podDeviceDTO.setParentIp(parentIps);
                podDeviceDTO.setParentConfigurationId(hostDeviceDO.getConfigurationId());
                podDeviceDTO.setParentDeviceType(hostDeviceDO.getDeviceType());
                podDeviceDTO.setParentDeviceName(hostDeviceDO.getDeviceName());
                podDeviceDTO.setParentDeviceSpace(kubPodExtInfoDO.getKubNamespace());
                podDeviceDTO.setHostConfigurationId(hostDeviceDO.getConfigurationId());
                podDeviceDTO.setHostInstanceId(hostDeviceDO.getHostInstanceId());
                podDeviceDTO.setUptime(kubPodExtInfoDO.getUptime());
                podDeviceDTO.setInstallMode(hostDeviceDO.getInstallMode());
                podDeviceDTO.setOsVersion("");
                podDeviceDTO.setHostname("");

                if ("running".equalsIgnoreCase(kubPodExtInfoDO.getState())) {
                    podDeviceDTO.setStatus(DeviceStatus.ONLINE.getStatus());
                } else {
                    podDeviceDTO.setStatus(DeviceStatus.OFFLINE.getStatus());
                }
                podDeviceDTO.setState(kubPodExtInfoDO.getState());
                podDeviceDTO.setDeviceType(DeviceType.POD.getType());
                podDeviceDTO.setDeviceId(kubPodExtInfoDO.getUid());
                podDeviceDTO.setDeviceName(kubPodExtInfoDO.getName());
                podDeviceDTO.setLeaseTime(System.currentTimeMillis());
                podDeviceDTO.setConfigurationId(kubPodExtInfoDO.getConfigurationId());
                podDeviceDTO.setDeploymentConfigurationId(kubPodExtInfoDO.getDeploymentCid());
                podDeviceDTO.setReplicaSetConfigurationId(kubPodExtInfoDO.getReplicaSetCid());
                podDeviceDTO.setServiceConfigurationId(kubPodExtInfoDO.getServiceCid());
                podDeviceDTO.setDaemonSetConfigurationId(kubPodExtInfoDO.getDaemonSetCid());

                podDeviceDTO.setExtInfo(JSON.toJSONString(kubPodExtInfoDO));
                podDeviceDTO.setMd5(kubPodExtInfoDO.getMd5());
                waitInsertOrUpdateDeviceDOs.add(podDeviceDTO);
                result.put(podUid, kubPodExtInfoDO.getConfigurationId());

            }

            podController.insertOrUpdate(userId, hostDeviceDO.getNamespace(), waitInsertOrUpdateDeviceDOs);

            podController.lease(userId, hostDeviceDO.getVpcId(), namespace, hostDeviceDO.getClusterId(),
                    hostDeviceDO.getClusterName(), hostDeviceDO.getConfigurationId(), waitUpdateHeartBeatDeviceSet);
            if (waitDeleteCollection != null && CollectionUtils.isNotEmpty(waitDeleteCollection.getIdentifierList())) {
                waitDeleteCollection.getIdentifierList().forEach(item -> podController.delete(userId, hostDeviceDO.getNamespace(), item));
            }
            podController.refresh(userId,
                    waitRefreshCacheDeviceList);
            return result;
        }
        return null;
    }

    /**
     * 构建pod ext info
     *
     * @param podUid
     * @param podName
     * @param itemJson
     * @param hostDeviceDO
     * @return
     */
    private KubPodExtInfoDTO buildKubPodExtInfoDO(String podUid, String podName, JSONObject itemJson,
        DeviceDO hostDeviceDO) {
        String userId = hostDeviceDO.getUserId();
        String namespace = hostDeviceDO.getNamespace();
        KubPodExtInfoDTO kubPodInfoDTO = new KubPodExtInfoDTO();
        kubPodInfoDTO.setUserId(userId);
        kubPodInfoDTO.setNamespace(namespace);
        kubPodInfoDTO.setClusterId(hostDeviceDO.getClusterId());
        kubPodInfoDTO.setClusterName(hostDeviceDO.getClusterName());
        kubPodInfoDTO.setUid(podUid);
        kubPodInfoDTO.setName(podName);
        kubPodInfoDTO.setUptime(getLimitLengthField(itemJson.getString("createdTime"), 63));
        kubPodInfoDTO.setLabels(itemJson.getString("labels"));
        kubPodInfoDTO.setKubNamespace(getLimitLengthField(itemJson.getString("namespace"), 127));
        kubPodInfoDTO.setRestartCount(itemJson.getInteger("restartCount"));
        kubPodInfoDTO.setState(getLimitLengthField(itemJson.getString("state"), 31));
        kubPodInfoDTO.setDaemonSetUid(getLimitLengthField(itemJson.getString("daemonsetUid"), 127));
        kubPodInfoDTO.setDaemonSetCid(getLimitLengthField(itemJson.getString("daemonsetCid"), 127));
        kubPodInfoDTO.setServiceUid(getLimitLengthField(itemJson.getString("serviceUid"), 127));
        kubPodInfoDTO.setServiceCid(getLimitLengthField(itemJson.getString("serviceCid"), 127));
        kubPodInfoDTO.setReplicaSetUid(getLimitLengthField(itemJson.getString("replicasetUid"), 127));
        kubPodInfoDTO.setReplicaSetCid(getLimitLengthField(itemJson.getString("replicasetCid"), 127));
        kubPodInfoDTO.setDeploymentUid(getLimitLengthField(itemJson.getString("deploymentUid"), 127));
        kubPodInfoDTO.setDeploymentCid(getLimitLengthField(itemJson.getString("deploymentCid"), 127));

        kubPodInfoDTO.setHostConfigurationId(hostDeviceDO.getConfigurationId());
        String ip = getLimitLengthField(itemJson.getString("ip"), 127);
        if (StringUtils.isBlank(ip)) {
            ip = "0.0.0.0";
        }
        kubPodInfoDTO.setIp(ip);

        if (StringUtils.isNotBlank(kubPodInfoDTO.getDaemonSetUid()) && StringUtils.isBlank(
            kubPodInfoDTO.getDaemonSetCid())) {
            kubPodInfoDTO.setDaemonSetCid(PrivateScope
                .generatorKubernetesConfigurationId(userId, namespace, hostDeviceDO.getVpcId(),
                    kubPodInfoDTO.getDaemonSetUid()));
        }
        if (StringUtils.isNotBlank(kubPodInfoDTO.getDeploymentUid()) && StringUtils.isBlank(
            kubPodInfoDTO.getDeploymentCid())) {
            kubPodInfoDTO.setDeploymentCid(PrivateScope
                .generatorKubernetesConfigurationId(userId, namespace, hostDeviceDO.getVpcId(),
                    kubPodInfoDTO.getDeploymentUid()));
        }
        if (StringUtils.isNotBlank(kubPodInfoDTO.getReplicaSetUid()) && StringUtils.isBlank(
            kubPodInfoDTO.getReplicaSetCid())) {
            kubPodInfoDTO.setReplicaSetCid(PrivateScope
                .generatorKubernetesConfigurationId(userId, namespace, hostDeviceDO.getVpcId(),
                    kubPodInfoDTO.getReplicaSetUid()));
        }
        if (StringUtils.isNotBlank(kubPodInfoDTO.getServiceUid()) && StringUtils.isBlank(
            kubPodInfoDTO.getServiceCid())) {
            kubPodInfoDTO.setServiceCid(PrivateScope
                .generatorKubernetesConfigurationId(userId, namespace, hostDeviceDO.getVpcId(),
                    kubPodInfoDTO.getServiceUid()));
        }

        kubPodInfoDTO.setConfigurationId(PrivateScope
            .generatorKubernetesConfigurationId(userId, namespace, hostDeviceDO.getVpcId(),
                kubPodInfoDTO.getUid()));

        kubPodInfoDTO.setMd5(Hashing.md5().newHasher().putString(kubPodInfoDTO.toString(), Charsets.UTF_8)
            .hash().toString());
        return kubPodInfoDTO;
    }

    private static String getLimitLengthField(String content, int maxLength) {
        if (StringUtils.isNotBlank(content) && content.length() > maxLength) {
            return content.substring(0, maxLength);
        }

        return content;
    }
}
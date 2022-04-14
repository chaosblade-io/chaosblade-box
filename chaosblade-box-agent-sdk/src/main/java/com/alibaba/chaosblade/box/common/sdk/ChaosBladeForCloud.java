package com.alibaba.chaosblade.box.common.sdk;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.constant.Blade;
import com.alibaba.chaosblade.box.common.sdk.constant.Target;
import com.alibaba.chaosblade.box.common.sdk.entity.*;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.chaosblade.box.common.sdk.util.CliUtil;
import com.alibaba.chaosblade.box.common.sdk.util.RequestUtil;
import com.alibaba.chaosblade.box.common.sdk.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Changjun Xiao
 */
public class ChaosBladeForCloud extends ChaosBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChaosBladeForCloud.class);
    private PaasTransportService transportService;

    private RequestIdProvider requestIdProvider;

    public ChaosBladeForCloud(PaasTransportService transportService, RequestIdProvider requestIdProvider) {
        this.transportService = transportService;
        this.requestIdProvider = requestIdProvider;
    }

    public ChaosBladeForCloud(PaasTransportService transportService) {
        this.transportService = transportService;
        requestIdProvider = new RequestIdProvider() {
            @Override
            public String provide() {
                return UUID.randomUUID().toString();
            }
        };
    }

    public PaasTransportService getTransportService() {
        return transportService;
    }

    /**
     * 实验准备
     *
     * @param prepareArgs
     * @param ak
     * @param sk
     * @param vpcId       用户机器 vcpId
     * @param tag         机器 tag
     * @return
     */
    public Response<String> prepareExpForCloud(PrepareArgs prepareArgs, String ak, String sk, String vpcId,
                                               String tag) {
        String command = CliUtil.buildPrepareCmd(prepareArgs);
        Request request = RequestUtil.createRequest(prepareArgs.getMachine(), prepareArgs.getPort(), prepareArgs.getMachineType(), ak, sk,
                command);
        return transportService.invoke(request, String.class);
    }

    /**
     * 创建混沌实验
     *
     * @param modelArgs
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<String> createExpForCloud(ModelArgs modelArgs, String ak, String sk, String vpcId, String tag) {
        String command = CliUtil.buildExpCmd(Blade.CREATE, modelArgs);
        Request request = RequestUtil.createRequest(modelArgs.getMachine(), modelArgs.getPort(), modelArgs.getMachineType(), ak, sk,
                command);
        return transportService.invoke(request, String.class);
    }

    /**
     * 停止混沌实验
     *
     * @param expUid
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<String> destroyExpForCloud(String expUid, String scope, String port, String ak, String sk, String vpcId, String
            tag) {
        String destroyCmd = CliUtil.buildDestroyCmd(expUid);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, destroyCmd);
        return transportService.invoke(request, String.class);
    }

    /**
     * 停止实验，兜底方案 modelArgs 中 machine、machineType、target、action 是必须的，matcherFlags 取决于 FlagSpecBean#requiredWhenDestroyed
     * 字段，true 则必须填写；
     *
     * @param modelArgs
     * @param ak
     * @param sk
     * @return
     */
    public Response<String> destroyExpForCloudWithoutUid(ModelArgs modelArgs, String ak, String sk, String vpcId,
                                                         String tag) {
        String destroyCmd = CliUtil.buildDestroyCmdWithoutUid(modelArgs);
        Request request = RequestUtil.createRequest(modelArgs.getMachine(), modelArgs.getPort(), modelArgs.getMachineType(), ak, sk,
                destroyCmd);
        return transportService.invoke(request, String.class);
    }

    /**
     * 创建k8s混沌实验
     *
     * @param modelArgs
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<K8sResultBean> createK8sExpForCloud(ModelArgs modelArgs, String ak, String sk, String vpcId,
                                                        String tag) {
        modelArgs.getFlags().put("waiting-time", "0.5s");
        String command = CliUtil.buildExpCmd(Blade.CREATE, modelArgs);
        Request request = RequestUtil.createRequest(modelArgs.getMachine(), modelArgs.getPort(), modelArgs.getMachineType(), ak, sk,
                command);
        Response<String> response = transportService.invoke(request, String.class);
        return decodeResponse(response.getResult(), response);
    }

    /**
     * 停止k8s混沌实验
     *
     * @param expUid
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<K8sResultBean> destroyK8sExpForCloud(String expUid, String scope, String port, String ak, String sk, String
            vpcId, String tag) {
        String destroyCmd = CliUtil.buildDestroyCmd(expUid);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, destroyCmd);
        Response<String> response = transportService.invoke(request, String.class);
        return decodeResponse(expUid, response);
    }

    public Response<K8sResultBean> decodeResponse(String expUid, Response<String> response) {
        Response<K8sResultBean> result = new Response<K8sResultBean>();
        result.setCode(response.getCode());
        result.setError(response.getError());
        result.setSuccess(response.isSuccess());
        K8sResultBean bean;
        try {
            bean = JSON.parseObject(response.getResult(), K8sResultBean.class);
        } catch (Exception e) {
            LOGGER.warn("decode result {} failed when destroy k8s experiment", result.getResult());
            bean = new K8sResultBean(expUid, response.isSuccess(), "", Collections.EMPTY_LIST);
        }
        result.setResult(bean);
        return result;
    }

    /**
     * 查询 k8s 实验状态
     *
     * @param expUid
     * @param cmd    {@link CliUtil#CREATE} | {@link CliUtil#DESTROY}
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<K8sResultBean> queryK8sExpForCloud(String expUid, String cmd, String scope, String port, String ak, String sk,
                                                       String vpcId, String tag) {
        String queryCmd = CliUtil.buildQueryCmd("k8s", cmd + " " + expUid);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, queryCmd);
        return transportService.invoke(request, K8sResultBean.class);
    }

    /**
     * 撤销混沌实验准备
     *
     * @param expUid
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<String> revokeExpForCloud(String expUid, String scope, String port, String ak, String sk, String vpcId, String
            tag) {
        String destroyCmd = CliUtil.buildRevokeCmd(expUid);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, destroyCmd);
        return transportService.invoke(request, String.class);
    }

    /**
     * 查询演练和挂载状态
     *
     * @param expUid 混沌实验唯一标识
     * @param scope  混沌实验执行主机
     * @param ak     混沌实验调用所需 ak
     * @param sk     混沌实验所需 sk
     * @return
     */
    public Response<ExpStatusBean> getExpStatusForCloud(String expUid, String scope, String port, String ak, String sk, String vpcId,
                                                        String tag) {
        if (StringUtil.isBlank(expUid)) {
            return Response.ofFailure(Response.Code.FORBIDDEN, "uid is empty");
        }
        String statusCmd = CliUtil.buildStatusCmd(expUid, Blade.CREATE);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, statusCmd);
        return transportService.invoke(request, ExpStatusBean.class);
    }

    /**
     * 获取全部的实验
     *
     * @param scope
     * @param ak
     * @param sk
     * @return
     */
    public Response<List<ExpStatusBean>> getExpStatusListForCloud(String scope, String port, String ak, String sk, String vpcId,
                                                                  String tag) {
        String statusCmd = CliUtil.buildStatusCmd(null, Blade.CREATE);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, statusCmd);
        Response<String> response = transportService.invoke(request, String.class);
        if (response.isSuccess()) {
            List<ExpStatusBean> expStatusBeans = JSONObject.parseArray(response.getResult(), ExpStatusBean.class);
            return Response.ofSuccess(expStatusBeans);
        } else {
            return new Response(response.getRequestId(), response.getCode(), response.isSuccess(), Collections
                    .EMPTY_LIST, response.getError());
        }
    }

    /**
     * 获取全部的实验
     *
     * @param scope
     * @param ak
     * @param sk
     * @return
     */
    public Response<List<PrepareStatusBean>> getPrepareStatusListForCloud(String scope, String port, String ak, String sk,
                                                                          String vpcId,
                                                                          String tag) {
        String statusCmd = CliUtil.buildStatusCmd(null, Blade.PREPARE);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, statusCmd);
        Response<String> response = transportService.invoke(request, String.class);
        if (response.isSuccess()) {
            List<PrepareStatusBean> statusBeans = JSONObject.parseArray(response.getResult(), PrepareStatusBean.class);
            return Response.ofSuccess(statusBeans);
        } else {
            return new Response(response.getRequestId(), response.getCode(), response.isSuccess(), Collections
                    .EMPTY_LIST, response.getError());
        }
    }

    /**
     * 查询混沌实验准备状态
     *
     * @param expUid
     * @param scope
     * @param ak
     * @param sk
     * @return
     */
    public Response<PrepareStatusBean> getPrepareStatusForCloud(String expUid, String scope, String port, String ak, String sk,
                                                                String vpcId,
                                                                String tag) {
        if (StringUtil.isBlank(expUid)) {
            return Response.ofFailure(Response.Code.FORBIDDEN, "uid is empty");
        }
        String statusCmd = CliUtil.buildStatusCmd(expUid, Blade.PREPARE);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, statusCmd);
        return transportService.invoke(request, PrepareStatusBean.class);
    }

    /**
     * 查找网络设备信息
     *
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<String[]> getNetworkDeviceForCloud(String scope, String port, String ak, String sk, String
            vpcId, String tag) {
        String queryCmd = CliUtil.buildQueryCmd("network", "interface");
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, queryCmd);
        Response<String[]> response = transportService.invoke(request, String[].class);
        if (response.isSuccess()) {
            return response;
        }
        return Response.ofFailure(Response.Code.COMMAND_NOT_FOUND, "query command not found", new String[0]);
    }

    /**
     * 查找磁盘设备信息
     *
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<String[]> getDiskDeviceForCloud(String scope, String port, String ak, String sk, String
            vpcId, String tag) {
        String queryCmd = CliUtil.buildQueryCmd("disk", "mount-point");
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, queryCmd);
        Response<String[]> response = transportService.invoke(request, String[].class);
        if (response.isSuccess()) {
            return response;
        }
        return Response.ofFailure(Response.Code.COMMAND_NOT_FOUND, "query command not found", new String[0]);
    }

    /**
     * 根据实验 UID 查询 Java 实验命中次数
     *
     * @param scope
     * @param ak
     * @param sk
     * @param vpcId
     * @param tag
     * @return
     */
    public Response<Long> getHitCountInJvmForCloud(String scope, String port, String ak, String sk, String
            vpcId, String tag, String uid) {
        String queryCmd = CliUtil.buildQueryCmd("jvm", uid);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, queryCmd);
        Response<Long> response = transportService.invoke(request, Long.class);
        if (response.isSuccess()) {
            return response;
        }
        return Response.ofFailure(Response.Code.COMMAND_NOT_FOUND, "query command not found", -1L);
    }

    @Override
    public String getYamlFileName() {
        return "chaosblade.spec-cloud.yaml";
    }

    /**
     * 查询 k8s jvm 场景命中率
     * {
     * "uid": "cc8e51b50ec573cb",
     * "success": true,
     * "metrics": [
     * {
     * "id": "60852d76ec9ff489", // jvm experiment id
     * "type": "jvm",
     * "success": true,
     * "count": 100, // 累计值
     * },
     * {
     * "id": "2c4644579ff71276", // jvm experiment id
     * "type": "jvm",
     * "success": false,
     * "error": "the experiment record not found",
     * "count": 50, // 累计值
     * },
     * ]
     * }
     *
     * @param scope 仅支持 pod/container
     * @param ak    access key
     * @param sk    secret key
     * @param vpcId user vpc id
     * @param tag   agent tag
     * @param uid   k8s experiment id
     * @return {@link K8sHitCountBean}
     */
    public Response<K8sHitCountBean> getHitCountInK8sJvmForCloud(String scope, String port, String ak, String sk, String vpcId,
                                                                 String tag, String uid) {
        String queryCmd = CliUtil.buildQueryCmd("k8s", "jvm " + uid);
        Request request = RequestUtil.createRequest(scope, port, Target.IP, ak, sk, queryCmd);
        return transportService.invoke(request, K8sHitCountBean.class);
    }

    /**
     * 检测 混沌实验
     */
    public Response<String> checkForCloud(ModelArgs modelArgs, String ak, String sk, String vpcId, String tag,
                                          String environment) {
        String command = CliUtil.buildCheckCmd(Blade.CHECK, modelArgs, environment);
        if (command.equals("")) {
            return Response.ofSuccess("success");
        }

        Request request = RequestUtil.createRequest(modelArgs.getMachine(), modelArgs.getPort(), modelArgs.getMachineType(), ak, sk,
                command);
        return transportService.invoke(request, String.class);
    }

    public Response<String> uninstallAgent(String scope, String port) {

        Request request = RequestUtil.createRequest(scope, port,"uninstall", Target.IP);
        return transportService.invoke(request, String.class);
    }

    public Response<String> pingAgent(String scope, String port) {
        Request request = RequestUtil.createRequest(scope, port,"ping", Target.IP);
        return transportService.invoke(request, String.class);
    }
}


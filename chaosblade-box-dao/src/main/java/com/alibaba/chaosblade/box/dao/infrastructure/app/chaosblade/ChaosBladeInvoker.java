package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.*;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.sdk.entity.*;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface ChaosBladeInvoker {

    public static String STATUS_CREATED = "Created";

    public static String STATUS_RUNNING = "Running";

    public static String STATUS_ERROR = "Error";

    public static String STATUS_REVOKED = "Revoked";

    public static String TimeOutError = "an hsf timeout exception happens while gateway";

    public static String ExecutionError = "but the real status: `Error`";

    public static String StillRunningError = "please wait";

    public static String InitializedError = "the real value is Initialized";

    public static String RealValueRunningError = "expected destroyed, but the real value is Running";

    default ModelArgs buildModelArgs(ChaosAppRequest chaosAppRequest, ChaosBladeAction chaosBladeAction) {
        ModelArgs modelArgs = new ModelArgs();
        modelArgs.setTarget(chaosBladeAction.getTarget())
            .setSubTarget(chaosBladeAction.getSubTarget())
            .setScope(getScope(chaosBladeAction.getTarget()))
            .setAction(chaosBladeAction.getAction()).setFlags(chaosAppRequest.getAction())
            .setMatcherFlags(chaosAppRequest.getMatcher())
            .setMachine(chaosAppRequest.getScope().getIp());
        return modelArgs;
    }

    default String getScope(String target) {
        String[] splits = target.split("-");
        if (splits.length == 2) {
            return splits[0];
        }
        return null;
    }

    default PrepareArgs buildPrepareArgs(MiniAppInvokeContext miniAppInvokeContext, ChaosBladeAction chaosBladeAction) {
        ChaosAppRequest chaosAppRequest = miniAppInvokeContext.getChaosAppRequest();
        PrepareArgs modelArgs = new PrepareArgs();
        modelArgs
            .setType(chaosBladeAction.getPrepareType())
            .setScope(getScope(chaosBladeAction.getTarget()))
            .setFlags(chaosAppRequest.getAction())
            .setMachine(chaosAppRequest.getScope().getIp());
        return modelArgs;

    }

    /**
     * 获取混沌实验操作定义，如果存在，则返回，否则解析场景文件
     * 读取默认 oss 上的场景文件
     * 如果默认的 oss 场景文件读取失败，则读取 SDK 中的场景文件
     *
     * @return
     */
    public ChaosModels getBladeModels() throws Exception;

    /**
     * 准备实验
     *
     * @param prepareExpRequest
     * @return
     */
    public Response<String> prepareExp(ChaosBladePrepareExpRequest prepareExpRequest);

    /**
     * 创建演练
     *
     * @param createExpRequest
     * @return
     */
    public Response<String> createExp(ChaosBladeCreateExpRequest createExpRequest);

    /**
     * 销毁演练
     *
     * @return
     */
    public Response<String> destroyExp(ChaosBladeDestroyExpRequest chaosBladeDestroyExpRequest);

    /**
     * 撤销演练准备
     *
     * @param chaosBladeRevokeExpRequest
     * @return
     */
    public Response<String> revokeExp(ChaosBladeRevokeExpRequest chaosBladeRevokeExpRequest);

    /**
     * @return
     */
    public Response<ExpStatusBean> getExpStatus(ChaosBladeExpQueryRequest chaosBladeExpQueryRequest);

    public Response<List<ExpStatusBean>> getExpStatusList(
        ChaosBladeHostQueryRequest chaosBladeHostQueryRequest);

    public Response<String[]> getNetworkDevice(ChaosBladeHostQueryRequest chaosBladeHostQueryRequest);

    public Response<String[]> getDiskDevice(ChaosBladeHostQueryRequest chaosBladeHostQueryRequest);

    public Response<Long> getHitCountInJvm(ChaosBladeExpQueryRequest chaosBladeExpQueryRequest);

    Response<String> getProcessIdByKey(ChaosBladeProcessQueryRequest chaosBladeProcessQueryRequest);

    Response<PrepareStatusBean> getPrepareStatus(ChaosBladeExpQueryRequest chaosBladeExpQueryRequest);

    Response<List<PrepareStatusBean>> getPrepareStatusList(ChaosBladeHostQueryRequest chaosBladeHostQueryRequest);

    Response<String[]> getDiskBlockDevice(ChaosBladeHostQueryRequest chaosBladeHostQueryRequest);

    /**
     * 销毁演练，不传Uid
     */
    Response<String> destroyExpWithoutUid(ChaosBladeAction chaosBladeAction, ChaosAppRequest chaosAppRequest, String ak,
                                          String sk);

    Response<String> destroyByChaosBladeExpDO(ChaosBladeExpUidDO chaosBladeExpUidDO);

    /**
     * 目标机器环境预校验
     *
     * @param chaosBladePreCheckRequest
     * @return
     */
    Response<String> checkForCloud(ChaosBladePreCheckRequest chaosBladePreCheckRequest);

    Response<K8sResultBean> queryK8sExp(Host host, String expId, boolean isCreate);

    Response<String> uninstallAgent(Host host);

    Response<String> pingAgent(Host host);

}

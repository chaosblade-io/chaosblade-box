package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.annotation.ChaosComponent;
import com.alibaba.chaosblade.box.common.common.constant.PublicCloudConstant;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import com.alibaba.chaosblade.box.common.sdk.LitmusChaosForCloud;
import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;
import com.alibaba.chaosblade.box.common.sdk.entity.ModelArgs;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.request.LitmusChaosRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.PluginTypeUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.ProxyHelper;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@ChaosComponent
public class PublicLitmusChaosInvoker implements LitmusChaosInvoker {

    @Autowired
    private LitmusChaosForCloud litmusChaosForCloud;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public ChaosModels getBladeModels() throws Exception {
        return litmusChaosForCloud.getChaosModels();
    }

    @Override
    public Response<String> createExp(LitmusChaosRequest litmusChaosRequest) {

        MiniAppInvokeContext appInvokeContext = litmusChaosRequest.getAppInvokeContext();
        ModelArgs modelArgs = buildModelArgs(appInvokeContext.getChaosAppRequest(), litmusChaosRequest.getChaosBladeAction());
        String ak = appInvokeContext.getActivityInvokeContext().getUserAk();
        String sk = appInvokeContext.getActivityInvokeContext().getUserSk();
        PublicCloudChaosBladeInvoker.UserIdentification userIdentification = getUserIdentification(appInvokeContext.getHost(), false, null);

        return litmusChaosForCloud.createExpForCloud(modelArgs, "default", "generic", ak, sk, userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<String> destroyExp(LitmusChaosRequest litmusChaosRequest, String expId) {

        MiniAppInvokeContext appInvokeContext = litmusChaosRequest.getAppInvokeContext();
        ModelArgs modelArgs = buildModelArgs(appInvokeContext.getChaosAppRequest(), litmusChaosRequest.getChaosBladeAction());
        String ak = appInvokeContext.getActivityInvokeContext().getUserAk();
        String sk = appInvokeContext.getActivityInvokeContext().getUserSk();
        PublicCloudChaosBladeInvoker.UserIdentification userIdentification = getUserIdentification(appInvokeContext.getHost(), false, null);
        return litmusChaosForCloud.destroyExpForCloud(modelArgs.getMachine(),modelArgs.getMachineType(),
                "default", expId, ak, sk, userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<String> destroyByChaosBladeExpDO(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(chaosBladeExpUidDO.getDeviceConfigurationId());
        if (deviceDO == null) {
            throw new RuntimeException("Not find device by configurationId:" + chaosBladeExpUidDO.getConfigurationId());
        }
        PublicCloudChaosBladeInvoker.UserIdentification userIdentification = getUserIdentification(deviceDO, chaosBladeExpUidDO);
        return litmusChaosForCloud.destroyExpForCloud(chaosBladeExpUidDO.getTargetIp(),null,
                "default", chaosBladeExpUidDO.getExpUid() , userIdentification.getAk(), userIdentification.getSk(), userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    private PublicCloudChaosBladeInvoker.UserIdentification getUserIdentification(DeviceDO deviceDO, ChaosBladeExpUidDO chaosBladeExpUidDO) {
        PublicCloudChaosBladeInvoker.UserIdentification userIdentification = new PublicCloudChaosBladeInvoker.UserIdentification();
        userIdentification.setVpcId(deviceDO.getVpcId());
        userIdentification.setCloudTag(buildCloudTag(deviceDO.getDeviceType(), deviceDO.getPrivateIp()));
        userIdentification.setAk(chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_AK));
        userIdentification.setSk(chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_SK));
        return userIdentification;
    }


    private PublicCloudChaosBladeInvoker.UserIdentification getUserIdentification(Host host, boolean reloadAkSk, String expId) {
        PublicCloudChaosBladeInvoker.UserIdentification userIdentification = new PublicCloudChaosBladeInvoker.UserIdentification();
        userIdentification.setHost(host);
        if (host.getVpcId() != null) {
            userIdentification.setVpcId(host.getVpcId());
        }
        if (host.getDeviceType() == null) {
            DeviceDO deviceDO = null;
            if (host.getDeviceConfigurationId() != null) {
                deviceDO = deviceRepository.findByConfigurationId(host.getDeviceConfigurationId());
            }
            if (deviceDO == null) {
                deviceDO = deviceRepository.findByPrivateIp(host.getTargetIp());
            }
            if (deviceDO != null) {
                userIdentification.setVpcId(deviceDO.getVpcId());
                userIdentification.setCloudTag(buildCloudTag(deviceDO.getDeviceType(), host.getTargetIp()));
            }
        } else {
            userIdentification.setCloudTag(buildCloudTag(host.getDeviceType(), host.getTargetIp()));
        }
        if (!reloadAkSk) {
            return userIdentification;
        }
        ChaosUser chaosUser = ChaosRequestContextHolder.getApplicationContext().map(ChaosApplicationContext::getLoginUser)
                .orElse(null);
        String ak = null;
        String sk = null;
        if (chaosUser != null) {
            ak = chaosUser.getUserId();
            sk = chaosUser.getLicense();
        } else {
            if (expId != null) {
                ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findLastByExpUid(expId);
                ak = chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_AK);
                sk = chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_SK);
                if (ak == null) {
                    UserDo userDo = findByExpId(chaosBladeExpUidDO);
                    ak = userDo.getUserId();
                    sk = userDo.getLicense();
                }
            }
        }
        userIdentification.setAk(ak);
        userIdentification.setSk(sk);

        return userIdentification;
    }

    private String buildCloudTag(Integer deviceType, String ip) {
        return ProxyHelper.buildProxyTag(PluginTypeUtil.getPluginTypeByDeviceType(deviceType), ip, null);
    }


    public ModelArgs buildModelArgs(ChaosAppRequest chaosAppRequest, ChaosBladeAction chaosBladeAction) {
        ModelArgs modelArgs = new ModelArgs();
        fillModelArgs(chaosAppRequest, modelArgs, chaosBladeAction);
        return modelArgs;
    }

    private UserDo findByExpId(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(chaosBladeExpUidDO.getExperimentTaskId())
                .get();
        return findByUserId(experimentTaskDO.getUserId());
    }

    private UserDo findByUserId(String userId) {
        return licenseRepository.getUserLicense(userId).get();
    }

    private void fillModelArgs(ChaosAppRequest chaosAppRequest, ModelArgs modelArgs, ChaosBladeAction chaosBladeAction) {
        Map<String, String> action = chaosAppRequest.getAction();
        Map<String, String> matcher = new HashMap<>();
        if (action != null) {
            List<String> keys = action.keySet().stream().filter(key ->
                    "appns".equals(key) || "applabel".equals(key) || "appkind".equals(key)
            ).collect(Collectors.toList());
            keys.forEach(key -> {
                matcher.put(key, action.get(key));
                action.remove(key);
            });
        }

        modelArgs.setTarget(getTarget(chaosBladeAction.getTarget()))
                .setAction(chaosBladeAction.getAction()).setFlags(chaosAppRequest.getAction())
                .setMatcherFlags(matcher)
                .setScope(getScope(chaosBladeAction.getTarget()))
                .setMachine(chaosAppRequest.getScope().getTargetIp());
    }

    public String getTarget(String target) {
        String[] splits = target.split("-");
        if (splits.length == 2) {
            return splits[1];
        }
        return target;
    }

    public String getScope(String target) {
        String[] splits = target.split("-");
        if (splits.length >= 2) {
            return splits[0];
        }
        return null;
    }
}

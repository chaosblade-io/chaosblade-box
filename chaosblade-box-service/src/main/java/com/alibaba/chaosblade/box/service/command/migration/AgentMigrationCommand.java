package com.alibaba.chaosblade.box.service.command.migration;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.MigrationProgressEnum;
import com.alibaba.chaosblade.box.common.common.enums.MigrationStateEnum;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.MigrationConfigurationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.MigrationConfigurationRepository;
import com.alibaba.chaosblade.box.service.SettingService;
import com.alibaba.chaosblade.box.service.model.agent.SettingRequest;
import com.alibaba.chaosblade.box.toolsmgr.ChaosToolsMgr;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class AgentMigrationCommand  extends SpringBeanCommand<BaseRequest, Boolean> {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Autowired
    private MigrationConfigurationRepository migrationConfigurationRepository;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ChaosToolsMgr chaosToolsMgr;

    @Value("${regionId.default}")
    private String regionId;

    @Value("${cloud.agent.version}")
    private String cloudAgentVersion;

    @Override
    public Boolean execute(BaseRequest baseRequest) {
        log.info("[Agent Migration] start!");
        String userId = baseRequest.getUserId();

        // 1. 获取所有online设备探针
        Optional<MigrationConfigurationDO> migrationConfigurationDO = migrationConfigurationRepository.findByUserId(userId);
        if (!migrationConfigurationDO.isPresent()) {
            return false;
        }

        ConcurrentHashMap migrationResult;
        migrationResult = JSON.parseObject(migrationConfigurationDO.get().getMigrationResult(), ConcurrentHashMap.class);


        List<DeviceDO> deviceDOList = deviceRepository.getAliveDevicesByUserId(userId);
        if (deviceDOList.isEmpty()){
            migrationResult.put(MigrationProgressEnum.AGENT_PROGRESS.getType(), MigrationStateEnum.SUCCESS);
            migrationConfigurationRepository.updateMigrationResultByUserId(userId, JSON.toJSONString(migrationResult));
            return true;
        }

        // 2. 遍历，将host模式的探针统一替换，而k8s模式的则跳过，并给出提示
        Boolean finish = true;
        for (DeviceDO deviceDO : deviceDOList) {
            if (!deviceDO.getInstallMode().equals("host")) {
                log.warn("[Agent Migration] device type is {}, deviceId: {}, paas", deviceDO.getInstallMode(), deviceDO.getDeviceId());
                continue;
            }

            // 2.1 卸载
            SettingRequest settingRequest = new SettingRequest();
            settingRequest.setConfigurationId(deviceDO.getConfigurationId());
            settingRequest.setNamespace(deviceDO.getNamespace());
            if (!settingService.uninstallAgentForHost("", settingRequest)) {
                log.error("[Agent Migration] uninstall community agent failed!");
                finish = false;
                continue;
            }

            // 2.2 安装线上探针
            // 2.2.1 判断是否ping通
            MgrRequest mgrRequest = buildMgrRequest(deviceDO);
            Response<String> connectionResult = chaosToolsMgr.pingConnection(mgrRequest);
            if (!connectionResult.isSuccess()){
                log.error("[Agent Migration] install cloud agent," + deviceDO.getPrivateIp()+
                        " test connection failed, err: " + connectionResult.getError());
                finish = false;
                continue;
            }

            // 2.2 get appName && appGroup
            Optional<ApplicationDeviceDO> applicationDeviceDO = applicationDeviceRepository.findById(deviceDO.getConfigurationId());
            if (!applicationDeviceDO.isPresent()) {
                log.error("[Agent Migration] get appName failed, deviceId: {}, pass", deviceDO.getDeviceId());
                continue;
            }
            ApplicationDeviceDO applicationDeviceDO1 = applicationDeviceDO.get();

            //todo if regionId =cn-public，command need `license`
            //todo regionId
            String command = String.format("wget -q http://ahasoss-%s.oss-%s-internal.aliyuncs.com/agent/prod/%s/aliyunahasctl.sh" +
                            " -O /tmp/aliyunahasctl.sh && sudo sh /tmp/aliyunahasctl.sh install -r %s -n default -p %s -g %s -v %s",
                    regionId, regionId, cloudAgentVersion, regionId, applicationDeviceDO1.getAppName(), applicationDeviceDO1.getGroupName(), cloudAgentVersion
                    );
            mgrRequest.setCommand(command);

            Response<String> installResult = chaosToolsMgr.deployAndInstallAgent(mgrRequest);
            if (!installResult.isSuccess()){
                log.error("[Agent Migration] install cloud agent," + deviceDO.getPrivateIp()+
                        " failed, err: " + connectionResult.getError());
                finish = false;
            }
            log.error("[Agent Migration] install cloud agent success! device id: {}", deviceDO.getDeviceId());
        }


        if (finish){
            migrationResult.put(MigrationProgressEnum.AGENT_PROGRESS.getType(), MigrationStateEnum.SUCCESS);
        } else {
            migrationResult.put(MigrationProgressEnum.AGENT_PROGRESS.getType(), MigrationStateEnum.FAILED);
        }
        migrationConfigurationRepository.updateMigrationResultByUserId(userId, JSON.toJSONString(migrationResult));

        return finish;
    }

    private MgrRequest buildMgrRequest(DeviceDO deviceDO) {
        return MgrRequest.builder().
                instanceIp(deviceDO.getPublicIp()).
                instanceUser("").
                needPassword(false).
                build();
    }
}

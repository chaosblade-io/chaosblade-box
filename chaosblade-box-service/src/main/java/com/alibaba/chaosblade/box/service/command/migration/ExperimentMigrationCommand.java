package com.alibaba.chaosblade.box.service.command.migration;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.enums.MigrationProgressEnum;
import com.alibaba.chaosblade.box.common.common.enums.MigrationStateEnum;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ChaosAppException;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentGrade;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.MigrationConfigurationDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.MigrationConfigurationRepository;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentInfoQueryCommand;
import com.alibaba.chaosblade.box.service.model.migration.ExperimentDefinitionApiRequest;
import com.alibaba.chaosblade.box.service.model.migration.Flow;
import com.alibaba.chaosblade.box.service.model.migration.FlowActivity;
import com.alibaba.chaosblade.box.service.model.migration.FlowGroup;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExperimentMigrationCommand extends SpringBeanCommand<BaseRequest, Boolean> {

    @Autowired
    private MigrationConfigurationRepository migrationConfigurationRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

    private static String DefaultRegionId = "popunify-pre.cn-hangzhou.aliyuncs.com";

    private static String SysVersion = "2019-09-01";

    private static String ExperimentOpenApiName = "CreateExperiment";

    @Value("${regionId.default}")
    private String regionId;

    @Override
    public Boolean execute(BaseRequest baseRequest) {
        log.info("experiment experiment experiment experiment");
        String userId = baseRequest.getUserId();
        Optional<MigrationConfigurationDO> migrationConfigurationDO = migrationConfigurationRepository.findByUserId(userId);
        if (!migrationConfigurationDO.isPresent()){
            return false;
        }
        ConcurrentHashMap migrationResult;
        migrationResult = JSON.parseObject(migrationConfigurationDO.get().getMigrationResult(), ConcurrentHashMap.class);

        Boolean finish = true;
        List<ExperimentDO> experimentDOS = experimentRepository.findByUserId(userId);
        for (ExperimentDO experimentDO : experimentDOS) {
            Boolean reportResult = reportMetricToCloud(getApiRequest(experimentDO),experimentDO.getDescription(),
                    migrationConfigurationDO.get().getCloudAk(),
                    migrationConfigurationDO.get().getCloudSk());
            if (!reportResult) {
                finish = false;
                log.warn("report failed , experimentId is : "+ experimentDO.getExperimentId());
            }
        }

        if (finish) {
            migrationResult.put(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType(), MigrationStateEnum.SUCCESS);
        }else {
            migrationResult.put(MigrationProgressEnum.EXPERIMENT_PROGRESS.getType(), MigrationStateEnum.FAILED);
        }
        migrationConfigurationRepository.updateMigrationResultByUserId(userId, JSON.toJSONString(migrationResult));

        return finish;
    }

    private ExperimentDefinitionApiRequest getApiRequest(ExperimentDO experimentDO) {
        ExperimentDefinitionApiRequest experimentDefinitionApiRequest = new ExperimentDefinitionApiRequest();
        experimentDefinitionApiRequest.setName(experimentDO.getName());
        experimentDefinitionApiRequest.setRunMode(experimentDO.getRunMode());
        experimentDefinitionApiRequest.setDuration(experimentDO.getDuration());

        BaseExperimentRequest baseExperimentRequest = new BaseExperimentRequest();
        baseExperimentRequest.setExperimentId(experimentDO.getExperimentId());
        baseExperimentRequest.setNamespace(experimentDO.getNamespace());
        Response<ExperimentInfo> experimentInfoResponse = commandBus.syncRun(ExperimentInfoQueryCommand.class, baseExperimentRequest);
        if (null == experimentInfoResponse.getResult()) {
            return null;
        }
        ExperimentInfo experimentInfo = experimentInfoResponse.getResult();

        List<MiniFlowGroup> miniFlowGroupList = experimentInfo.getFlowInfo().getFlowGroups();
        List<FlowGroup> flowGroups = new ArrayList<>();
        for (MiniFlowGroup miniFlowGroup : miniFlowGroupList) {
            FlowGroup flowGroup = new FlowGroup();
            flowGroup.setAppName(miniFlowGroup.getAppName());
            flowGroup.setAppGroups(miniFlowGroup.getAppGroups());
            flowGroup.setGroupName(miniFlowGroup.getGroupName());
            flowGroup.setHosts(miniFlowGroup.getHosts().stream().map(Host::getPrivateIp).collect(Collectors.toList()));

            List<MiniFlow> miniFlows = miniFlowGroup.getFlows();
            List<Flow> flows = new ArrayList<>();
            for (MiniFlow miniFlow : miniFlows) {
                Flow flow = new Flow();
                flow.setPrepare(getActivities(miniFlow.getPrepare()));
                if(CollectionUtil.isNullOrEmpty(miniFlow.getAttack()) || null == miniFlow.getAttack().get(0)) {
                    throw new ChaosAppException("can not build flow");
                }
                flow.setAttack(getActivity(miniFlow.getAttack().get(0)));
                flow.setCheck(getActivities(miniFlow.getCheck()));
                flow.setRecover(getActivities(miniFlow.getRecover()));
                flows.add(flow);
            }
            flowGroup.setFlows(flows);
            flowGroups.add(flowGroup);
        }
        experimentDefinitionApiRequest.setFlowGroups(flowGroups);

        return experimentDefinitionApiRequest;

    }

    private List<FlowActivity> getActivities(List<ExperimentActivityInfo> experimentActivityInfos) {
        return experimentActivityInfos.stream().map(experimentActivityInfo -> {
            return getActivity(experimentActivityInfo);
        }).collect(Collectors.toList());
    }

    private FlowActivity getActivity(ExperimentActivityInfo experimentActivityInfo) {
            FlowActivity flowActivity = new FlowActivity();
            flowActivity.setActivityName(experimentActivityInfo.getActivityName());
            flowActivity.setAppCode(experimentActivityInfo.getAppCode());
            flowActivity.setUserCheck(experimentActivityInfo.isUserCheck());
            List<SceneArgumentGrade> sceneArgumentGrades = experimentActivityInfo.getArguments();
            Map<String,String> param = new HashMap<>();
            sceneArgumentGrades.stream().forEach(sceneArgumentGrade -> {
                List<SceneArgumentDefinition> sceneArgumentDefinitions = sceneArgumentGrade.getArgumentList();
                sceneArgumentDefinitions.forEach(sceneArgumentDefinition -> {
                    if(null != sceneArgumentDefinition.getValue()) {
                        param.put(sceneArgumentDefinition.getAlias(),sceneArgumentDefinition.getValue());
                    }
                });
            });
            flowActivity.setArguments(param);
            return flowActivity;
    }


    private boolean reportMetricToCloud(ExperimentDefinitionApiRequest experimentDefinitionApiRequest, String description, String ak, String sk) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ak, sk);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        // todo regionId
        request.setSysDomain(String.format("ahas.%s.aliyuncs.com", regionId));
        request.setSysVersion(SysVersion);
        request.setSysAction(ExperimentOpenApiName);

        request.putQueryParameter("Description", description);
        request.putQueryParameter("Definition", JSON.toJSONString(experimentDefinitionApiRequest));
        request.putQueryParameter("Name", "migration-"+experimentDefinitionApiRequest.getName());
        request.putQueryParameter("NameSpace", experimentDefinitionApiRequest.getNamespace());

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return false;
    }


}

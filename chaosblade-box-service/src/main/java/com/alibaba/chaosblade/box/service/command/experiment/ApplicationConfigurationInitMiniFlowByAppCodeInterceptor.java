package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.infrastructure.SceneArgumentGradeConverter;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.InitMiniFlowRequest;
import com.alibaba.chaosblade.box.service.infrastructure.configuration.ApplicationConfiguration;
import com.alibaba.chaosblade.box.service.infrastructure.configuration.ApplicationConfigurationProvider;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Sets;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 初始化的演练流程的时候,可以设置默认的参数值
 *
 * @author haibin.lhb
 *
 * 
 */
@Slf4j
@Component
public class ApplicationConfigurationInitMiniFlowByAppCodeInterceptor implements InitMiniFlowByAppCodeInterceptor {

    @Autowired
    private ApplicationConfigurationProvider applicationConfigurationProvider;

    @Autowired
    private SceneArgumentGradeConverter sceneArgumentGradeConverter;

    @Override
    public void afterInit(InitMiniFlowByAppCodeInterceptorContext initMiniFlowByAppCodeInterceptorContext) {
        InitMiniFlowRequest initMiniFlowRequest = initMiniFlowByAppCodeInterceptorContext.getInitMiniFlowRequest();
        if (initMiniFlowRequest.getAppId() == null) { return; }
        Long appId = initMiniFlowRequest.getAppId();
        List<ApplicationConfiguration> applicationConfigurationList = getApplicationConfigurationList(
                initMiniFlowRequest, appId);
        Map<String, List<ExperimentActivityInfo>> phaseToExperimentActivityInfo
                = initMiniFlowByAppCodeInterceptorContext.getResponse();
        for (Map.Entry<String, List<ExperimentActivityInfo>> entry : phaseToExperimentActivityInfo.entrySet()) {
            PhaseType phaseType = PhaseType.valueOf(entry.getKey().toUpperCase());
            List<ApplicationConfiguration> configurationsSupportPhases = filterConfigurationsByPhase(
                    applicationConfigurationList, phaseType);
            if (configurationsSupportPhases.isEmpty()) { continue; }
            for (ExperimentActivityInfo experimentActivityInfo : entry.getValue()) {
                List<ApplicationConfiguration> configurationsSupportAppCode = getConfigurationsSupportAppCode(
                        configurationsSupportPhases, experimentActivityInfo);
                if (configurationsSupportAppCode.isEmpty()) { continue; }
                for (SceneArgumentDefinition sceneArgumentDefinition : sceneArgumentGradeConverter.tranToArgumentsList(experimentActivityInfo.getArguments())) {
                    List<ApplicationConfiguration> configurationsSupportAlias = getConfigurationsSupportAlias(
                            configurationsSupportAppCode, sceneArgumentDefinition);
                    if (configurationsSupportAlias.isEmpty()) { continue; }
                    for (ApplicationConfiguration applicationConfiguration : configurationsSupportAlias) {
                        if (!Strings.isNullOrEmpty(applicationConfiguration.getValue())) {
                            sceneArgumentDefinition.getComponent().setDefaultValue(applicationConfiguration.getValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据应用信息来获取配置项目
     *
     * @param initMiniFlowRequest
     * @param appId
     * @return
     */
    @NotNull
    private List<ApplicationConfiguration> getApplicationConfigurationList(InitMiniFlowRequest initMiniFlowRequest,
                                                                           Long appId) {
        return applicationConfigurationProvider
                .provideApplicationConfigurations(appId).stream().filter(new Predicate<ApplicationConfiguration>() {
                    @Override
                    public boolean test(ApplicationConfiguration applicationConfiguration) {
                        if(null == applicationConfiguration.getScope() ||
                                null == applicationConfiguration.getScope().getNodeGroups()) { return true; }
                        List<String> nodeGroups = applicationConfiguration.getScope().getNodeGroups();
                        if (nodeGroups.isEmpty()) { return true; }
                        return !Sets.intersect(new HashSet<>(nodeGroups),
                                new HashSet<>(initMiniFlowRequest.getNodeGroups()))
                                .isEmpty();
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 根据参数名字来筛选配置项
     *
     * @param configurationsSupportAppCode
     * @param sceneArgumentDefinition
     * @return
     */
    @NotNull
    private List<ApplicationConfiguration> getConfigurationsSupportAlias(
            List<ApplicationConfiguration> configurationsSupportAppCode, SceneArgumentDefinition sceneArgumentDefinition) {
        return configurationsSupportAppCode.stream().filter(
                new Predicate<ApplicationConfiguration>() {
                    @Override
                    public boolean test(ApplicationConfiguration applicationConfiguration) {
                        return applicationConfiguration.getFunctionParamAlias().equals(sceneArgumentDefinition.getAlias());
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 根据小程序code来筛选配置项
     *
     * @param configurationsSupportPhases
     * @param experimentActivityInfo
     * @return
     */
    @NotNull
    private List<ApplicationConfiguration> getConfigurationsSupportAppCode(
            List<ApplicationConfiguration> configurationsSupportPhases, ExperimentActivityInfo experimentActivityInfo) {
        return configurationsSupportPhases.stream().filter(
                new Predicate<ApplicationConfiguration>() {
                    @Override
                    public boolean test(ApplicationConfiguration applicationConfiguration) {
                        return applicationConfiguration.supportAppCode(experimentActivityInfo.getAppCode());
                    }
                }).collect(Collectors.toList());
    }

    @NotNull
    private List<ApplicationConfiguration> filterConfigurationsByPhase(
            List<ApplicationConfiguration> applicationConfigurationList, PhaseType phaseType) {
        return applicationConfigurationList.stream().filter(
                new Predicate<ApplicationConfiguration>() {
                    @Override
                    public boolean test(ApplicationConfiguration applicationConfiguration) {
                        return applicationConfiguration.supportPhase(phaseType);
                    }
                }).collect(Collectors.toList());
    }
}

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentFlowDefinitionMeta {
    /**
     * 是否包含jvm场景
     */
    private boolean includeJvm;

    private boolean includeJvmInstall;

    /**
     * 是否新建操作
     */
    private boolean create;

    /**
     * 演练Id
     */
    private String experimentId;

    private ExperimentDO experimentDO;

    private ExperimentActivitiesMap activitiesMap;

    /**
     * 这里面的code只允许出现一次
     */
    private Map<String, Integer> appCounter = new HashMap<>();

    private Map<String, Object> appIdToApplicationDO = new HashMap<>();

    private Set<String> apps = new HashSet<>();

    private Set<String> appCodes = new HashSet<>();

    public boolean isJavaAgentUseless() {
        return includeJvmInstall && !includeJvm;
    }

    public static class ExperimentActivitiesMap {

        private Map<PhaseType, List<ExperimentActivityDO>> phaseTypeListMap = new HashMap<>();

        private Set<String> activities = new HashSet<>();

        private Map<String, List<ExperimentActivityDO>> flowIdToExperimentActivityDO = new HashMap<>();

        public void addExperimentActivityDO(PhaseType phaseType, ExperimentActivityDO experimentActivityDO) {
            if (experimentActivityDO.getActivityId() != null) {
                if (activities.contains(experimentActivityDO.getActivityId())) { return; }
                activities.add(experimentActivityDO.getActivityId());
            }
            phaseTypeListMap.computeIfAbsent(phaseType, phaseType1 -> new ArrayList<>()).add(experimentActivityDO);
            if (experimentActivityDO.getFlowId() != null) {
                flowIdToExperimentActivityDO.computeIfAbsent(experimentActivityDO.getFlowId(),
                    s -> new ArrayList<>())
                    .add(experimentActivityDO);
            }
        }

        public List<ExperimentActivityDO> getFlowIdByExperimentActivityDO(String flowId) {
            return flowIdToExperimentActivityDO.getOrDefault(flowId, new ArrayList<>());
        }

        public List<ExperimentActivityDO> getExperimentActivityDOs() {
            return phaseTypeListMap.values().stream().flatMap(
                (Function<List<ExperimentActivityDO>, Stream<ExperimentActivityDO>>)Collection::stream).collect(
                Collectors.toList());
        }

        public List<ExperimentActivityDO> getExperimentActivityDOSByPhase(PhaseType phaseType) {
            return phaseTypeListMap.computeIfAbsent(phaseType, new Function<PhaseType, List<ExperimentActivityDO>>() {
                @Override
                public List<ExperimentActivityDO> apply(PhaseType phaseType) {
                    return new ArrayList<>();
                }
            });
        }

    }
}

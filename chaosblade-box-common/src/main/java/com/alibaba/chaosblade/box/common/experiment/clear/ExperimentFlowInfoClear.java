package com.alibaba.chaosblade.box.common.experiment.clear;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author haibin.lhb
 *
 *
 */
public class ExperimentFlowInfoClear {

    public static void clearAllIds(ExperimentFlowInfo experimentFlowInfo) {
        if (experimentFlowInfo == null) { return; }
        List<MiniFlowGroup> flowGroups = experimentFlowInfo.getFlowGroups();
        if (flowGroups == null) { return; }
        flowGroups.forEach(new Consumer<MiniFlowGroup>() {
            @Override
            public void accept(MiniFlowGroup miniFlowGroup) {
                miniFlowGroup.setGroupId(null);
                List<MiniFlow> miniFlows = miniFlowGroup.getFlows();
                if (miniFlows == null) {
                    return;
                }
                miniFlows.forEach(new Consumer<MiniFlow>() {
                    @Override
                    public void accept(MiniFlow miniFlow) {
                        miniFlow.setFlowId(null);
                        miniFlow.getPrepare().forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                        miniFlow.getAttack().forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                        miniFlow.getCheck().forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                        miniFlow.getRecover().forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                    }
                });
            }
        });
    }

    /**
     * 演练转经验，还需要清除掉应用和机器相关信息
     * @param experimentFlowInfo
     */
    public static void clearApplicationAndHosts(ExperimentFlowInfo experimentFlowInfo) {
        if (experimentFlowInfo == null) { return; }
        List<MiniFlowGroup> flowGroups = experimentFlowInfo.getFlowGroups();
        if (flowGroups == null) { return; }
        flowGroups.forEach(new Consumer<MiniFlowGroup>() {
            @Override
            public void accept(MiniFlowGroup miniFlowGroup) {
                miniFlowGroup.setAppId(null);
                miniFlowGroup.setAppName(null);
                miniFlowGroup.setAppGroups(null);
                miniFlowGroup.setHosts(null);
                miniFlowGroup.setSelectType(null);
                miniFlowGroup.setHostPercent(null);
                List<MiniFlow> miniFlows = miniFlowGroup.getFlows();
                if (miniFlows == null) {
                    return;
                }
                miniFlows.forEach(new Consumer<MiniFlow>() {
                    @Override
                    public void accept(MiniFlow miniFlow) {
                        miniFlow.setFlowId(null);
                        miniFlow.getPrepare().forEach(
                                experimentActivityInfo -> experimentActivityInfo.setScope(null));
                        miniFlow.getAttack().forEach(
                                experimentActivityInfo -> experimentActivityInfo.setScope(null));
                        miniFlow.getCheck().forEach(
                                experimentActivityInfo -> experimentActivityInfo.setScope(null));
                        miniFlow.getRecover().forEach(
                                experimentActivityInfo -> experimentActivityInfo.setScope(null));
                    }
                });
            }
        });
    }
}

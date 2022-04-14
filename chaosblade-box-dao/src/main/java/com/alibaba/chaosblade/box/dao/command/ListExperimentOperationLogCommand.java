package com.alibaba.chaosblade.box.dao.command;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.experiment.log.ExperimentOperationLog;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers.BaseChangeLogTracker;
import com.alibaba.chaosblade.box.dao.infrastructure.service.ChangelogService;
import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ChangelogQuery;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 查询演练操作
 *
 * @author haibin.lhb
 *
 *
 */
@Component
public class ListExperimentOperationLogCommand
    extends SpringBeanCommand<ExperimentPageableQueryRequest, Response<PageableResponse<ExperimentOperationLog>>> {

    @Autowired
    private ChangelogService changelogService;

    @Override
    public Response<PageableResponse<ExperimentOperationLog>> execute(
        ExperimentPageableQueryRequest experimentPageableQueryRequest) {
        ChangelogQuery changelogQuery = new ChangelogQuery();
        changelogQuery.setTargetType(ChangelogTypes.ChangeTargetType.EXPERIMENT.getName());
        changelogQuery.setTargetId(experimentPageableQueryRequest.getExperimentId());
        PageableResponse<ChangelogDO> pageableResponse = changelogService.getChangelogs(
            experimentPageableQueryRequest.getPage(),
            experimentPageableQueryRequest.getSize(), changelogQuery);
        PageableResponse<ExperimentOperationLog> experimentOperationLogPageableResponse = PageableResponse.of(
            pageableResponse.page(), pageableResponse.getPageSize(), pageableResponse.getData().stream().map(
                changelogDO -> {
                    ExperimentOperationLog experimentOperationLog = new ExperimentOperationLog();
                    experimentOperationLog.setTime(changelogDO.getGmtCreate());
                    fillOperatorInfo(experimentOperationLog, changelogDO);
                    experimentOperationLog.setDescription(buildDescription(changelogDO));
                    experimentOperationLog.setChangeType(
                        BaseChangeLogTracker.getChangeActionTypeDescription(changelogDO.getChangeType()));
                    experimentOperationLog.setPropertyId(changelogDO.getPropertyId());
                    return experimentOperationLog;
                }).collect(Collectors.toList()), pageableResponse.getPages(), pageableResponse.getTotal());
        return Response.okWithData(experimentOperationLogPageableResponse);
    }

    private void fillOperatorInfo(ExperimentOperationLog experimentOperationLog, ChangelogDO changelogDO) {
        for (ChangelogTypes.ChangeOperatorType changeOperatorType : ChangelogTypes.ChangeOperatorType.values()) {
            if (changeOperatorType.getName().equalsIgnoreCase(changelogDO.getOperatorType())) {
                experimentOperationLog.setOperator(changeOperatorType.getDesc());
            }
        }
    }

    private String buildDescription(ChangelogDO changelogDO) {
        if (Strings.isNullOrEmpty(changelogDO.getChangeDescription())) {
            if (ChangelogTypes.ChangeActionType.RUN.getName().equals(changelogDO.getChangeType())) {
                return "运行演练,任务ID:";
            }
            if (ChangelogTypes.ChangeActionType.STOP.getName().equals(changelogDO.getChangeType())) {
                return "停止演练,任务ID:";
            }
            if (ChangelogTypes.ChangeActionType.Update.getName().equals(changelogDO.getChangeType())) {
                return "修改演练";
            }
            if (ChangelogTypes.ChangeActionType.ADD.getName().equals(changelogDO.getChangeType())) {
                return "新建演练";
            }
        }
        return changelogDO.getChangeDescription();
    }

}

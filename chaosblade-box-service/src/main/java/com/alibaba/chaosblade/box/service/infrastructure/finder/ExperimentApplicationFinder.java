package com.alibaba.chaosblade.box.service.infrastructure.finder;

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentHostRelationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentHostRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRepository;
import com.alibaba.chaosblade.box.service.model.ExperimentApplication;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class ExperimentApplicationFinder {

    @Autowired
    private ExperimentHostRelationMapper experimentHostRelationMapper;

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<ExperimentApplication> findApplicationsInExperiment(String mainUserId, String namespace) {
        QueryWrapper<ExperimentHostRelationDO>
            queryWrapper = buildQuery(mainUserId, namespace);
        return doQuery(queryWrapper);
    }
    
    private List<ExperimentApplication> doQuery(QueryWrapper<ExperimentHostRelationDO> queryWrapper) {
        return experimentHostRelationMapper.selectMaps(queryWrapper).stream().map(
            stringObjectMap -> {
                ExperimentApplication experimentApplication = new ExperimentApplication();
                experimentApplication.setAppId((String)stringObjectMap.get(ExperimentHostRelationDO.COLUMN_APP_ID));
                experimentApplication.setTaskCount((Long)stringObjectMap.get(
                    ExperimentHostRelationDO.RESULT_FIELD_TOTAL));
                experimentApplication.setAppName(applicationRepository.findByIdWithStr(experimentApplication.getAppId())
                    .map(
                        ApplicationDO::getAppName).orElse(null));
                return experimentApplication;
            }).collect(Collectors.toList());
    }

    private QueryWrapper<ExperimentHostRelationDO> buildQuery(String mainUserId,
        String namespace) {
        QueryWrapper<ExperimentHostRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(ExperimentHostRelationDO.CONDITION_APP_ID_COUNT_DISTINCT_OUTER_ID_AS_TOTAL);
        queryWrapper.eq(ExperimentHostRelationDO.COLUMN_USER_ID, mainUserId);
        queryWrapper.eq(ExperimentHostRelationDO.COLUMN_RELATION_TYPE,
            ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
        queryWrapper.eq(ExperimentHostRelationDO.COLUMN_NAMESPACE, namespace);
        queryWrapper.eq(ExperimentHostRelationDO.COLUMN_EXPERIMENT_TASK_STATE, StateEnum.RUNNING.getValue());
        queryWrapper.groupBy(ExperimentHostRelationDO.COLUMN_APP_ID);
        return queryWrapper;
    }
}

package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ExperimentNotificationMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentNotificationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Data
@Component
public class ExperimentNotificationRepository implements IRepository<String, ExperimentNotificationDO> {

    @Autowired
    private ExperimentNotificationMapper experimentNotificationMapper;

    @Override
    public Optional<ExperimentNotificationDO> findById(String s) {
        return Optional.ofNullable(experimentNotificationMapper.selectById(s));
    }

    @Override
    public boolean update(ExperimentNotificationDO experimentNotificationDO) {
        experimentNotificationMapper.updateById(experimentNotificationDO);
        return true;
    }

    @Override
    public boolean add(ExperimentNotificationDO experimentNotificationDO) {
        if (experimentNotificationDO.getEnabled() == null) {
            experimentNotificationDO.setEnabled(true);
        }
        experimentNotificationMapper.insert(experimentNotificationDO);
        return true;
    }

    public Optional<ExperimentNotificationDO> findByExperimentId(String experimentId) {
        return Optional.ofNullable(experimentNotificationMapper
            .selectOne(new QueryWrapper<ExperimentNotificationDO>()
                .eq(ExperimentNotificationDO.FIELD_EXPERIMENT_ID, experimentId)));
    }

}

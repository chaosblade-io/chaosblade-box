/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosbox.dao.repository;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosbox.dao.QueryWrapperBuilder;
import com.alibaba.chaosbox.dao.mapper.MetricTaskMapper;
import com.alibaba.chaosbox.dao.model.MetricTaskDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class MetricTaskRepository extends ServiceImpl<MetricTaskMapper, MetricTaskDO> implements IRepository<Long, MetricTaskDO> {

    @Autowired
    private MetricTaskMapper metricTaskMapper;

    @Override
    public Optional<MetricTaskDO> selectById(Long aLong) {
        return Optional.ofNullable(metricTaskMapper.selectById(aLong));
    }

    @Override
    public Long insert(MetricTaskDO metricTaskDO) {
        metricTaskMapper.insert(metricTaskDO);
        return metricTaskDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, MetricTaskDO metricTaskDO) {
        metricTaskDO.setId(id);
        return metricTaskMapper.updateById(metricTaskDO) == 1;
    }

    public List<MetricTaskDO> selectByTaskIdAndCategory(Long taskId,
                                                        Long deviceId,
                                                        String category,
                                                        String startTime,
                                                        String endTime
    ) {
        QueryWrapper<MetricTaskDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(MetricTaskDO::getTaskId, taskId);
        if (deviceId != null) {
            queryWrapper.lambda().eq(MetricTaskDO::getDeviceId, deviceId);
        }
        queryWrapper.lambda().eq(MetricTaskDO::getCategoryCode, category);
        if (StrUtil.isNotBlank(startTime)) {
            queryWrapper.lambda().ge(MetricTaskDO::getDate, startTime);
        }
        if (StrUtil.isNotBlank(endTime)) {
            queryWrapper.lambda().le(MetricTaskDO::getDate, endTime);
        }
        return metricTaskMapper.selectList(queryWrapper);
    }
}

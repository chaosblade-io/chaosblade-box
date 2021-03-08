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

import com.alibaba.chaosbox.dao.QueryWrapperBuilder;
import com.alibaba.chaosbox.dao.mapper.ExperimentTaskLogMapper;
import com.alibaba.chaosbox.dao.model.ExperimentTaskLogDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ExperimentTaskLogRepository implements IRepository<Long, ExperimentTaskLogDO> {

    @Autowired
    private ExperimentTaskLogMapper experimentTaskLogMapper;

    @Override
    public Optional<ExperimentTaskLogDO> selectById(Long aLong) {
        return Optional.ofNullable(experimentTaskLogMapper.selectById(aLong));
    }

    @Override
    public Long insert(ExperimentTaskLogDO experimentTaskLogDO) {
        experimentTaskLogMapper.insert(experimentTaskLogDO);
        return experimentTaskLogDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentTaskLogDO experimentTaskLogDO) {
        experimentTaskLogDO.setId(id);
        return experimentTaskLogMapper.updateById(experimentTaskLogDO) == 1;
    }

    public List<ExperimentTaskLogDO> selectByTaskId(Long taskId) {
        QueryWrapper<ExperimentTaskLogDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ExperimentTaskLogDO::getTaskId, taskId);
        return experimentTaskLogMapper.selectList(queryWrapper);
    }
}

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
import com.alibaba.chaosbox.dao.mapper.ExperimentActivityTaskMapper;
import com.alibaba.chaosbox.dao.model.ExperimentActivityTaskDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ExperimentActivityTaskRepository implements IRepository<Long, ExperimentActivityTaskDO> {

    @Autowired
    private ExperimentActivityTaskMapper experimentActivityTaskMapper;

    public List<ExperimentActivityTaskDO> selectByTaskId(Long taskId) {
        QueryWrapper<ExperimentActivityTaskDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ExperimentActivityTaskDO::getExperimentTaskId, taskId);
        return experimentActivityTaskMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<ExperimentActivityTaskDO> selectById(Long aLong) {
        return Optional.ofNullable(experimentActivityTaskMapper.selectById(aLong));
    }

    @Override
    public Long insert(ExperimentActivityTaskDO experimentActivityTaskDO) {
        experimentActivityTaskMapper.insert(experimentActivityTaskDO);
        return experimentActivityTaskDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentActivityTaskDO experimentActivityTaskDO) {
        experimentActivityTaskDO.setId(id);
        return experimentActivityTaskMapper.updateById(experimentActivityTaskDO) == 1;
    }
}

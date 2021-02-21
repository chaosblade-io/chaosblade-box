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

package com.alibaba.chaosblade.platform.dao.repository;

import com.alibaba.chaosblade.platform.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.platform.dao.mapper.ExperimentTaskMapper;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ExperimentTaskRepository implements IRepository<Long, ExperimentTaskDO> {

    @Autowired
    private ExperimentTaskMapper experimentTaskMapper;

    @Override
    public Optional<ExperimentTaskDO> selectById(Long aLong) {
        return Optional.ofNullable(experimentTaskMapper.selectById(aLong));
    }

    @Override
    public Long insert(ExperimentTaskDO experimentTaskDO) {
        experimentTaskMapper.insert(experimentTaskDO);
        return experimentTaskDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentTaskDO experimentTaskDO) {
        experimentTaskDO.setId(id);
        return experimentTaskMapper.updateById(experimentTaskDO) == 1;
    }

    public ExperimentTaskDO selectLatestByExperimentId(Long experimentId) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().orderByDesc(ExperimentTaskDO::getGmtCreate);
        queryWrapper.lambda().eq(ExperimentTaskDO::getExperimentId, experimentId);
        queryWrapper.last("limit 1");
        return experimentTaskMapper.selectOne(queryWrapper);
    }

    public List<ExperimentTaskDO> selectByExperimentId(Long experimentId) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().orderByDesc(ExperimentTaskDO::getGmtCreate);
        queryWrapper.lambda().eq(ExperimentTaskDO::getExperimentId, experimentId);
        return experimentTaskMapper.selectList(queryWrapper);
    }

}

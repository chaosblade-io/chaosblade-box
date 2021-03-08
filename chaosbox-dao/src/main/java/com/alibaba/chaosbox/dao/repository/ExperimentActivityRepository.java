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

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosbox.dao.QueryWrapperBuilder;
import com.alibaba.chaosbox.dao.mapper.ExperimentActivityMapper;
import com.alibaba.chaosbox.dao.model.ExperimentActivityDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Repository
public class ExperimentActivityRepository extends ServiceImpl<ExperimentActivityMapper, ExperimentActivityDO>
        implements IRepository<Long, ExperimentActivityDO> {

    @Autowired
    private ExperimentActivityMapper experimentActivityMapper;

    public Map<String, List<ExperimentActivityDO>> selectByExperiment(Long experimentId) {
        QueryWrapper<ExperimentActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExperimentActivityDO::getExperimentId, experimentId);
        List<ExperimentActivityDO> activityDOS = experimentActivityMapper.selectList(queryWrapper);
        return activityDOS.stream().collect(Collectors.toMap(
                ExperimentActivityDO::getPhase, CollUtil::newArrayList, CollUtil::addAllIfNotContains));
    }

    public List<ExperimentActivityDO> selectListByExperiment(Long experimentId) {
        QueryWrapper<ExperimentActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExperimentActivityDO::getExperimentId, experimentId);
        List<ExperimentActivityDO> activityDOS = experimentActivityMapper.selectList(queryWrapper);
        return activityDOS;
    }

    @Override
    public Optional<ExperimentActivityDO> selectById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Long insert(ExperimentActivityDO experimentActivityDO) {
        experimentActivityMapper.insert(experimentActivityDO);
        return experimentActivityDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentActivityDO experimentActivityDO) {
        experimentActivityDO.setId(id);
        return experimentActivityMapper.updateById(experimentActivityDO) == 1;
    }

    public void deleteExperimentId(Long experimentId) {
        QueryWrapper<ExperimentActivityDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ExperimentActivityDO::getExperimentId, experimentId);
        experimentActivityMapper.delete(queryWrapper);
    }
}

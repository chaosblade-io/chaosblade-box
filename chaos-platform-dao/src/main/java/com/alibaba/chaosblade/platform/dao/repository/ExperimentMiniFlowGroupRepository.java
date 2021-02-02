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
import com.alibaba.chaosblade.platform.dao.mapper.ExperimentMiniFlowGroupMapper;
import com.alibaba.chaosblade.platform.dao.model.ExperimentMiniFlowGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Component
public class ExperimentMiniFlowGroupRepository implements IRepository<Long, ExperimentMiniFlowGroupDO> {

    @Autowired
    private ExperimentMiniFlowGroupMapper experimentMiniFlowGroupMapper;

    @Override
    public Optional<ExperimentMiniFlowGroupDO> selectById(Long aLong) {
        return Optional.ofNullable(experimentMiniFlowGroupMapper.selectById(aLong));
    }

    @Override
    public Long insert(ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO) {
        experimentMiniFlowGroupMapper.insert(experimentMiniFlowGroupDO);
        return experimentMiniFlowGroupDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO) {
        experimentMiniFlowGroupDO.setExperimentId(id);
        return experimentMiniFlowGroupMapper.updateById(experimentMiniFlowGroupDO) == 1;
    }

    public List<ExperimentMiniFlowGroupDO> selectByExperiment(Long experimentId) {
        QueryWrapper<ExperimentMiniFlowGroupDO> wrapper = QueryWrapperBuilder.build();
        wrapper.lambda().eq(ExperimentMiniFlowGroupDO::getExperimentId, experimentId);
        return experimentMiniFlowGroupMapper.selectList(wrapper);
    }

    public boolean updateByExperimentId(Long experimentId, ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO) {
        QueryWrapper<ExperimentMiniFlowGroupDO> wrapper = QueryWrapperBuilder.build();
        wrapper.lambda().eq(ExperimentMiniFlowGroupDO::getExperimentId, experimentId);
        return experimentMiniFlowGroupMapper.update(experimentMiniFlowGroupDO, wrapper) == 1;
    }

}

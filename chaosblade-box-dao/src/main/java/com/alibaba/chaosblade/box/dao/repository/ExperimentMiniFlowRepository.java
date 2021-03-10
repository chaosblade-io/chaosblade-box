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

package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentMiniFlowMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniFlowDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author yefei
 */
@Component
public class ExperimentMiniFlowRepository implements IRepository<Long, ExperimentMiniFlowDO> {

    @Autowired
    private ExperimentMiniFlowMapper experimentMiniFlowMapper;

    @Override
    public Optional<ExperimentMiniFlowDO> selectById(Long aLong) {
        return Optional.ofNullable(experimentMiniFlowMapper.selectById(aLong));
    }

    @Override
    public Long insert(ExperimentMiniFlowDO experimentMiniFlowDO) {
        experimentMiniFlowMapper.insert(experimentMiniFlowDO);
        return experimentMiniFlowDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentMiniFlowDO experimentMiniFlowDO) {
        experimentMiniFlowDO.setExperimentId(id);
        return experimentMiniFlowMapper.updateById(experimentMiniFlowDO) == 1;
    }

    public void deleteByExperimentId(Long experimentId) {
        QueryWrapper<ExperimentMiniFlowDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ExperimentMiniFlowDO::getExperimentId, experimentId);
        experimentMiniFlowMapper.delete(queryWrapper);
    }

}

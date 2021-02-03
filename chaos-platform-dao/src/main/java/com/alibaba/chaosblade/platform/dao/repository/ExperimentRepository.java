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

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.dao.mapper.ExperimentMapper;
import com.alibaba.chaosblade.platform.dao.model.ExperimentDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ExperimentRepository implements IRepository<Long, ExperimentDO> {

    @Autowired
    private ExperimentMapper experimentMapper;

    @Override
    public Optional<ExperimentDO> selectById(Long aLong) {
        return Optional.ofNullable(experimentMapper.selectById(aLong));
    }

    @Override
    public Long insert(ExperimentDO experimentDO) {
        experimentMapper.insert(experimentDO);
        return experimentDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ExperimentDO experimentDO) {
        experimentDO.setId(id);
        return experimentMapper.updateById(experimentDO) == 1;
    }

    public List<ExperimentDO> fuzzySelect(ExperimentDO experimentDO) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(experimentDO.getName())) {
            queryWrapper.lambda().like(ExperimentDO::getName, experimentDO.getName());
        }
        return experimentMapper.selectList(queryWrapper);
    }

    public void deleteById(Long id) {
        experimentMapper.deleteById(id);
    }
}

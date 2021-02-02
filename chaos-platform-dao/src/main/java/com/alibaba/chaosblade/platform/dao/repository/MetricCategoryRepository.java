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
import com.alibaba.chaosblade.platform.dao.mapper.MetricCategoryMapper;
import com.alibaba.chaosblade.platform.dao.model.MetricCategoryDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 * @create 2021-01-31 20:52
 */
@Repository
public class MetricCategoryRepository implements IRepository<Long, MetricCategoryDO> {

    @Autowired
    private MetricCategoryMapper metricCategoryMapper;

    public List<MetricCategoryDO> selectAll() {
        return metricCategoryMapper.selectList(QueryWrapperBuilder.build());
    }

    @Override
    public Optional<MetricCategoryDO> selectById(Long aLong) {
        return Optional.ofNullable(metricCategoryMapper.selectById(aLong));
    }

    @Override
    public Long insert(MetricCategoryDO metricCategoryDO) {
        metricCategoryMapper.insert(metricCategoryDO);
        return metricCategoryDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, MetricCategoryDO metricCategoryDO) {
        metricCategoryDO.setId(id);
        return metricCategoryMapper.updateById(metricCategoryDO) == 1;
    }
}

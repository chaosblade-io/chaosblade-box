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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.ProbesMapper;
import com.alibaba.chaosblade.box.dao.model.ProbesDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ProbesRepository implements IRepository<Long, ProbesDO> {

    @Autowired
    private ProbesMapper probesMapper;

    public List<ProbesDO> selectList(ProbesDO probesDO) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(probesDO.getHostname())) {
            queryWrapper.lambda().in(ProbesDO::getHostname, probesDO.getHostname());
        }
        if (StrUtil.isNotBlank(probesDO.getIp())) {
            queryWrapper.lambda().in(ProbesDO::getIp, probesDO.getIp());
        }
        if (probesDO.getStatus() != null) {
            queryWrapper.lambda().in(ProbesDO::getStatus, probesDO.getStatus());
        }

        if (probesDO.getAgentType() != null) {
            queryWrapper.lambda().in(ProbesDO::getAgentType, probesDO.getAgentType());
        }
        if (probesDO.getInstallMode() != null) {
            queryWrapper.lambda().in(ProbesDO::getInstallMode, probesDO.getInstallMode());
        }
        return probesMapper.selectList(queryWrapper);
    }

    public List<ProbesDO> selectByHosts(List<String> hosts) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        if (CollUtil.isNotEmpty(hosts)) {
            queryWrapper.lambda().in(ProbesDO::getIp, hosts);
        }
        return probesMapper.selectList(queryWrapper);
    }

    public Optional<ProbesDO> selectByHost(String host) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ProbesDO::getIp, host);
        return Optional.ofNullable(probesMapper.selectOne(queryWrapper));
    }

    public Optional<ProbesDO> selectByDeviceId(Long deviceId) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ProbesDO::getDeviceId, deviceId);
        return Optional.ofNullable(probesMapper.selectOne(queryWrapper));
    }

    public List<ProbesDO> selectByStatus(String host, Collection<Byte> status) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ProbesDO::getIp, host);
        queryWrapper.lambda().in(ProbesDO::getStatus, status);
        return probesMapper.selectList(queryWrapper);
    }

    public List<ProbesDO> selectByType(Collection<Byte> types) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().in(ProbesDO::getAgentType, types);
        return probesMapper.selectList(queryWrapper);
    }

    public List<ProbesDO> selectByIds(Collection<Long> collection) {
        return probesMapper.selectBatchIds(collection);
    }

    @Override
    public Optional<ProbesDO> selectById(Long aLong) {
        return Optional.ofNullable(probesMapper.selectById(aLong));
    }

    @Override
    public Long insert(ProbesDO probesDO) {
        probesMapper.insert(probesDO);
        return probesDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ProbesDO probesDO) {
        probesDO.setId(id);
        return probesMapper.updateById(probesDO) == 1;
    }

    public boolean updateByHost(String host, ProbesDO probesDO) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ProbesDO::getIp, host);

        return probesMapper.update(probesDO, queryWrapper) == 1;
    }

    public boolean updateByDeviceId(Long deviceId, ProbesDO probesDO) {
        QueryWrapper<ProbesDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(ProbesDO::getDeviceId, deviceId);
        return probesMapper.update(probesDO, queryWrapper) == 1;
    }

    public void deleteById(Long probesId) {
        probesMapper.deleteById(probesId);
    }
}

package com.alibaba.chaosblade.box.dao.repository;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.ClusterMapper;
import com.alibaba.chaosblade.box.dao.model.ClusterDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Repository
public class ClusterRepository implements IRepository<Long, ClusterDO> {

    @Autowired
    private ClusterMapper clusterMapper;

    @Override
    public Optional<ClusterDO> selectById(Long aLong) {
        return Optional.ofNullable(clusterMapper.selectById(aLong));
    }

    @Override
    public Long insert(ClusterDO clusterDO) {
        clusterMapper.insert(clusterDO);
        return clusterDO.getId();
    }

    @Override
    public boolean updateByPrimaryKey(Long id, ClusterDO clusterDO) {
        clusterDO.setId(id);
        return clusterMapper.updateById(clusterDO) > 0;
    }

    public List<ClusterDO> selectList(ClusterDO clusterDO) {
        QueryWrapper<ClusterDO> queryWrapper = QueryWrapperBuilder.build();
        if (StrUtil.isNotBlank(clusterDO.getClusterName())) {
            queryWrapper.lambda().eq(ClusterDO::getClusterName, clusterDO.getClusterName());
        }
        if (clusterDO.getIsCollector() != null) {
            queryWrapper.lambda().eq(ClusterDO::getIsCollector, clusterDO.getIsCollector());
        }
        return clusterMapper.selectList(queryWrapper);
    }
}

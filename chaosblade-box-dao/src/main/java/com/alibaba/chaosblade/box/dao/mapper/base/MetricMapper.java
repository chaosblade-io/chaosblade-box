package com.alibaba.chaosblade.box.dao.mapper.base;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.ChaosMetricDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author haibin.lhb
 *
 * 
 */
@MybatisMapper
public interface MetricMapper extends BaseMapper<ChaosMetricDO> {
}

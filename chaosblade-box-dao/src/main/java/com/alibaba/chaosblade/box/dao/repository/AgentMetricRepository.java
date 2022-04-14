package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.base.MetricMapper;
import com.alibaba.chaosblade.box.dao.model.ChaosMetricDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 * 
 * 
 */
@Component
public class AgentMetricRepository extends ServiceImpl<MetricMapper, ChaosMetricDO> {

    @Autowired
    private MetricMapper metricMapper;

}

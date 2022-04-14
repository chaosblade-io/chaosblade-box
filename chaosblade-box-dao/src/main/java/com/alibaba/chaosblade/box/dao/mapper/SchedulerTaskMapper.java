package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author haibin
 *
 *
 */
@MybatisMapper
public interface SchedulerTaskMapper extends BaseMapper<SchedulerJobDO> {

}

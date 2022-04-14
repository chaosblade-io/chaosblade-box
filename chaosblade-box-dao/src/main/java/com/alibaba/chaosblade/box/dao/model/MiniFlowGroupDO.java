package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.infrastructure.domain.Hosts;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
@TableName("t_chaos_experiment_mini_flow_group")
public class MiniFlowGroupDO extends BaseDO {

    @TableId(type = IdType.ID_WORKER_STR)
    private String groupId;

    private String groupName;

    private String experimentId;

    private Hosts hosts;

    private Integer groupOrder;

    /**
     * 是否必须，如果非必须表示可以删除,默认是false
     */
    private Boolean required;

}

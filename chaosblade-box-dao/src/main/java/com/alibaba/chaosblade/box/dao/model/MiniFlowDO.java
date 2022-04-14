package com.alibaba.chaosblade.box.dao.model;

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
@TableName("t_chaos_experiment_mini_flow")
public class MiniFlowDO extends BaseDO {

    @TableId(type = IdType.ID_WORKER_STR)
    private String flowId;

    private Integer flowOrder;

    private String name;

    private String groupId;

    private String experimentId;

    private Integer groupOrder;

    /**
     * 表示是否可以删除
     */
    private Boolean required;

}

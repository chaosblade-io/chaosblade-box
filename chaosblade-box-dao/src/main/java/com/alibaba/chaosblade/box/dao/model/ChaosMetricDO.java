package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@TableName(value = "t_chaos_metric")
@Data
public class ChaosMetricDO extends BaseDO {

    /**
     * 机器地址
     */
    private String host;

    private String configurationId;

    /**
     * 值
     */
    private Float value;

    /**
     * 单位
     */
    private String unit;

    /**
     * 具体指标
     */
    private String metric;

    /**
     * 指标分类
     */
    private String category;

    /**
     * 记录时间
     */
    private Long timestamp;

    /**
     * 来源
     */
    private Integer source;

    /**
     * 注入ID
     */
    private String expId;

}
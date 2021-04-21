package com.alibaba.chaosblade.box.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * t_chaos_cluster
 *
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_cluster")
public class ClusterDO extends BaseDO {

    private String clusterName;

    private String config;

    private Boolean isCollector;

    private Date lastCollectTime;

    private Byte status;

}

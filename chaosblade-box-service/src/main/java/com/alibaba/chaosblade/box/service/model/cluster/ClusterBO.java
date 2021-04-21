package com.alibaba.chaosblade.box.service.model.cluster;

import com.alibaba.chaosblade.box.dao.page.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClusterBO extends PageQuery {

    private Long id;

    private String clusterName;

    private String config;

    private Boolean isCollector;

    private Date lastCollectTime;

    private Byte status;
}

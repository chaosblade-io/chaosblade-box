package com.alibaba.chaosblade.box.common.common.domain.experiment;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class WorkspaceShortInfo implements Serializable {

    /**
     * workspace
     */
    String workspaceId;

    String name;

    private String relationType;

    /**
     * creator
     */
    private String userId;

    /**
     * 描述
     */
    private String description;

    /**
     * create time
     */
    private Date createTime;
}

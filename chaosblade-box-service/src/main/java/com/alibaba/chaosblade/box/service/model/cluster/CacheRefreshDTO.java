package com.alibaba.chaosblade.box.service.model.cluster;

import lombok.Data;

import java.io.Serializable;

@Data
public class CacheRefreshDTO implements Serializable {
    private static final long serialVersionUID = -1L;
    private String userId;
    private String operation;
    private String resourceType;
    private String configurationId;
}

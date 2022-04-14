package com.alibaba.chaosblade.box.common.infrastructure.domain.app;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class AppInfo {

    private String appId;

    private String appName;

    private List<AppNodeGroup> nodeGroups;

    private AppMembers appMembers;
}

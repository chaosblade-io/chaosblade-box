package com.alibaba.chaosblade.box.common.common.domain.notification;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentNotification {

    private boolean dingDing;

    private boolean email;

    private List<String> empIds;
}

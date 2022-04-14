package com.alibaba.chaosblade.box.common.experiment.risk;

/**
 * @author sunpeng
 *
 *
 */

import lombok.Data;

@Data
public class AppRiskMessage {

    private String appCode;

    private String appName;

    private String message;

}

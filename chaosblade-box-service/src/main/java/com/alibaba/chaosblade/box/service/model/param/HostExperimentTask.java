package com.alibaba.chaosblade.box.service.model.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 *
 *
 */
@Data
public class HostExperimentTask implements Serializable {

    private String user;

    private String experimentTaskId;
}

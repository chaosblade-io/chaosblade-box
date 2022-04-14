package com.alibaba.chaosblade.box.service.model.application;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class ApplicationHostsCount implements Serializable {

    private Integer total;

    private Integer permissionCount;

    private String message;

}

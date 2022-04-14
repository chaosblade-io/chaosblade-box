package com.alibaba.chaosblade.box.service.model.scope;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentScopeFilter {

    public static Integer FILTER_TYPE_ALL = 0;

    public static Integer FILTER_TYPE_EXPERIMENT = 1;

    public static Integer FILTER_TYPE_NOT_EXPERIMENT = 2;

    private String key;

    private Integer type;
}

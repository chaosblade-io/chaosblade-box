package com.alibaba.chaosblade.box.service.model.scope;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentScopeInvokeCount implements Serializable {

    private Date time;

    private Integer total;

}

package com.alibaba.chaosblade.box.service.model.scope;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class FunctionInvocationCount {

    private String name;

    private String code;

    private Integer count;
}

package com.alibaba.chaosblade.box.service.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
@ApiModel
public class RestResponse<T> {

    private T result;

    private Boolean success;

    private Integer statusCode;

    private String code;

    private String message;

    private String requestId;

}

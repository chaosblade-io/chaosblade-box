package com.alibaba.chaosblade.box.service.model.expertise;

import lombok.Data;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ExperimentExpertiseCreateRequest extends BaseExpertiseOperationRequest {

    /**
     * 是否配置同步
     */
    private boolean configSync = false;


    private String experimentId;

    /**
     * 如果expertiseId为空，则会重新生成一个id,这个参数现在只用于configSync=true的情况下
     */
    private String expertiseId;
}

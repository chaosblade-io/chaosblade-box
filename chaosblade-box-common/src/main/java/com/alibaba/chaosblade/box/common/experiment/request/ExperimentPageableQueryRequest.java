package com.alibaba.chaosblade.box.common.experiment.request;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentPageableQueryRequest {

    private String experimentId;

    private int size;

    private int page;
}

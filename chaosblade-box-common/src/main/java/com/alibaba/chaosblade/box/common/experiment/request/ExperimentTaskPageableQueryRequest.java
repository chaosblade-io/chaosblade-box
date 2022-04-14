package com.alibaba.chaosblade.box.common.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class ExperimentTaskPageableQueryRequest extends BaseRequest {

    private int size;

    private int page;

    private String experimentId;

}

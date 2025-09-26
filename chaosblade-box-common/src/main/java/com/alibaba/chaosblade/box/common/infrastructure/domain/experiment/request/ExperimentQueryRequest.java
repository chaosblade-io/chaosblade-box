package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentQueryRequest extends BaseRequest {

  private String searchKey;

  private ExperimentStateEnum state;

  private List<ResultEnum> results;
}

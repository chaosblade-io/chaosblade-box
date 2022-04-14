package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class GetUserApplicationSummariesRequest extends PageableRequest {

    private boolean filterDisabled;

}

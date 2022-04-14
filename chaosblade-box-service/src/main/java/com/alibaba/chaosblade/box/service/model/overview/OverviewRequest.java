package com.alibaba.chaosblade.box.service.model.overview;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewRequest extends BaseRequest {

    private int page = 1;

    private int size = 4;

}

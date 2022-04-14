package com.alibaba.chaosblade.box.common.infrastructure.domain;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class PageableSearchRequest extends PageableRequest {

    private String key;
}

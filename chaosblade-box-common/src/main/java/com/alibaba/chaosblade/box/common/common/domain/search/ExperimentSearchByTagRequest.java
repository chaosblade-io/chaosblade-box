package com.alibaba.chaosblade.box.common.common.domain.search;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentSearchByTagRequest extends CommonExperimentSearchRequest {

    /**
     * 必须含有的标签
     */
    private String requiredTag;

}

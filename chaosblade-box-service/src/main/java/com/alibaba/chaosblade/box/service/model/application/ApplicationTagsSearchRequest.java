package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class ApplicationTagsSearchRequest extends BaseRequest {

    private String key;

    private Long appId;

    private List<String> groupNames;

}

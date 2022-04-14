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
public class ApplicationTagsUpdateRequest extends BaseRequest {

    private String appId;

    private String groupName;

    private List<String> configurationIds;

    private List<String> tags;

}

package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class PageableQueryApplicationHostRequest extends PageableRequest {

    @JSONField(name = "app_id")
    private Long appId;

    @JSONField(name = "app_group")
    private List<String> groups;

    private String key;

    private List<String> tags;

    private Integer osType;

    private List<String> kubNamespaces;

    private List<String> clusterIds;

}

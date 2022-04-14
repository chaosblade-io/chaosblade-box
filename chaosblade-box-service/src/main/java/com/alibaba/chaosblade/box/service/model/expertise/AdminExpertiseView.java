package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @author haibin
 *
 *
 */
@Data
public class AdminExpertiseView {

    @JSONField(name = "expertise_id")
    private String expertiseId;

    private String name;

    @JSONField(name = "function_desc")
    private String functionDesc;

    private Integer state;

    @JSONField(name = "experiment_count")
    private Integer experimentCount;

    @JSONField(name = "gmt_create")
    private Date gmtCreate;

    @JSONField(name = "gmt_modified")
    private Date gmtModified;

    @JSONField(name = "tags")
    private Set<String> tags;

    private ChaosUser creator;
}

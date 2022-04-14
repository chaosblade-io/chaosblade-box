package com.alibaba.chaosblade.box.common.infrastructure.domain.activity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class ActivityGroupDefinitionCheckItem {

    @JSONField(name = "id")
    private String frontId;

    private List<ActivityGroupDefinitionParamCheckResult> params;

    @Data
    public static class ActivityGroupDefinitionParamCheckResult {
        private String alias;

        private String error;
    }

}

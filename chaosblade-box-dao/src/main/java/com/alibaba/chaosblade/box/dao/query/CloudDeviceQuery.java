package com.alibaba.chaosblade.box.dao.query;

import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scope.ScopeQuery;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author sunju
 *
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudDeviceQuery extends ScopeQuery {

    String userId;
    String namespace;

    ScopeTypeEnum scopeType;
    Integer osType;
    String configurationId;

    String key;

    private Boolean isExperiment;

    private Integer limit;

    private List<String> tags;

}
package com.alibaba.chaosblade.box.dao.query;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author haibin
 * 
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentQuery {

    List<String> experimentIds;

    ExperimentStateEnum state;
    ChaosUser user;
    String namespace;
    List<ResultEnum> results;

    /**
     * 部分名字
     */
    String partName;
    Boolean containsDeleted;

    public void setExperimentId(String experimentId) {
        this.experimentIds = Lists.newArrayList(experimentId);
    }
}

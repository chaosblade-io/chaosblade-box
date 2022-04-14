package com.alibaba.chaosblade.box.dao.query;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author haibin
 * 
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskQuery implements Serializable {
    String taskId;
    String host;
    List<Integer> states;
    Date gmtCreateFrom;
    Date gmtCreateTo;
    String experimentId;
    List<Integer> excludeStates;
    ChaosUser user;
    String namespace;

}

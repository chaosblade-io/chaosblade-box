package com.alibaba.chaosblade.box.common.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserExperimentTaskStatRequest {

    ChaosUser user;
    String namespace;

}

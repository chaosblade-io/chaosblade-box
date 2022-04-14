package com.alibaba.chaosblade.box.common.infrastructure.domain.tag;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagCreatingRequest extends BaseRequest {

    String name;
    String code;
    Integer type;

}

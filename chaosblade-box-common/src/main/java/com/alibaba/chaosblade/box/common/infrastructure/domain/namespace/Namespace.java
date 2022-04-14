package com.alibaba.chaosblade.box.common.infrastructure.domain.namespace;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Namespace {

    String id;
    String name;
    String userId;
    String licenseKey;
    String secretKey;
}

package com.alibaba.chaosblade.box.common.app.sdk;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChaosToolkit {

    /**
     * toolkit类型，0为云版本，1为内部版本
     */
    int type;

    String address;
    String password;
    int port;

}

package com.alibaba.chaosblade.box.common.common.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * For describe KV-Store data structure friendly for frontend.
 *
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class KVPair<KEY, VALUE> {

    KEY key;
    VALUE value;

    public KVPair() {

    }

}

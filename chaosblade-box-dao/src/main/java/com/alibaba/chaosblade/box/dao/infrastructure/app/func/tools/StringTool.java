package com.alibaba.chaosblade.box.dao.infrastructure.app.func.tools;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;

/**
 * @author sunju
 *
 */
@UtilityClass
public final class StringTool {

    public static String concat(Object... strings) {
        return Joiner.on("").join(strings);
    }

}

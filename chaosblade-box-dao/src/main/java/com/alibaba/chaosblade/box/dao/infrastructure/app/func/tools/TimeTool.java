package com.alibaba.chaosblade.box.dao.infrastructure.app.func.tools;

import lombok.experimental.UtilityClass;

/**
 * @author sunju
 *
 */
@UtilityClass
public final class TimeTool {

    public static Long timestamp() {
        return System.currentTimeMillis();
    }

}

package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.google.common.base.Preconditions;

/**
 * @author haibin
 * 
 *
 */
public final class SceneFunctionUtils {

    /**
     * functionCode至少三位，a.b.c,前面两位是场景code,a.b
     *
     * @param functionCode
     * @return
     */
    public static String extractSceneCodeFronSunctionCode(String functionCode) {
        Preconditions.checkArgument(functionCode != null, "functionCode not null");
        String[] codes = functionCode.split("\\.");
        Preconditions.checkArgument(codes.length >= 2, "functionCode illegal");
        return codes[0] + CommonConstant.DOT + codes[1];
    }

}

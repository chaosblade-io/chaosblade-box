package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
public class CompareUtils {

    public static int dateCompare(Date a, Date b) {
        return a.toInstant().compareTo(b.toInstant());
    }

    public static int integerCompare(Integer a, Integer b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return a.compareTo(b);
    }

}

package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.SystemUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author haibin.lhb
 *
 * 
 */
public final class ParamAsserts {

    public static void assertNotNull(Object value) {
        Preconditions.checkArgument(value != null, "内容不为空");
    }

    public static void assertStringsNotEmpty(String value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "内容不为空");
    }

    private static Pattern StringDot = Pattern.compile(
        "(\\w+,)*(\\w+)");

    /**
     * 大于等于
     *
     * @param value
     */
    public static void assertNumberGt(String value, BigDecimal bigDecimal) {
        Preconditions.checkArgument(new BigDecimal(value).compareTo(bigDecimal) > 0, "需要大于:" + bigDecimal.floatValue());
    }

    public static void assertStringSplitterByDot(String value) {
        Preconditions.checkArgument(StringDot.matcher(value).find(), "输入不合法，多个值请用,分割");
    }

    public static void assertNumberGe(String value, BigDecimal bigDecimal) {
        Preconditions.checkArgument(new BigDecimal(value).compareTo(bigDecimal) >= 0,
            "必须大于等于:" + bigDecimal.floatValue());
    }

    public static void assertNumberEq(String value, BigDecimal bigDecimal) {
        Preconditions.checkArgument(new BigDecimal(value).compareTo(bigDecimal) == 0,
            "必须等于:" + bigDecimal.floatValue());
    }

    public static void assertNumberLt(String value, BigDecimal bigDecimal) {
        Preconditions.checkArgument(new BigDecimal(value).compareTo(bigDecimal) < 0, "需要小于:" + bigDecimal.floatValue());
    }

    public static void assertNumberLe(String value, BigDecimal bigDecimal) {
        Preconditions.checkArgument(new BigDecimal(value).compareTo(bigDecimal) <= 0,
            "必须小于等于:" + bigDecimal.intValue());
    }

    public static void assertNumberIn(String value, BigDecimal from, BigDecimal to) {
        BigDecimal bigDecimal = isValidBigDecimal(value);
        Preconditions.checkArgument(bigDecimal.compareTo(from) >= 0 && bigDecimal.compareTo(to) <= 0,
            "必须在[" + from.intValue() + "," + to.intValue() + "]内");
    }

    public static void assertPort(String value) {
        BigDecimal bigDecimal = isValidBigDecimal(value);
        Preconditions.checkArgument(
            bigDecimal.compareTo(new BigDecimal(0)) >= 0 && bigDecimal.compareTo(new BigDecimal(65535)) <= 0,
            "端口必须在[" + 0 + "," + 65535 + "]内");
    }

    private static BigDecimal isValidBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("当前内容非数字:" + value);
        }
    }

    public static void assertPackage(String value) {
        Preconditions.checkArgument(value.contains("."), "需要包含完整的类路径");
    }

    public static void assertValidIp(String value) {
        Preconditions.checkArgument(SystemUtils.isValidIpAddress(value) || SystemUtils.isValidIpSubMask(value),
            "请按照要求输入合法的IP地址,当前输入存在不合法地址:" + value);
    }

    public static void assertNumberLeftIn(String value, BigDecimal from, BigDecimal to) {
        BigDecimal bigDecimal = isValidBigDecimal(value);
        Preconditions.checkArgument(bigDecimal.compareTo(from) >= 0 && bigDecimal.compareTo(to) < 0,
            "须在[" + from.intValue() + "," + to.intValue() + ")内");
    }

    public static void assertNumberRightIn(String value, BigDecimal from, BigDecimal to) {
        BigDecimal bigDecimal = isValidBigDecimal(value);
        Preconditions.checkArgument(bigDecimal.compareTo(from) > 0 && bigDecimal.compareTo(to) <= 0,
            "须在(" + from.intValue() + "," + to.intValue() + "]内");
    }

    public static void assertStringIn(String value, Set<String> options) {
        Preconditions.checkArgument(options.contains(value),
            "须在[" + options.toString() + "]内");
    }
}

package com.alibaba.chaosblade.box.common.common.domain.feedback;

import com.alibaba.chaosblade.box.common.common.util.KVPair;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class OptionInputFormat implements Serializable {

    /**
     * 输入框
     */
    public static String TYPE_INPUT = "input";
    /**
     * radio
     */
    public static String TYPE_RADIO = "radio";

    /**
     * 下拉选择
     */
    public static String TYPE_SELECT = "select";

    /**
     * radio,input,search,select
     */
    String type;

    /**
     * 选项输出,key是唯一标记,value是展示给用户的值
     */
    List<KVPair<String, String>> options;

    /**
     * 默认值
     */
    String defaultValue;

    /**
     * 如果是字符串的话最大长度
     */
    private Integer inputSize;

    /**
     * 是否必须
     */
    private boolean required;

    /**
     * 是否可写
     */
    private boolean writable;
}

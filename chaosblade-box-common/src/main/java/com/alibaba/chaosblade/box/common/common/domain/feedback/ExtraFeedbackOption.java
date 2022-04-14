package com.alibaba.chaosblade.box.common.common.domain.feedback;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class ExtraFeedbackOption implements Serializable {

    /**
     * 每一个选项输入的格式
     */
    private OptionInputFormat format;

    /**
     * 选项的唯一key
     */
    private String key;

    /**
     * 选项的描述信息
     */
    private String description;

    /**
     * 选项的值,如果没有填过为空
     */
    private String value;

}

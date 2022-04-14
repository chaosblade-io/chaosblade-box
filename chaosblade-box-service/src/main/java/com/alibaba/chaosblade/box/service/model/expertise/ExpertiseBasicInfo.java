package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ExpertiseBasicInfo {

    /**
     * 名称
     */
    private String name;

    /**
     * 功能描述
     */
    @JSONField(name = "function_desc")
    private String functionDesc;

    /**
     * 背景描述
     */
    @JSONField(name = "background_desc")
    private String backgroundDesc;

    /**
     * 设计理念
     */
    @JSONField(name = "design_concept")
    private String designConcept;

    /**
     * ExpertiseConstant
     */
    private Integer state;

    /**
     * 标签
     */
    private Set<String> tags = new HashSet<>();

}

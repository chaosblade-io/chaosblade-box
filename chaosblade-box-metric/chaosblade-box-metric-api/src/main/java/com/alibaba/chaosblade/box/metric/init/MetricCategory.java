package com.alibaba.chaosblade.box.metric.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author yefei
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricCategory {

    private String unit;

    private String name;

    private String code;

    private Long parentId;

    private Integer level;

    private String params;

    private List<MetricCategory> subCategories;
}

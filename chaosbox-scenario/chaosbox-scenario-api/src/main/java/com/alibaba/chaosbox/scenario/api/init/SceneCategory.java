package com.alibaba.chaosbox.scenario.api.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author yefei
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SceneCategory {

    private String name;

    private String categoryCode;

    private Long parentId;

    private int level;

    private String supportScope;

    private List<SceneCategory> subCategories;
}

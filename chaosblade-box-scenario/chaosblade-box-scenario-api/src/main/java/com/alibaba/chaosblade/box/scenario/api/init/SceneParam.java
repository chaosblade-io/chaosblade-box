package com.alibaba.chaosblade.box.scenario.api.init;

import com.alibaba.chaosblade.box.common.jackson.JsonToStringDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

/**
 * @author yefei
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SceneParam {

    private String name;

    private String alias;

    private String description;

    @JsonDeserialize(using = JsonToStringDeserializer.class)
    private String component;

}

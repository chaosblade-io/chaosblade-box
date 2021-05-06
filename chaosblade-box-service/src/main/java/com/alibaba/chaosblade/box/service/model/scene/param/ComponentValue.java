package com.alibaba.chaosblade.box.service.model.scene.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComponentValue {

    private String key;

    private String label;

    private String value;
}

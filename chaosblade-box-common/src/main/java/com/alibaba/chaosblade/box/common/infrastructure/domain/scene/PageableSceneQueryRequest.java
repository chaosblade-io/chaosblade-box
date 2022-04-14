package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author jiumu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableSceneQueryRequest extends PageableRequest {

    String sceneId;
    String functionId;
    String parameterId;
    String categoryId;
    Integer phase;
    Integer scopeType;
    List<String> codes;

    /**
     * k8s资源类型
     */
    Integer k8sResourceType;

    /**
     * 应用Id
     */
    private String appId;

    /**
     * 操作系统类型
     */
    Integer osType;

}

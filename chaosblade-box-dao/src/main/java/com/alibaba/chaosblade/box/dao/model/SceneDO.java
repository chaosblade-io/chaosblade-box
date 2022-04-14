package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.BaseChaosMethodDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_scene")
public final class SceneDO extends BaseDO {

    Long id;
    Integer state;
    String sceneId;
    String code;
    String name;
    String description;
    String version;
    String userId;
    Boolean isDelete = false;
    Boolean isPublic = false;
    transient List<SceneFunctionDO> functions;

    public static SceneDO from(ChaosAppDescriptor descriptor) {
        SceneDO scene = new SceneDO();
        scene.setCode(descriptor.getNamespace());
        scene.setName(descriptor.getName());
        scene.setDescription(descriptor.getDescription());
        scene.setVersion(descriptor.getVersion());
        scene.setUserId(descriptor.getEmpId());
        scene.setIsPublic(descriptor.getForPublic());
        List<BaseChaosMethodDescriptor> methodDescriptors = descriptor.getMethodDescriptors();
        if (null != methodDescriptors) {
            scene.setFunctions(
                methodDescriptors.stream()
                    .map(methodDescriptor -> SceneFunctionDO.from(descriptor, methodDescriptor))
                    .collect(Collectors.toList())
            );
        }

        return scene;
    }
}

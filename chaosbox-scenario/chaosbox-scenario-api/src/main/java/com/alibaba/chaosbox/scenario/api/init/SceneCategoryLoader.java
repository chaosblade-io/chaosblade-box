package com.alibaba.chaosbox.scenario.api.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.dao.model.SceneCategoryDO;
import com.alibaba.chaosbox.dao.repository.SceneCategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SceneCategoryLoader implements InitializingBean {

    @Autowired
    private SceneCategoryRepository sceneCategoryRepository;

    @Override
    public void afterPropertiesSet() {
        InputStream stream = ResourceUtil.getStream("scene_category.json");

        List<SceneCategory> categories = JsonUtils.readValue(new TypeReference<List<SceneCategory>>() {
        }, IoUtil.readBytes(stream));

        for (SceneCategory category : categories) {
            SceneCategoryDO categoryDO = SceneCategoryDO.builder()
                    .name(category.getName())
                    .categoryCode(category.getCategoryCode())
                    .level(category.getLevel())
                    .supportScope(category.getSupportScope())
                    .build();
            Optional<SceneCategoryDO> optional = sceneCategoryRepository.selectByCode(category.getCategoryCode());

            optional.ifPresent(sceneCategoryDO -> sceneCategoryRepository.updateByPrimaryKey(sceneCategoryDO.getId(), categoryDO));
            Long parentId = optional.map(SceneCategoryDO::getId).orElseGet(() -> {
                sceneCategoryRepository.insert(categoryDO);
                return categoryDO.getId();
            });

            List<SceneCategory> subCategories = category.getSubCategories();
            if (CollUtil.isNotEmpty(subCategories)) {
                subCategories.forEach(sub -> {
                    SceneCategoryDO subDO = SceneCategoryDO.builder()
                            .name(sub.getName())
                            .categoryCode(sub.getCategoryCode())
                            .level(sub.getLevel())
                            .supportScope(sub.getSupportScope())
                            .parentId(parentId)
                            .build();

                            Optional<SceneCategoryDO> subOption = sceneCategoryRepository.selectByCode(sub.getCategoryCode());
                            if (subOption.isPresent()) {
                                sceneCategoryRepository.updateByPrimaryKey(subOption.get().getId(), subDO);
                            } else {
                                sceneCategoryRepository.insert(subDO);
                            }
                        }
                );

            }
        }
    }
}

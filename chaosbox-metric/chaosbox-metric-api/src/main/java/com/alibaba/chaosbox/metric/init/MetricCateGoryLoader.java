package com.alibaba.chaosbox.metric.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.dao.model.MetricCategoryDO;
import com.alibaba.chaosbox.dao.repository.MetricCategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Component
public class MetricCateGoryLoader implements InitializingBean {

    @Autowired
    private MetricCategoryRepository metricCategoryRepository;

    @Override
    public void afterPropertiesSet() {
        InputStream stream = ResourceUtil.getStream("metric_category.json");

        List<MetricCategory> categories = JsonUtils.readValue(new TypeReference<List<MetricCategory>>() {
        }, IoUtil.readBytes(stream));

        for (MetricCategory category : categories) {
            MetricCategoryDO categoryDO = MetricCategoryDO.builder()
                    .name(category.getName())
                    .code(category.getCode())
                    .level(category.getLevel())
                    .params(category.getParams())
                    .build();
            Optional<MetricCategoryDO> optional = metricCategoryRepository.selectByCode(category.getCode());

            optional.ifPresent(sceneCategoryDO -> metricCategoryRepository.updateByPrimaryKey(sceneCategoryDO.getId(), categoryDO));
            Long parentId = optional.map(MetricCategoryDO::getId).orElseGet(() -> {
                metricCategoryRepository.insert(categoryDO);
                return categoryDO.getId();
            });

            List<MetricCategory> subCategories = category.getSubCategories();
            if (CollUtil.isNotEmpty(subCategories)) {
                subCategories.forEach(sub -> {
                    MetricCategoryDO subDO = MetricCategoryDO.builder()
                            .name(sub.getName())
                            .code(sub.getCode())
                            .level(sub.getLevel())
                            .parentId(parentId)
                            .build();

                            Optional<MetricCategoryDO> subOption = metricCategoryRepository.selectByCode(sub.getCode());
                            if (subOption.isPresent()) {
                                metricCategoryRepository.updateByPrimaryKey(subOption.get().getId(), subDO);
                            } else {
                                metricCategoryRepository.insert(subDO);
                            }
                        }
                );

            }
        }
    }
}

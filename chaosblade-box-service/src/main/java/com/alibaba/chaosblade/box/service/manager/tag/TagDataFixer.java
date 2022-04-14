package com.alibaba.chaosblade.box.service.manager.tag;

import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ThreadPool;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTagRelationDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTagRepository;
import com.alibaba.chaosblade.box.dao.repository.TagRepository;
import com.google.common.base.Strings;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * 用来修复原有的tag数据
 *
 * @author haibin.lhb
 *
 *
 */
@Data
@Component
public class TagDataFixer implements InitializingBean {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ExperimentTagRepository experimentTagRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ThreadPool threadPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        threadPool.executor("tag-fixer").execute(new Runnable() {
            @Override
            public void run() {
                List<ExperimentTagRelationDO> experimentTagRelationDOList = experimentTagRepository
                    .findByTagTypeAndUserIdIsNull(TagDO.TAG_TYPE_EXPERIMENT);
                experimentTagRelationDOList.forEach(
                    experimentTagRelationDO -> tagRepository.findById(experimentTagRelationDO.getTagId())
                        .ifPresent(new Consumer<TagDO>() {
                            @Override
                            public void accept(TagDO tagDO) {
                                if (Strings.isNullOrEmpty(experimentTagRelationDO.getTagName())) {
                                    experimentTagRelationDO.setTagName(tagDO.getName());
                                }
                                experimentRepository.findById(experimentTagRelationDO.getRelationId()).ifPresent(
                                    new Consumer<ExperimentDO>() {
                                        @Override
                                        public void accept(ExperimentDO experimentDO) {
                                            experimentTagRelationDO.setUserId(experimentDO.getUserId());
                                        }
                                    });
                                experimentTagRepository.update(experimentTagRelationDO);
                            }
                        }));
            }
        });
    }
}

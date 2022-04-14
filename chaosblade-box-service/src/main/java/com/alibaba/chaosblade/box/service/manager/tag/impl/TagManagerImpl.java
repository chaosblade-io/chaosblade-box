package com.alibaba.chaosblade.box.service.manager.tag.impl;

import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagSearchRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentTagRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTagQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTagRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.dao.repository.TagRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExpertiseConstant;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class TagManagerImpl implements TagManager {

    @Autowired
    private ExperimentTagRepository experimentTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ExpertiseRepository expertiseRepository;

    @Override
    public void addTags(String userId, String relationId, Integer tagType, List<String> tagNames) {
        experimentTagRepository.deleteByRelationId(relationId, tagType);
        if (!CollectionUtil.isNullOrEmpty(tagNames)) {
            List<TagDO> tagDOS = tagNames.stream().map(
                tagName -> findOrCreateIfNotExistByTagName(userId, tagName, tagType)).collect(Collectors.toList());
            tagDOS.forEach(tagDO -> {
                ExperimentTagRelationDO experimentTagRelationDO = new ExperimentTagRelationDO();
                experimentTagRelationDO.setRelationId(relationId);
                experimentTagRelationDO.setTagType(tagType);
                experimentTagRelationDO.setTagId(tagDO.getTagId());
                experimentTagRelationDO.setTagName(tagDO.getName());
                experimentTagRelationDO.setUserId(userId);
                experimentTagRepository.add(experimentTagRelationDO);
            });
        }
    }

    @Override
    public List<TagDO> findTagsByExperimentId(String experimentId) {
        return experimentTagRepository.findByExperimentId(experimentId).stream().map(
            experimentTagDO -> tagRepository.findById(experimentTagDO.getTagId()).orElse(null)).filter(
            Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<TagDO> findTagsByExpertiseId(String expertiseId) {
        return experimentTagRepository.findByExpertiseId(expertiseId).stream().map(
            experimentTagDO -> tagRepository.findById(experimentTagDO.getTagId()).orElse(null)).filter(
            Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<String> listExperimentTags(String userId) {
        return experimentTagRepository.findDistinctTagsByUserId(userId, TagDO.TAG_TYPE_EXPERIMENT);
    }

    @Override
    public List<String> listUserTagsByType(TagSearchRequest tagSearchRequest) {
        return experimentTagRepository.findUserAndAdminDistinctTagsByUserId(tagSearchRequest.getUserId(),
            tagSearchRequest.getType());
    }

    @Override
    public void unbindTagsByExperimentId(String experimentId) {
        experimentTagRepository.deleteByRelationId(experimentId, TagDO.TAG_TYPE_EXPERIMENT);
    }

    @Override
    public void unbindTagsByExpertiseId(String expertiseId) {
        experimentTagRepository.deleteByRelationId(expertiseId, TagDO.TAG_TYPE_EXPERTISE);
    }

    @Override
    public List<String> searchTagsByUserId(String userId, Integer tagType, String key) {
        ExperimentTagQuery experimentTagQuery = new ExperimentTagQuery();
        experimentTagQuery.setTagType(tagType);
        experimentTagQuery.setUserId(userId);
        experimentTagQuery.setKey(key);
        return experimentTagRepository.findDistinctTagsByUserId(userId, tagType).stream().filter(
            s -> {
                if (Strings.isNullOrEmpty(s) || Strings.isNullOrEmpty(key)) { return true; }
                return s.contains(key);
            }).collect(Collectors.toList());
    }

    @Override
    public List<String> getAdminExpertiseTags() {
        List<String> adminExpertiseId = expertiseRepository.findExpertisesByType(ExpertiseConstant.SYSTEM_EXPERTISE)
            .stream().map(ExpertiseDO::getExpertiseId).collect(Collectors.toList());
        if(CollectionUtil.isNullOrEmpty(adminExpertiseId)) {
            return Collections.emptyList();
        }
        List<ExperimentTagRelationDO> experimentTagRelationDOS = experimentTagRepository.findByExperimentIdIn(
            adminExpertiseId);
        List<TagDO> tagDOS = tagRepository.findByExpertiseIdsIn(
            experimentTagRelationDOS.stream().map(ExperimentTagRelationDO::getTagId).collect(Collectors.toList()));
        return tagDOS.stream().map(TagDO::getName).collect(Collectors.toList());

    }

    @Override
    public TagDO findOrCreateIfNotExistByTagName(String userId, String tagName, Integer tagType) {
        return tagRepository.findByUserIdNameAndType(userId, tagName, tagType).orElseGet(() -> {
            TagDO tagDO = new TagDO();
            tagDO.setUserId(userId);
            tagDO.setCode(tagName);
            tagDO.setName(tagName);
            tagDO.setType(tagType);
            tagRepository.add(tagDO);
            return tagDO;
        });
    }

}

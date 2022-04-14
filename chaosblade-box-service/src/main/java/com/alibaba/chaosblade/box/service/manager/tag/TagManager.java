package com.alibaba.chaosblade.box.service.manager.tag;

import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagSearchRequest;
import com.alibaba.chaosblade.box.dao.model.TagDO;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface TagManager {

    /**
     * 增加标签
     *
     * @param userId
     * @param relationId
     * @param tagType
     * @param tagNames
     */
    void addTags(String userId, String relationId, Integer tagType, List<String> tagNames);

    /**
     * 根据演练ID获取标签
     *
     * @param experimentId 演练ID
     * @return 标签组合
     */
    List<TagDO> findTagsByExperimentId(String experimentId);

    /**
     * 根据专家经验Id获取专家经验库
     *
     * @param expertiseId 经验ID
     * @return
     */
    List<TagDO> findTagsByExpertiseId(String expertiseId);

    /**
     * 搜索用户的演练标签
     *
     * @param userId
     * @return
     */
    List<String> listExperimentTags(String userId);

    /**
     * 根据类型搜索用户标签
     *
     * @param tagSearchRequest
     * @return
     */
    List<String> listUserTagsByType(TagSearchRequest tagSearchRequest);

    /**
     * 解绑tags
     *
     * @param experimentId
     */
    void unbindTagsByExperimentId(String experimentId);

    /**
     * 解绑经验库的标签
     * @param expertiseId
     */
    void unbindTagsByExpertiseId(String expertiseId);

    /**
     * 搜索标签
     *
     * @param userId
     * @param tagType
     * @param key
     * @return
     */
    List<String> searchTagsByUserId(String userId, Integer tagType, String key);

    /**
     * 获取系统的经验库标签
     *
     * @return
     */
    List<String> getAdminExpertiseTags();

    /**
     * 查找标签如果不存在
     * @param userId
     * @param tagName
     * @param tagType
     * @return
     */
    TagDO findOrCreateIfNotExistByTagName(String userId, String tagName, Integer tagType);


}

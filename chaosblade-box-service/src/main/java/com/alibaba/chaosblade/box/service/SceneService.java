package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneUpdateRequest;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;

import java.util.List;

/**
 * @author sunju
 *
 */
public interface SceneService {

    void syncChaosAppScenes();

    /**
     * Query scene information of scene, INCLUDES functions
     *
     * @param sceneId scene id
     *
     * @return scene entity with functions entity and all parameter entities
     */
    SceneDO queryScene(String sceneId);

    /**
     * Query scene information of scene WITHOUT functions
     *
     * @param sceneId scene id
     *
     * @return scene entity ONLY
     */
    SceneDO queryBaseScene(String sceneId);

    /**
     * Search scene by name or code fuzzy(left and right). Excludes draft(state = 0).
     *
     * @param key search keyword
     *
     * @return matched scene entities WITHOUT functions.
     */
    List<SceneDO> search(String key);

    /**
     * Search scene by name or code fuzzy(left and right). Draft(state = 0) only.
     *
     * @param key search keyword
     *
     * @return matched scene entities WITHOUT functions.
     */
    List<SceneDO> searchDraft(String key);

    List<SceneDO> queryScenes();

    /**
     * Query scene by page.
     *
     * @param pageNo   page no. Start with 1.
     * @param pageSize page size. DEFAULT(less than 1) value is 10, MAX(great than 100) value is 100.
     *
     * @return Scene entities WITHOUT functions.
     */
    PageableResponse<SceneDO> queryScenes(int pageNo, int pageSize, SceneQueryRequest queryRequest);

    /**
     * Query user's scene.
     *
     * @param user
     *
     * @return Scene entities WITHOUT functions.
     */
    List<SceneDO> queryUserScenes(ChaosUser user);

    /**
     * Update scene by scene id
     *
     * @param request update scene request
     *
     * @return return TRUE if update success, otherwise return FALSE
     */
    boolean updateSceneBySceneId(SceneUpdateRequest request);

    /**
     * Delete scene by scene id
     *
     * @param sceneId scene id
     *
     * @return return TRUE if update delete, otherwise return FALSE
     */
    boolean deleteSceneBySceneId(ChaosUser user, String sceneId);

    /**
     * 查找或者创建场景如果不存在的话
     *
     * @param user      创建者
     * @param sceneCode 场景code
     *
     * @return
     */
    String findOrCreateIfNotExistBySceneCode(ChaosUser user, String sceneCode);

}

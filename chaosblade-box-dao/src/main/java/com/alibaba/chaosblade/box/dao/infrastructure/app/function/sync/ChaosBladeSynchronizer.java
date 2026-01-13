/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.lock.DistributeLock;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.RetryUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.BaseSceneSynchronizer;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionDeletedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionUpdatedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.SceneFunctionCategoryManager;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneRepository;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author sunju date 9/3/19 */
@Slf4j
@Component
public class ChaosBladeSynchronizer extends BaseSceneSynchronizer {

  @Resource private SceneFunctionRepository sceneFunctionRepository;

  @Resource private SceneRepository sceneRepository;

  @Resource SceneSynchronizerUtil sceneSynchronizerUtil;

  @Autowired private SceneSynchronousHelper sceneSynchronousHelper;

  private final AtomicBoolean inited = new AtomicBoolean(false);

  @Autowired private ChaosEventDispatcher chaosEventDispatcher;

  @Autowired private SceneFunctionCategoryManager sceneFunctionCategoryManager;

  private static final int RETRY_TIMES = 3;

  @Override
  @DistributeLock(
      name = "chaosblade_scene_sync",
      lockAtLeastFor = "5m",
      lockAtMostFor = "10m",
      desc = "为了防止多台机器启动时候同步多次，增加一个锁来限制每次发布只有一台机器进行同步")
  public void syncSceneFunctions(SceneDO scene) throws ChaosException {
    if (inited.compareAndSet(false, true)) {
      log.info("[ChaosBladeSynchronizer] Start to sync ChaosBlade models.");
      if (null == scene) {
        return;
      }
      List<SceneFunctionDO> functions = scene.getFunctions();
      if (CollectionUtil.isNullOrEmpty(functions)) {
        return;
      }

      // 获取ChaosBlade场景ID
      Optional<SceneDO> chaosBladeSceneOptional =
          sceneRepository.findByCode(CommonConstant.CHAOS_BLADE_SCENE_CODE);
      if (chaosBladeSceneOptional.isPresent()) {
        String sceneId = chaosBladeSceneOptional.get().getSceneId();
        // 删除YAML中不存在的场景函数
        deleteRemovedSceneFunctions(sceneId, functions);
      }

      log.info("[ChaosBladeSynchronizer] Sync All functions.");
      functions.forEach(
          new Consumer<SceneFunctionDO>() {
            @Override
            public void accept(SceneFunctionDO sceneFunctionDO) {
              if (ignoreSyncSceneFunction(sceneFunctionDO)) {
                return;
              }
              try {
                SceneSynchronousHelper.FunctionWrapper functionWrapper = wrapper(sceneFunctionDO);
                syncSceneFunction(functionWrapper);
                log.info(
                    "[ChaosBladeSynchronizer] Sync ChaosBlade model successful,code:"
                        + sceneFunctionDO.getCode());
              } catch (Exception ex) {
                log.error("sync function failed,code:{}", sceneFunctionDO.getCode(), ex);
              }
            }
          });
      sceneFunctionCategoryManager.rebindFunctionCategories(
          sceneFunctionRepository.findAvailableFunctions());
      chaosEventDispatcher.fireEvent(new ChaosBladeSyncFinishedOnStartedUpEvent());
      log.info("[ChaosBladeSynchronizer] Sync ChaosBlade models finished.");
    }
  }

  /**
   * 删除YAML中已经不存在的场景函数
   *
   * @param sceneId 场景ID
   * @param functions YAML中存在的场景函数列表
   */
  private void deleteRemovedSceneFunctions(String sceneId, List<SceneFunctionDO> functions)
      throws ChaosException {
    // 获取数据库中该场景下的所有函数
    List<SceneFunctionDO> existFunctions = sceneFunctionRepository.findBySceneId(sceneId);

    // 获取YAML中所有函数的code列表
    List<String> yamlFunctionCodes =
        functions.stream().map(SceneFunctionDO::getCode).collect(Collectors.toList());

    // 遍历数据库中的函数，只删除ChaosBlade来源且YAML中不存在的
    for (SceneFunctionDO existFunction : existFunctions) {
      // 只删除ChaosBlade来源的场景函数，避免误删其他来源（如用户自定义的场景函数）
      if (ChaosFunctionConstant.SOURCE_CHAOS_BLADE.equals(existFunction.getSource())
          && !yamlFunctionCodes.contains(existFunction.getCode())) {
        log.info(
            "[ChaosBladeSynchronizer] Scene function not exists in YAML, will be deleted. code: {}",
            existFunction.getCode());
        boolean deleteResult =
            RetryUtil.retryIfReturnFalse(
                () -> sceneFunctionRepository.deleteByFunctionId(existFunction.getFunctionId()),
                RETRY_TIMES);
        if (!deleteResult) {
          throw new ChaosException(
              CommonErrorCode.B_UPDATE_MINIAPP_FAILED,
              "Delete ChaosBlade scene function failed before sync. code: "
                  + existFunction.getCode());
        }
        chaosEventDispatcher.fireEvent(new SceneFunctionDeletedEvent(existFunction, true));
        log.info(
            "[ChaosBladeSynchronizer] Scene function deleted successfully. code: {}",
            existFunction.getCode());
      }
    }
  }

  private void syncSceneFunction(SceneSynchronousHelper.FunctionWrapper functionWrapper)
      throws ChaosException {

    sceneSynchronizerUtil.checkSceneFunctionBeforePersistence(
        functionWrapper.getUpdateSceneFunction());
    if (functionWrapper.getExistSceneFunction() == null) {
      functionWrapper.getUpdateSceneFunction().setFunctionId(IdWorker.getIdStr());
      sceneSynchronizerUtil.addFunction(functionWrapper.getUpdateSceneFunction());
    } else {
      sceneSynchronizerUtil.updateSceneFunction(
          functionWrapper.getExistSceneFunction(), functionWrapper.getUpdateSceneFunction(), true);
      chaosEventDispatcher.fireEvent(
          new SceneFunctionUpdatedEvent(functionWrapper.getUpdateSceneFunction(), true));
    }
    sceneSynchronizerUtil.addAuthorizedRecord(functionWrapper.getUpdateSceneFunction());
  }

  public SceneSynchronousHelper.FunctionWrapper wrapper(SceneFunctionDO sceneFunction) {
    return sceneSynchronousHelper.useDefaultConfig(sceneFunction);
  }
}

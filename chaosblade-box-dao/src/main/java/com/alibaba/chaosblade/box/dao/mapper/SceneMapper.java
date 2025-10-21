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

package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider.SceneSqlProvider;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

/** @author sunju */
@MybatisMapper
public interface SceneMapper extends BaseMapper<SceneDO> {

  /**
   * search scene entity by name or code fuzzy(left and right)
   *
   * @param keyword key word for search
   * @param state scene state
   * @return scene entity result set matched
   */
  @SelectProvider(type = SceneSqlProvider.class, method = "search")
  List<SceneDO> search(@Param("key") String keyword, @Param("state") Integer state);
}

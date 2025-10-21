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
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.query.ExpertisePageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/** @author haibin */
@MybatisMapper
public interface ExpertiseMapper extends BaseMapper<ExpertiseDO> {

  @Select(
      "<script>"
          + "select "
          + "mkt.* "
          + "from t_chaos_expertise mkt WHERE 1=1 "
          + "<if test='null != query.partName and query.partName != \"\" '>"
          + "AND mkt.name like #{query.partName} "
          + "</if>"
          + "AND"
          + "("
          + "<if test='null != query.userId and query.userId != \"\" '>"
          + " mkt.user_id = #{query.userId} "
          + "</if>"
          + "And mkt.type = 1 or mkt.type=0"
          + ")"
          + "<if test='null != query.state '>"
          + "AND mkt.state = #{query.state} "
          + "</if>"
          + "<if test='null != query.scopeType and query.scopeType != \"\" '>"
          + "AND mkt.scope_type like #{query.scopeType} "
          + "</if>"
          + "<if test='null != query.tagNames and query.tagNames.size != 0'>"
          + " AND mkt.expertise_id IN "
          + "(select experiment_id from t_chaos_experiment_tag t where t.tag_name in "
          + "<foreach collection='query.tagNames' item='tag' index='index' open='(' close=')' separator=',' >"
          + "#{tag}"
          + "</foreach>"
          + "AND t.type = 3"
          + ")"
          + "</if>"
          + "ORDER BY mkt.gmt_create DESC"
          + "</script>")
  IPage<ExpertiseDO> selectUserAndAdminExpertisesByPage(
      IPage<ExpertiseDO> page, @Param("query") ExpertisePageQuery expertisePageQuery);

  @Select(
      "<script>"
          + "select "
          + "mkt.* "
          + "from t_chaos_expertise mkt WHERE 1=1 "
          + "<if test='null != query.partName and query.partName != \"\" '>"
          + "AND mkt.name like #{query.partName} "
          + "</if>"
          + "AND mkt.state != 2 "
          + "<if test='null != query.tagNames and query.tagNames.size != 0'>"
          + " AND mkt.expertise_id IN "
          + "(select experiment_id from t_chaos_experiment_tag t where t.tag_name in "
          + "<foreach collection='query.tagNames' item='tag' index='index' open='(' close=')' separator=',' >"
          + "#{tag}"
          + "</foreach>"
          + "AND t.type = 3"
          + ")"
          + "</if>"
          + "<if test='null != query.userId and query.userId != \"\" '>"
          + "AND mkt.user_id = #{query.userId} "
          + "</if>"
          + "ORDER BY mkt.gmt_create DESC"
          + "</script>")
  IPage<ExpertiseDO> selectUserExpertisesByPage(
      IPage<ExpertiseDO> page, @Param("query") ExpertisePageQuery expertisePageQuery);
}

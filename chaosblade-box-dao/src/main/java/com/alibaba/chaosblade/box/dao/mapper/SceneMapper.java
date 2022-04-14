package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider.SceneSqlProvider;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author sunju
 *
 */
@MybatisMapper
public interface SceneMapper extends BaseMapper<SceneDO> {

    /**
     * search scene entity by name or code fuzzy(left and right)
     *
     * @param keyword key word for search
     * @param state scene state
     *
     * @return scene entity result set matched
     */
    @SelectProvider(type = SceneSqlProvider.class, method = "search")
    List<SceneDO> search(@Param("key") String keyword, @Param("state") Integer state);

}

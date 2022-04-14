package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider.TagSqlProvider;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author sunju
 *
 */
@MybatisMapper
public interface TagMapper extends BaseMapper<TagDO> {

    /**
     * search tag entity by name or code fuzzy(left and right)
     *
     * @param keyword key word for search
     * @return tag entity result set matched
     */
    @SelectProvider(type = TagSqlProvider.class, method = "search")
    List<TagDO> search(@Param("key") String keyword);

}

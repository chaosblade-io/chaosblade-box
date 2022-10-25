package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.mapper.NamespaceMapper;
import com.alibaba.chaosblade.box.dao.model.NamespaceDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author sunju
 * 
 */
@Repository
public class NamespaceRepository {

    private static final Integer DELETED_FLAG = 1;
    private static final Integer NOT_DELETE_FLAG = 0;

    @Resource
    private NamespaceMapper namespaceMapper;

    public Optional<List<NamespaceDO>> getUserNamespace(String userId) {
        if (!Strings.isNullOrEmpty(userId)) {
            QueryWrapper<NamespaceDO> queryWrapper = new QueryWrapper<NamespaceDO>()
                .eq("user_id", userId)
                .eq("is_delete", NOT_DELETE_FLAG);

            return Optional.of(namespaceMapper.selectList(queryWrapper));
        }

        return Optional.empty();
    }

    public Optional<NamespaceDO> getNamespaceByName(String name, String userId, String regionId) {
        if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(userId)) {
            QueryWrapper<NamespaceDO> queryWrapper = new QueryWrapper<NamespaceDO>()
                .eq("user_id", userId)
                .eq("name", name)
                .eq("is_delete", NOT_DELETE_FLAG);

            return Optional.of(namespaceMapper.selectOne(queryWrapper));
        }

        return Optional.empty();
    }

    public boolean addNamespace(NamespaceDO namespace) {
        checkArgument(null != namespace, "Namespace can not be null");
        checkArgument(!Strings.isNullOrEmpty(namespace.getName()), "Namespace's name can not be null or empty");
//        checkArgument(!Strings.isNullOrEmpty(namespace.getNamespace()), "Namespace's name can not be null or empty");
        checkArgument(!Strings.isNullOrEmpty(namespace.getUserId()), "User id can not be null or empty");

        namespace.setIsDelete(NOT_DELETE_FLAG);
        namespace.setDescription(namespace.getDescription());

        List<NamespaceDO> namespaces = namespaceMapper.selectList(
            new QueryWrapper<NamespaceDO>()
                .eq("name", namespace.getName())
                .eq("user_id", namespace.getUserId())
        );
        if (CollectionUtil.isNullOrEmpty(namespaces)) {
            return namespaceMapper.insert(namespace) > 0;
        } else {
            namespaces.stream().filter(namespaceDO -> DELETED_FLAG.equals(namespaceDO.getIsDelete())).forEach(
                namespaceDO -> {
                    namespaceDO.setIsDelete(0);
                    namespaceMapper.updateById(namespaceDO);
                });
            return true;
        }

    }

    public boolean deleteNamespace(String name, String userId) {
        QueryWrapper<NamespaceDO> queryWrapper = new QueryWrapper<NamespaceDO>()
            .eq("name", name)
            .eq("user_id", userId)
            .eq("is_delete", NOT_DELETE_FLAG);

        NamespaceDO exist = namespaceMapper.selectOne(queryWrapper);

        if (null != exist && exist.getUserId().equals(userId)) {
            exist.setIsDelete(DELETED_FLAG);
            int rows = namespaceMapper.updateById(exist);
            return rows > 0;
        }

        return false;
    }

    public NamespaceDO queryNamespace(String userId, String namespace) {
        QueryWrapper<NamespaceDO> queryWrapper = new QueryWrapper<NamespaceDO>()
                .eq("user_id", userId)
                .eq("name", namespace)
                .eq("is_delete", NOT_DELETE_FLAG);
        return namespaceMapper.selectOne(queryWrapper);

    }
}

package com.alibaba.chaosblade.box.dao.repository;


import com.alibaba.chaosblade.box.dao.mapper.LoadTestDefinitionMapper;
import com.alibaba.chaosblade.box.dao.model.LoadTestDefinitionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 压测定义 Repository
 *
 * @author ZhengMingZhuo
 */
@Repository
public class LoadTestDefinitionRepository implements IRepository<String, LoadTestDefinitionDO> {

    @Resource
    @Getter
    private LoadTestDefinitionMapper loadTestDefinitionMapper;

    @Override
    public Optional<LoadTestDefinitionDO> findById(String definitionId) {
        QueryWrapper<LoadTestDefinitionDO> queryWrapper = new QueryWrapper<LoadTestDefinitionDO>()
                .eq("definition_id", definitionId)
                .eq("is_delete", 0);
        return Optional.ofNullable(loadTestDefinitionMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean update(LoadTestDefinitionDO loadTestDefinition) {
        int rows = loadTestDefinitionMapper.updateById(loadTestDefinition);
        return rows > 0;
    }

    @Override
    public boolean add(LoadTestDefinitionDO loadTestDefinition) {
        int rows = loadTestDefinitionMapper.insert(loadTestDefinition);
        return rows > 0;
    }

    /**
     * 分页查询压测定义
     *
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param userId 用户ID
     * @param namespace 命名空间
     * @param name 名称（模糊查询）
     * @param engineType 引擎类型
     * @return 分页结果
     */
    public PageableResponse<LoadTestDefinitionDO> findByPage(int pageNo, int pageSize, 
                                                           String userId, String namespace, 
                                                           String name, String engineType) {
        IPage<LoadTestDefinitionDO> page = MyBatisUtil.getPage(pageNo, pageSize);
        IPage<LoadTestDefinitionDO> result = loadTestDefinitionMapper.selectPageByConditions(
                page, userId, namespace, name, engineType);
        
        if (result == null) {
            return PageableResponse.of(pageNo, pageSize, Lists.newArrayList());
        }
        
        return PageableResponse.of(pageNo, pageSize, result.getRecords())
                .pages(result.getPages())
                .total(result.getTotal());
    }

    /**
     * 查询所有压测定义（不分页）
     *
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测定义列表
     */
    public List<LoadTestDefinitionDO> findAll(String userId, String namespace) {
        QueryWrapper<LoadTestDefinitionDO> queryWrapper = new QueryWrapper<LoadTestDefinitionDO>()
                .eq("is_delete", 0);
        
        if (userId != null && !userId.isEmpty()) {
            queryWrapper.eq("user_id", userId);
        }
        
        if (namespace != null && !namespace.isEmpty()) {
            queryWrapper.eq("namespace", namespace);
        }
        
        queryWrapper.orderByDesc("gmt_create");
        
        return loadTestDefinitionMapper.selectList(queryWrapper);
    }

    /**
     * 逻辑删除压测定义
     *
     * @param definitionId 压测定义ID
     * @return 是否删除成功
     */
    public boolean deleteById(String definitionId) {
        LoadTestDefinitionDO loadTestDefinition = new LoadTestDefinitionDO();
        loadTestDefinition.setDefinitionId(definitionId);
        loadTestDefinition.setIsDelete(1);
        return update(loadTestDefinition);
    }

    /**
     * 根据名称查询压测定义
     *
     * @param name 名称
     * @param userId 用户ID
     * @param namespace 命名空间
     * @return 压测定义
     */
    public Optional<LoadTestDefinitionDO> findByName(String name, String userId, String namespace) {
        QueryWrapper<LoadTestDefinitionDO> queryWrapper = new QueryWrapper<LoadTestDefinitionDO>()
                .eq("name", name)
                .eq("user_id", userId)
                .eq("namespace", namespace)
                .eq("is_delete", 0);
        return Optional.ofNullable(loadTestDefinitionMapper.selectOne(queryWrapper));
    }
}

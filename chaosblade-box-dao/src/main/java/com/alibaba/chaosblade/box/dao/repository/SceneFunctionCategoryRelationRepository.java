package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionCategoryRelationMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class SceneFunctionCategoryRelationRepository
    extends ServiceImpl<SceneFunctionCategoryRelationMapper, SceneFunctionCategoryRelationDO>
    implements IRepository<Long, SceneFunctionCategoryRelationDO> {

    @Autowired
    private SceneFunctionCategoryRelationMapper sceneFunctionCategoryRelationMapper;

    public List<SceneFunctionCategoryRelationDO> findByCategoryId(String categoryId) {
        SceneFunctionCategoryRelationDO sceneFunctionCategoryRelationDO = new SceneFunctionCategoryRelationDO();
        sceneFunctionCategoryRelationDO.setCategoryId(categoryId);
        return sceneFunctionCategoryRelationMapper.selectList(new QueryWrapper<>(sceneFunctionCategoryRelationDO));
    }

    @Override
    public Optional<SceneFunctionCategoryRelationDO> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean update(SceneFunctionCategoryRelationDO sceneFunctionCategoryRelationDO) {
        return false;
    }

    @Override
    public boolean add(SceneFunctionCategoryRelationDO sceneFunctionCategoryRelationDO) {
        return false;
    }

    public int deleteByFunctionCode(String functionCode) {
        SceneFunctionCategoryRelationDO sceneFunctionCategoryRelationDO = new SceneFunctionCategoryRelationDO();
        sceneFunctionCategoryRelationDO.setCode(functionCode);
        return sceneFunctionCategoryRelationMapper.delete(new QueryWrapper<>(sceneFunctionCategoryRelationDO));
    }

    public int countByCategoryId(String categoryId) {
        SceneFunctionCategoryRelationDO sceneFunctionCategoryRelationDO = new SceneFunctionCategoryRelationDO();
        sceneFunctionCategoryRelationDO.setCategoryId(categoryId);
        return sceneFunctionCategoryRelationMapper.selectCount(new QueryWrapper<>(sceneFunctionCategoryRelationDO));
    }

}

package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.MigrationConfigurationMapper;
import com.alibaba.chaosblade.box.dao.model.MigrationConfigurationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MigrationConfigurationRepository
        extends ServiceImpl<MigrationConfigurationMapper, MigrationConfigurationDO>
        implements IRepository<String, MigrationConfigurationDO> {
    @Autowired
    MigrationConfigurationMapper migrationConfigurationMapper;

    @Override
    public Optional<MigrationConfigurationDO> findById(String s) {
       return Optional.empty();
    }

    @Override
    public boolean update(MigrationConfigurationDO migrationConfigurationDO) {
        return migrationConfigurationMapper.updateById(migrationConfigurationDO) > 0;
    }

    @Override
    public boolean add(MigrationConfigurationDO migrationConfigurationDO) {
        return migrationConfigurationMapper.insert(migrationConfigurationDO) > 0;
    }

    public Optional<MigrationConfigurationDO> findByUserId(String s) {

        QueryWrapper<MigrationConfigurationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", s);
        queryWrapper.last("limit 1");
        return Optional.ofNullable(migrationConfigurationMapper.selectOne(queryWrapper));
    }

    public boolean updateMigrationResultByUserId(String userId, String s) {
        UpdateWrapper<MigrationConfigurationDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("migration_result", s);
        updateWrapper.eq("user_id", userId);
        return update(updateWrapper);

    }

}

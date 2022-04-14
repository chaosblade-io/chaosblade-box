package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ScriptMapper;
import com.alibaba.chaosblade.box.dao.model.ScriptDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haibin
 * 
 *
 */
@Repository
public class ScriptRepository implements IRepository<String, ScriptDO> {

    @Autowired
    private ScriptMapper scriptMapper;

    @Override
    public Optional<ScriptDO> findById(String s) {
        return Optional.ofNullable(scriptMapper.selectById(s));
    }

    @Override
    public boolean update(ScriptDO scriptDO) {
        Preconditions.checkArgument(scriptDO.getScriptId() != null, "script id not null");
        Preconditions.checkArgument(scriptDO.getVersion() != null, "script version not null");
        Integer oldVersion = scriptDO.getVersion();
        QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("version", oldVersion);
        queryWrapper.eq("script_id", scriptDO.getScriptId());
        scriptDO.setVersion(oldVersion + 1);
        return scriptMapper.update(scriptDO, queryWrapper) > 0;
    }

    @Override
    public boolean add(ScriptDO scriptDO) {
        scriptDO.setIsDelete(false);
        return scriptMapper.insert(scriptDO) > 0;
    }

    public ScriptDO findByAppCode(String appCode) {
        QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_code", appCode);
        return scriptMapper.selectOne(queryWrapper);
    }

    public ScriptDO findByFunctionId(String functionId) {
        QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("function_id", functionId);
        return scriptMapper.selectOne(queryWrapper);
    }

    public ScriptDO getScriptByScriptId(String scriptId) {
        QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("script_id", scriptId)
            .eq("is_delete", false);
        return scriptMapper.selectOne(queryWrapper);
    }
}

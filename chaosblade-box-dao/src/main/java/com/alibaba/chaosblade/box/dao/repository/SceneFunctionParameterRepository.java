package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneFunctionParameterInterceptors;
import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionParameterMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
@Slf4j
public class SceneFunctionParameterRepository implements IRepository<Long, SceneFunctionParameterDO> {

    @Resource
    private SceneFunctionParameterMapper sceneFunctionParameterMapper;

    @Autowired
    private SceneFunctionParameterInterceptors sceneFunctionParameterInterceptors;

    @Override
    public Optional<SceneFunctionParameterDO> findById(Long id) {
        return Optional.ofNullable(sceneFunctionParameterMapper.selectById(id));
    }

    public SceneFunctionParameterDO findByParameterId(String parameterId) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("parameter_id", parameterId);
        return sceneFunctionParameterMapper.selectOne(queryWrapper);
    }

    public List<SceneFunctionParameterDO> findByParameterIds(List<String> parameterIds) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .in("parameter_id", parameterIds);

        return sceneFunctionParameterMapper.selectList(queryWrapper);
    }

    public boolean existParam(String functionId, String alias) {
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setFunctionId(functionId);
        sceneFunctionParameterDO.setAlias(alias);
        sceneFunctionParameterDO.setIsDelete(false);
        return sceneFunctionParameterMapper.selectCount(new QueryWrapper<>(sceneFunctionParameterDO)) > 0;
    }

    public Optional<List<SceneFunctionParameterDO>> findAllParamsByFunctionId(String functionId) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("is_delete", false)
            .eq("function_id", functionId);
        return Optional.ofNullable(sceneFunctionParameterMapper.selectList(queryWrapper));
    }

    public List<SceneFunctionParameterDO> findFilterParamsByFunctionId(SceneFunctionDO sceneFunctionDO) {
        List<SceneFunctionParameterDO> sceneFunctionParameterDOS = findAllParamsByFunctionId(
            sceneFunctionDO.getFunctionId()).get();
        return sceneFunctionParameterInterceptors.doUserParameterFilter(sceneFunctionDO, sceneFunctionParameterDOS);
    }

    public Optional<List<SceneFunctionParameterDO>> findByFunctionIds(List<String> functionIds) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("is_delete", false)
            .in("function_id", functionIds);
        return Optional.ofNullable(sceneFunctionParameterMapper.selectList(queryWrapper));
    }

    public List<SceneFunctionParameterDO> findByAlias(String functionId, String alias) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("function_id", functionId)
            .eq("alias", alias)
            .eq("is_delete", false);
        return sceneFunctionParameterMapper.selectList(queryWrapper);
    }

    public List<SceneFunctionParameterDO> findAllByAlias(String functionId, String alias) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("function_id", functionId)
            .eq("alias", alias)
            .eq("is_delete", false);

        return sceneFunctionParameterMapper.selectList(queryWrapper);
    }

    public List<SceneFunctionParameterDO> findByFunctionId(String functionId) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("is_delete", false)
            .eq("function_id", functionId);
        return sceneFunctionParameterMapper.selectList(queryWrapper);
    }

    @Override
    public boolean update(SceneFunctionParameterDO sceneFunctionParameter) {
        int rows = sceneFunctionParameterMapper.updateById(sceneFunctionParameter);
        return rows > 0;
    }

    public boolean updateByParameterId(SceneFunctionParameterDO sceneFunctionParameter) {
        SceneFunctionParameterDO whereRule = new SceneFunctionParameterDO();
        whereRule.setFunctionId(sceneFunctionParameter.getFunctionId());
        whereRule.setParameterId(sceneFunctionParameter.getParameterId());
        int rows = sceneFunctionParameterMapper.update(sceneFunctionParameter, new QueryWrapper<>(whereRule));
        return rows > 0;
    }

    @Override
    public boolean add(SceneFunctionParameterDO sceneFunctionParameter) {
        if (null == sceneFunctionParameter.getParameterId()) {
            sceneFunctionParameter.setParameterId(IdWorker.getIdStr());
        }
        int rows = sceneFunctionParameterMapper.insert(sceneFunctionParameter);
        log.info("add param,functionId:{},alias:{},id:{},rows:{}", sceneFunctionParameter.getFunctionId(),
            sceneFunctionParameter.getAlias(), sceneFunctionParameter.getParameterId(),rows);
        return rows > 0;
    }

    public boolean realDeleteByFunctionIdAndAlias(String functionId, String alias) {
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setFunctionId(functionId);
        sceneFunctionParameterDO.setAlias(alias);
        return sceneFunctionParameterMapper.delete(new QueryWrapper<>(sceneFunctionParameterDO)) > 0;

    }

    public boolean deleteByParameterId(String functionId, String parameterId) {
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setFunctionId(functionId);
        sceneFunctionParameterDO.setParameterId(parameterId);
        SceneFunctionParameterDO update = new SceneFunctionParameterDO();
        update.setIsDelete(true);
        return sceneFunctionParameterMapper.update(update, new QueryWrapper<>(sceneFunctionParameterDO)) > 0;
    }

    public boolean deleteByFunctionId(String functionId) {
        if (Strings.isNullOrEmpty(functionId)) {
            return false;
        }
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .eq("function_id", functionId);
        SceneFunctionParameterDO sceneFunctionParameterDO = new SceneFunctionParameterDO();
        sceneFunctionParameterDO.setIsDelete(true);
        return sceneFunctionParameterMapper.update(sceneFunctionParameterDO, queryWrapper) > 0;
    }

    public List<SceneFunctionParameterDO> findInParameterIds(List<String> parameterIds) {
        QueryWrapper<SceneFunctionParameterDO> queryWrapper = new QueryWrapper<SceneFunctionParameterDO>()
            .in("parameter_id", parameterIds);

        return sceneFunctionParameterMapper.selectList(queryWrapper);
    }


}

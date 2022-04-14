package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.service.SceneFunctionParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class SceneFunctionParameterServiceImpl implements SceneFunctionParameterService {

    @Resource
    private SceneFunctionParameterRepository sceneFunctionParameterRepository;

    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired
    private SceneSynchronousHelper sceneSynchronousHelper;

    @Override
    public List<SceneFunctionParameterDO> queryFilterParametersByFunctionId(String functionId) {
        Optional<SceneFunctionDO> optional = sceneFunctionRepository.findByFunctionId(functionId);
        if (!optional.isPresent()) {
            return Collections.emptyList();
        }
        String appCode = optional.get().getCode();
        String hierarchyDefaultCode = sceneSynchronousHelper.getHierarchyDefaultCode(optional.get().getCode());
        if (hierarchyDefaultCode == null) {
            hierarchyDefaultCode = appCode;
        }
        if (!Objects.equals(appCode, hierarchyDefaultCode)) {
            Optional<SceneFunctionDO> defaultScene = sceneFunctionRepository.findByCode(hierarchyDefaultCode);
            if (defaultScene.isPresent()) {
                return sceneFunctionParameterRepository.findFilterParamsByFunctionId(defaultScene.get());
            }
        }
        return sceneFunctionParameterRepository.findFilterParamsByFunctionId(optional.get());
    }


}

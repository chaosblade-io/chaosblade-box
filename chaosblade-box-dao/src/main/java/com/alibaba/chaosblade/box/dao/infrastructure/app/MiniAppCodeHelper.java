package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionRelationDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRelationRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
public class MiniAppCodeHelper {

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired
    private SceneFunctionRelationRepository sceneFunctionRelationRepository;

    /**
     * 获取到可执行的appCode,因为裂变小程序的code是无法裂变出来，不是可执行的，只有父小程序才能被真实的执行,所以如果裂变
     * 小程序需要找到父程序才可以
     *
     * @param sceneFunctionDO 小程序code，可能是父code，也可能是裂变后的code
     * @return 返回可执行的小程序code
     */
    public Optional<SceneFunctionDO> getExecutableSceneFunction(SceneFunctionDO sceneFunctionDO) {
        if (sceneFunctionDO == null) { return Optional.empty();}
        if (ChaosFunctionConstant.SOURCE_FISSION_APP.equals(sceneFunctionDO.getSource())) {
            SceneFunctionRelationDO relation = sceneFunctionRelationRepository.findByFunctionId(
                sceneFunctionDO.getFunctionId());
            if (relation == null) { return Optional.empty(); }
            SceneFunctionDO parentSceneFunction = sceneFunctionRepository
                .findByFunctionId(relation.getOutFunctionId()).orElse(null);
            if (parentSceneFunction != null) {
                return Optional.of(parentSceneFunction);
            }
        }
        return Optional.of(sceneFunctionDO);
    }

    public Optional<SceneFunctionDO> getExecutableSceneFunction(String appCode) {
        SceneFunctionDO sceneFunctionDO = sceneFunctionRepository.findByCode(appCode).orElse(null);
        return getExecutableSceneFunction(sceneFunctionDO);
    }
}

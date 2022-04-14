package com.alibaba.chaosblade.box.dao.infrastructure.app.function;


import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;

/**
 * @author haibin
 *
 *
 */
public class SceneFunctionDeletedEvent extends BaseSceneFunctionEvent {
    public SceneFunctionDeletedEvent(SceneFunctionDO sceneFunctionDO,
                                     boolean system) {
        super(sceneFunctionDO, system);
    }
}

package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;

import java.util.List;

/**
 * @author sunju
 *
 */
public interface SceneFunctionParameterService {

    List<SceneFunctionParameterDO> queryFilterParametersByFunctionId(String functionId);


}

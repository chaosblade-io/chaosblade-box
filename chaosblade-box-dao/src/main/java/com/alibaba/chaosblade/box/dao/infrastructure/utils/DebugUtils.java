package com.alibaba.chaosblade.box.dao.infrastructure.utils;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 * 
 */
@Slf4j
public class DebugUtils {

    public static void recordSceneFunctionSync(String time, SceneFunctionDO update) {
        List<String> params = update.getParameters().stream().map(SceneFunctionParameterDO::getAlias).collect(
            Collectors.toList());
        ;
        log.info("Debug for check:code:{},{},params:{}", update.getCode(), time, params);
    }

}

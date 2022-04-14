package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.google.common.base.Predicate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class SceneFunctionParamsGreyInterceptor implements SceneFunctionParametersInterceptor {

    private Map<String, List<String>> paramsToUserId = new ConcurrentHashMap<>();

    public boolean isAvailableToUser(String paramsAlias, ChaosUser chaosUser) {
        if (chaosUser == null) { return true; }
        if (paramsToUserId.containsKey(paramsAlias)) {
            return paramsToUserId.get(paramsAlias).contains(chaosUser.getUserId());
        }
        return true;
    }

    public void syncParams(Map<String, List<String>> params) {
        paramsToUserId.putAll(params);
    }

    @Override
    public void afterQuery(SceneFunctionDO sceneFunctionDO, List<SceneFunctionParameterDO> parameters) {
        Optional<ChaosApplicationContext> chaosApplicationContext = ChaosRequestContextHolder.getApplicationContext();
        if (!chaosApplicationContext.isPresent()) { return; }
        ChaosUser chaosUser = chaosApplicationContext.get().getLoginUser();
        if (chaosUser == null) { return; }
        parameters.removeIf((Predicate<SceneFunctionParameterDO>)input -> {
            if (input == null) { return false; }
            return !isAvailableToUser(input.getAlias(), chaosUser);
        });
    }

    @Override
    public void beforeSync(SceneSynchronousHelper.FunctionWrapper functionWrapper) {

    }
}

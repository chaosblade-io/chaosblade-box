package com.alibaba.chaosblade.box.common.experiment.risk;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunpeng
 *
 */
@Slf4j
@Component
public class AppRiskMessageLoadFactory implements InitializingBean {

    private static final String LOAD_EXACT_PATH = "app-risk/app_risk_message_exact_match.json";

    private static final String LOAD_FUZZY_PATH = "app-risk/app_risk_message_fuzzy_match.json";

    /**
     * 高位小程序警示信息
     */
    private final Map<String, AppRiskMessage> appRiskExactMap = new HashMap<>();

    private List<AppRiskMessage> appRiskFuzzyList = new ArrayList<>();


    public AppRiskMessage getAppRiskMessage(String code) {
        return appRiskExactMap.getOrDefault(code,null);
    }

    public List<AppRiskMessage> getAppRiskMessageFuzzy() {
        return appRiskFuzzyList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            Type type = new TypeReference<List<AppRiskMessage>>() {}.getType();

            InputStream messageInputStreamExact = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    LOAD_EXACT_PATH);
            List<AppRiskMessage> appRiskMessagesExact = JSON.parseObject(messageInputStreamExact, type);
            if(!CollectionUtil.isNullOrEmpty(appRiskMessagesExact)){
                appRiskMessagesExact.forEach(appRiskMessage -> {
                    appRiskExactMap.put(appRiskMessage.getAppCode(),appRiskMessage);
                });
            }

            InputStream messageInputStreamFuzzy = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                    LOAD_FUZZY_PATH);
            List<AppRiskMessage> appRiskMessagesFuzzy = JSON.parseObject(messageInputStreamFuzzy, type);
            if(!CollectionUtil.isNullOrEmpty(appRiskMessagesFuzzy)){
                appRiskFuzzyList = appRiskMessagesFuzzy;
            }
        } catch (Exception e) {
            log.error("[AppRiskMessageLoad] afterPropertiesSet fail ",e);
        }
    }
}

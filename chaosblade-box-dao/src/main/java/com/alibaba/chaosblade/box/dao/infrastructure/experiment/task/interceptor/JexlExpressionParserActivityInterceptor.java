package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosContextParser;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.activity.Activity;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import jodd.bean.BeanCopy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * jexl表达式解析
 *
 * @author haibin
 *
 * 
 */
@Component
@Slf4j
public class JexlExpressionParserActivityInterceptor extends BaseActivityInvokeInterceptor {

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        String appCode = activityInvokeContext.getExecutableAppCode();
        if (!MiniAppUtils.isUserMiniApp(appCode)) { return true; }
        try {
            Activity activity = activityInvokeContext.getActivity();
            ChaosAppContext context = activityInvokeContext.getStepExecuteContext().getChaosAppContext();
            handleScope(activity, context);
            handleUserArgs(activityInvokeContext, context);
        } catch (Exception e) {
            log.error("[SCOPE PARSER] Parse scope expression failed.", e);
        }
        return true;
    }

    private void handleUserArgs(ActivityInvokeContext activityInvokeContext, ChaosAppContext context) {
        Activity activity = activityInvokeContext.getActivity();
        ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = activity.getArguments();
        if (experimentNodeArgumentsDefinition == null) { return; }
        Map<String, Object> jexlParseResult = new HashMap<>();
        ChaosContextParser parser = ChaosContextParser.getInstance();
        Optional.ofNullable(experimentNodeArgumentsDefinition.getUserArgs()).ifPresent(
            new Consumer<Map<String, String>>() {
                @Override
                public void accept(Map<String, String> stringStringMap) {
                    stringStringMap.entrySet().forEach(new Consumer<Entry<String, String>>() {
                        @Override
                        public void accept(Entry<String, String> stringStringEntry) {
                            String key = stringStringEntry.getKey();
                            String value = stringStringEntry.getValue();
                            if (parser.isExpression(key)) {
                                try {
                                    Object result = parser.eval(context, value);
                                    jexlParseResult.put(key, result);
                                } catch (Exception ex) {
                                    log.error("parser jexl failed,expression:{},error:{}", key, ex.getMessage());
                                }
                            }
                        }
                    });
                }
            });
        activityInvokeContext.getContextData().add(ActivityTaskExecutionAttributes.ATTRIBUTE_JEXL_PARSER_RESULT,
            jexlParseResult);
    }

    private void handleScope(Activity activity, ChaosAppContext chaosAppContext) {
        Optional.ofNullable(activity.getScope())
            .ifPresent(hosts ->
                activity.getDefinition().setScope(
                    hosts.stream()
                        .filter(host -> null != host && !Strings.isNullOrEmpty(host.getIp()))
                        .flatMap(host ->
                            this.parse(chaosAppContext, host.getIp())
                                .stream()
                                .map(ip -> {
                                    Host derivedHost = new Host();
                                    BeanCopy.from(host).to(derivedHost).copy();
                                    derivedHost.setIp(ip);
                                    return derivedHost;
                                })
                        )
                        .collect(Collectors.toList())
                )
            );
    }

    private List<String> parse(ChaosAppContext context, String ip) {
        ChaosContextParser parser = ChaosContextParser.getInstance();
        if (!Strings.isNullOrEmpty(ip) && parser.isExpression(ip)) {
            log.info("[SCOPE PARSER] Parse expression: {}", ip);

            Object value = parser.eval(context, ip);
            if (null != value) {
                Class<?> valueType = value.getClass();
                if (valueType == String.class) {
                    log.info("[SCOPE PARSER] Parsed result: type: String, value: {}", value);
                    return Lists.newArrayList((String)value);
                } else if (List.class.isAssignableFrom(valueType)) {
                    log.info("[SCOPE PARSER] Parsed result: type: List, value: {}", value);
                    return ((List<?>)value)
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList());
                } else if (valueType == String[].class) {
                    log.info("[SCOPE PARSER] Parsed result: type: String Array, value: {}", value);
                    return Lists.newArrayList((String[])value);
                }
            }

            log.info("[SCOPE PARSER] Parsed result: type: UNKNOWN, value: {}", ip);
        }
        return Lists.newArrayList(ip);
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {

    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE - 2;
    }
}

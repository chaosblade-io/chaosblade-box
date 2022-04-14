package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class NetworkPortPreChecker extends ActivityParamPreChecker {

    private static Set<String> portAlias = new HashSet<>();

    static {
        portAlias.add("exclude-port");
        portAlias.add("local-port");
        portAlias.add("remote-port");
    }

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        if (Strings.isNullOrEmpty(sceneArgumentDefinition.getValue())) { return null; }
        String alias = sceneArgumentDefinition.getAlias();
        if (portAlias.contains(alias)) {
            String value = sceneArgumentDefinition.getValue();
            Set<String> ports = new HashSet<>();
            try {
                ports = Splitter.on(",").splitToList(value).stream().flatMap(
                    new Function<String, Stream<String>>() {
                        @Override
                        public Stream<String> apply(String input) {
                            return Splitter.on("-").splitToList(input).stream();
                        }
                    }).collect(Collectors.toSet());
            } catch (Exception exception) {
                throw new IllegalArgumentException("端口格式输入有误");
            }
            for (String port : ports) {
                ParamAsserts.assertNumberIn(port, new BigDecimal(0), new BigDecimal(65535));
            }
        }
        return null;
    }
}

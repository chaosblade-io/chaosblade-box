package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.common.enums.SceneFunctionParameterGradeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentGrade;
import com.alibaba.chaosblade.box.common.infrastructure.miniapp.SceneFunctionParameterGradeProvider;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SceneArgumentGradeConverter {

    @Autowired
    private SceneFunctionParameterGradeProvider sceneFunctionParameterGradeProvider;

    /**
     * 分级格式转list
     *
     * @param sceneArgumentGrades
     * @return
     */
    public List<SceneArgumentDefinition> tranToArgumentsList(List<SceneArgumentGrade> sceneArgumentGrades) {
        if (!CollectionUtil.isNullOrEmpty(sceneArgumentGrades)) {
            List<SceneArgumentDefinition> sceneArgumentDefinitions = new ArrayList<>();
            sceneArgumentGrades.forEach(sceneArgumentGrade -> {
                if(!CollectionUtil.isNullOrEmpty(sceneArgumentGrade.getArgumentList())) {
                    sceneArgumentDefinitions.addAll(sceneArgumentGrade.getArgumentList());
                }
            });
            return sceneArgumentDefinitions;
        }
        return new ArrayList<>();
    }

    /**
     * list转分级格式化
     *
     * @return
     */
    public List<SceneArgumentGrade> tranToArgumentsGrade(List<SceneArgumentDefinition> sceneArgumentDefinitions) {
        List<SceneArgumentGrade> result = new ArrayList<>();
        if (!CollectionUtil.isNullOrEmpty(sceneArgumentDefinitions)) {
            sceneArgumentDefinitions.forEach(sceneArgumentDefinition -> {
                if (null == sceneArgumentDefinition.getGrade()) {
                    //老的演练分级参数为空，需要补充下
                    SceneFunctionParameterGradeEnum gradeEnum = sceneFunctionParameterGradeProvider.getGradeByCode(sceneArgumentDefinition.getAlias());
                    sceneArgumentDefinition.setGrade(null != gradeEnum ? gradeEnum.getValue() : SceneFunctionParameterGradeEnum.HIDE.getValue());
                }
            });
            Map<Integer, List<SceneArgumentDefinition>> map =
                    sceneArgumentDefinitions.stream().collect(Collectors.groupingBy(SceneArgumentDefinition::getGrade));
            result = map.entrySet().stream().map(integerListEntry -> {
                SceneFunctionParameterGradeEnum gradeEnum = SceneFunctionParameterGradeEnum.parse(integerListEntry.getKey());
                //排除掉hide参数
                if (null != gradeEnum && !SceneFunctionParameterGradeEnum.HIDE.equals(gradeEnum)) {
                    SceneArgumentGrade sceneArgumentGrade = new SceneArgumentGrade();
                    sceneArgumentGrade.setArgumentList(integerListEntry.getValue());
                    sceneArgumentGrade.setGradeName(gradeEnum.getName());
                    sceneArgumentGrade.setOrder(gradeEnum.getOrder());
                    sceneArgumentGrade.setOpen(gradeEnum.isOpen());
                    return sceneArgumentGrade;
                }
                return null;
            }).filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(SceneArgumentGrade::getOrder))
                    .collect(Collectors.toList());
        }
        return result;
    }

}

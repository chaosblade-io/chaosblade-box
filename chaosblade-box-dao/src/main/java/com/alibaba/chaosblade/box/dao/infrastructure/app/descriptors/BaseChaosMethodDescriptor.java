package com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors;

import com.alibaba.chaosblade.box.common.app.sdk.SupportScope;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosArgs;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosDependency;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosFunction;
import com.alibaba.chaosblade.box.common.app.sdk.constants.ChaosAppArgumentType;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.alibaba.chaosblade.box.common.experiment.task.flow.util.ReflectUtil.getAllDeclaredFields;

/**
 * @author sunju
 * 
 */
@Slf4j
public abstract class BaseChaosMethodDescriptor {

    public abstract String getCode();

    public abstract String getNamespace();

    public abstract String getName();

    public abstract String getDescription();

    public abstract Method getReference();

    public abstract Boolean isAgentRequired();

    public abstract List<ChaosFunctionArgumentDescriptor> getArgumentDescriptors();

    public abstract List<ChaosFunctionDependencyDescriptor> getDependencyDescriptors();

    public abstract PhaseType[] getPhases();

    public abstract String[] getCategories();

    public abstract SupportScope[] getSupportScopes();

    public abstract void setArgumentDescriptors(List<ChaosFunctionArgumentDescriptor> descriptors);

    static List<ChaosFunctionArgumentDescriptor> getFunctionArgumentDescriptors(Method method) {
        List<Pair<Class<?>, Annotation[]>> pairs = Lists.newArrayList();

        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterTypes.length; i++) {
            pairs.add(Pair.of(parameterTypes[i], parameterAnnotations[i]));
        }

        return pairs
            .stream()
            .flatMap(pair -> {
                Class<?> parameterType = pair.getLeft();
                Annotation[] annotations = pair.getRight();

                Class<?> type = null;
                ChaosAppArgumentType argType = null;

                Optional<Annotation> argsOptional = Arrays.stream(annotations)
                    .filter(annotation -> annotation.annotationType() == ChaosArgs.class)
                    .findFirst();
                if (argsOptional.isPresent()) {
                    ChaosArgs chaosArgs = (ChaosArgs)argsOptional.get();
                    if (Primitives.allPrimitiveTypes().contains(parameterType)
                        || Primitives.allWrapperTypes().contains(parameterType)
                        || parameterType == String.class || ClassUtils.isAssignable(parameterType, List.class)) {
                        return Stream.of(ChaosFunctionArgumentDescriptor.of(chaosArgs, ChaosAppArgumentType.USER_ARGS));
                    } else {
                        if (chaosArgs.converters().length > 0) {
                            return Stream.of(ChaosFunctionArgumentDescriptor.of(chaosArgs, ChaosAppArgumentType.USER_ARGS));
                        } else {
                            type = chaosArgs.type() != void.class ? chaosArgs.type() : parameterType;
                            argType = ChaosAppArgumentType.USER_ARGS;
                            if (null != type) {
                                ChaosAppArgumentType argType0 = argType;
                                List<ChaosFunctionArgumentDescriptor> chaosFunctionArgumentDescriptorStream = Arrays.stream(
                                    getAllDeclaredFields(type, parameterType))
                                    .filter(filed -> filed.isAnnotationPresent(ChaosArgs.class))
                                    .map(filed -> {
                                        ChaosArgs args = filed.getAnnotation(ChaosArgs.class);
                                        return ChaosFunctionArgumentDescriptor.of(filed, args, argType0);
                                    }).collect(Collectors.toList());
                                if (chaosFunctionArgumentDescriptorStream.isEmpty()) {
                                    log.warn("argument descriptor parser result is 0 for parameter:{},type:{}",
                                        chaosArgs.alias(), parameterType.getName());
                                }
                                return chaosFunctionArgumentDescriptorStream.stream();
                            }
                        }
                    }
                }
                return Stream.empty();
            })
            .collect(Collectors.toList());
    }

    static List<ChaosFunctionDependencyDescriptor> getFunctionDependencyDescriptors(Method method, ChaosFunction function) {
        ChaosDependency[] chaosDependencies = function.dependencies();
        return Arrays.stream(chaosDependencies).map(ChaosFunctionDependencyDescriptor::of).collect(Collectors.toList());
    }



}

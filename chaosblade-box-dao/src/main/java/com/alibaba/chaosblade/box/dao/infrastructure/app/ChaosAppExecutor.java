package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.*;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosAction;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosArgs;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosConfig;
import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ChaosAppException;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.TypeUtil;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.BaseChaosMethodDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosFunctionDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.func.ChaosExpressionParser;
import com.google.common.primitives.Primitives;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.util.ChaosAppUtil.getAppNamespace;
import static com.alibaba.chaosblade.box.common.experiment.task.flow.util.ReflectUtil.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author sunju
 */
@Slf4j
public class ChaosAppExecutor {

    private static final ChaosAppExecutor INSTANCE = new ChaosAppExecutor();

    private ChaosAppExecutor() {}

    public static ChaosAppExecutor getInstance() {
        return INSTANCE;
    }

    private static Map<Class<? extends ArgumentTypeConverter>, Object> converterInstanceMap = new HashMap<>();

    public void execute(String namespace, AppCallback callback) {
        this.execute(namespace, null, null, callback);
    }

    public void execute(String namespace, ChaosAppRequest request, ChaosAppContext context, AppCallback callback) {
        try {
            checkArgument(!isNullOrEmpty(namespace), "Can't execute chaosapp. Code is null or empty.");

            String appNamespace = getAppNamespace(namespace, true);

            ChaosAppDescriptor appDescriptor = LocalChaosAppLoader.getInstance().getChaosAppDescriptor(appNamespace);
            if (null == appDescriptor) {
                appNamespace = getAppNamespace(namespace, false);
                appDescriptor = LocalChaosAppLoader.getInstance().getChaosAppDescriptor(appNamespace);
            }
            if (appDescriptor == null) {
                throw new ChaosException(CommonErrorCode.B_MINIAPP_NOT_EXIST,
                    "Can't find any chaosapp descriptor by code[" + namespace + "].");
            }
            BaseChaosMethodDescriptor methodDescriptor = appDescriptor.getMethodDescriptor(namespace);
            if (methodDescriptor == null) {
                throw new ChaosException(CommonErrorCode.B_MINIAPP_NOT_EXIST,
                    "Can't find any executable function descriptor by code[" + namespace + "].");
            }
            ChaosApp app = appDescriptor.getReference();
            if (app == null) {
                throw new ChaosException(CommonErrorCode.B_MINIAPP_NOT_EXIST,
                    "Can't find any chaosapp by code[" + namespace + "].");
            }
            Method method = methodDescriptor.getReference();
            if (method == null) {
                throw new ChaosException(CommonErrorCode.B_MINIAPP_NOT_EXIST,
                    "Can't find any executable function by code[" + namespace + "].");
            }
            checkArgument(method.getReturnType() == ChaosAppResponse.class,
                "Illegal return type of function. MUST return instance of ChaosAppResponse.");
            if (app instanceof ChaosAppContextAware) {
                ((ChaosAppContextAware)app).setContext(context);
            }

            if (app instanceof ChaosToolkitAware) {
                if (null != context) {
                    ((ChaosToolkitAware)app).setChaosToolkit(new ChaosToolkit());
                }
            }

            if (methodDescriptor instanceof ChaosFunctionDescriptor) {
                invokeFunction(app, method, request, context, callback);
            }
        } catch (Exception e) {
            throw new ChaosAppException("Execute chaos app failed for code[" + namespace + "].", e);
        }
    }

    private void invokeFunction(ChaosApp app, Method method, ChaosAppRequest request, ChaosAppContext context,
                                AppCallback callback) {
        PhaseType phaseType = null != context ? context.getPhase() : null;
        Scope scope = null != request ? request.getScope() : null;

        try {
            Object[] args = generateArgument(context, method, request);

            Object response = method.invoke(app, args);

            if (null != response) {
                callback.completed(scope, phaseType, (ChaosAppResponse)response);
            } else {
                callback.completed(scope, phaseType, null);
            }
        } catch (Exception e) {
            log.error("Invoke app function error.", e);
            callback.failed(scope, phaseType, e);
        }
    }

    private Object[] generateArgument(ChaosAppContext context, Method method, ChaosAppRequest request) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        Object[] parameters = new Object[parameterTypes.length];
        if (null == request) {
            return parameters;
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            if (Scope.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = initScope(context, parameterTypes[i], request.getScope());
                continue;
            }

            Object parameter = null;

            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == ChaosAction.class) {
                    Class<?> type = ((ChaosAction)annotation).type() != void.class ? ((ChaosAction)annotation).type()
                        : parameterTypes[i];
                    parameter = initParameter(context, type, request.getAction(), parameter);
                }
                if (annotation.annotationType() == ChaosConfig.class) {
                    Class<?> type = ((ChaosConfig)annotation).type() != void.class ? ((ChaosConfig)annotation).type()
                        : parameterTypes[i];
                    parameter = initParameter(context, type, request.getConfig(), parameter);
                }
                if (annotation.annotationType() == ChaosArgs.class) {
                    Class<?> type = ((ChaosArgs)annotation).type() != void.class ? ((ChaosArgs)annotation).type()
                        : parameterTypes[i];
                    parameter = initParameter(context, (ChaosArgs)annotation, type, request.getUserArgs(), parameter);
                }
            }

            parameters[i] = parameter;
        }

        return parameters;
    }

    private <T, R> T initScope(ChaosAppContext context, Class<T> scopeType, R scope) {
        T instance = newInstance(scopeType);

        if (null == scope) {
            return instance;
        }

        Field[] fields = getAllDeclaredFields(scopeType, Scope.class);

        Field[] fields0 = scopeType == scope.getClass() ? fields : getAllDeclaredFields(scope.getClass(), Scope.class);

        Arrays.stream(fields0)
            .filter(field -> !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
            .forEach(field -> {

                String fieldName = field.getName();
                field.setAccessible(true);

                try {
                    Object value = field.get(scope);
                    if (null != value) {
                        Arrays.stream(fields)
                            .filter(f -> f.getName().equals(fieldName))
                            .findFirst()
                            .ifPresent(f -> {
                                if ("ip".equals(fieldName)) {
                                    invokeSetterByFieldNameSilently(instance, fieldName, evaluateIfy(context, value));
                                } else {
                                    invokeSetterByFieldNameSilently(instance, fieldName, value);
                                }
                            });
                    }
                } catch (IllegalAccessException ignored) {
                }

            });

        return instance;
    }

    private Object initParameter(ChaosAppContext context, ChaosArgs chaosArgs, Class<?> parameterType,
                                 Map<String, ?> fieldValues, Object exist) {
        if (null == fieldValues || fieldValues.isEmpty()) {
            return null;
        }

        String alias = chaosArgs.alias();
        Class<? extends ArgumentTypeConverter>[] converters = chaosArgs.converters();

        if (Primitives.allPrimitiveTypes().contains(parameterType)
            || Primitives.allWrapperTypes().contains(parameterType)
            || parameterType == String.class) {

            Object value = null;

            if (!isNullOrEmpty(alias)) {
                value = fieldValues.get(alias);
            }

            return convertIfy(context, value, parameterType, converters);
        } else {
            if (!isNullOrEmpty(alias)) {
                return convertIfy(context, fieldValues.get(alias), parameterType, converters);
            }

            return initParameter(context, parameterType, fieldValues, exist);
        }
    }

    private Object initParameter(ChaosAppContext context, Class<?> parameterType, Map<String, ?> fieldValues,
                                 Object exist) {
        if (null == fieldValues || fieldValues.isEmpty()) {
            return null;
        }

        Object instance;
        if (null != exist && exist.getClass() == parameterType) {
            instance = exist;
        } else {
            instance = newInstance(parameterType);
        }

        Arrays.stream(parameterType.getDeclaredFields())
            .forEach(field -> {
                Object value = getFieldValue(context, field, fieldValues);
                if (null != value) {
                    invokeSetterByFieldNameSilently(instance, field.getName(), value);
                }
            });

        return instance;
    }

    private Object getFieldValue(ChaosAppContext context, Field field, Map<String, ?> fieldValues) {
        Object value = null;
        Class<? extends ArgumentTypeConverter>[] converters = null;

        String fieldName = field.getName();

        if (field.isAnnotationPresent(ChaosArgs.class)) {
            ChaosArgs fieldConfig = field.getAnnotation(ChaosArgs.class);
            String alias = fieldConfig.alias();
            converters = fieldConfig.converters();

            if (!isNullOrEmpty(alias)) {
                value = fieldValues.get(alias);
            }
        }

        if (null == value) {
            value = fieldValues.get(fieldName);
        }

        return convertIfy(context, value, field.getType(), converters);
    }

    private Object convertIfy(ChaosAppContext context, Object value, Class<?> parameterType,
                              Class<? extends ArgumentTypeConverter>[] converters) {
        if (null == value) {
            return null;
        }

        value = evaluateIfy(context, value);

        if (null != converters && converters.length > 0) {
            //noinspection unchecked
            Class<? extends ArgumentTypeConverter> convert = converters[0];
            Object convertInstance = converterInstanceMap.get(convert);
            if (convertInstance == null) {
                convertInstance = newInstance(convert);
                converterInstanceMap.put(convert, convertInstance);
            }
            value = ((ArgumentTypeConverter)convertInstance).convert(value);
        }
        return TypeUtil.convert(value, parameterType);
    }

    private Object evaluateIfy(ChaosAppContext context, Object value) {
        if (null == context) {
            return value;
        }

        ChaosExpressionParser expressionParser = ChaosExpressionParser.getInstance();
        if (expressionParser.isExpression(value)) {
            return expressionParser.eval(value);
        }

        ChaosContextParser contextParser = ChaosContextParser.getInstance();
        if (contextParser.isExpression(value)) {
            return contextParser.eval(context, value);
        }

        return value;
    }

    private static class AgentAttachCallback implements AppCallback {

        private final AppCallback callback;

        private AgentAttachCallback(AppCallback callback) {this.callback = callback;}

        @Override
        public void agentAttached(Scope scope, PhaseType phase, ChaosAppResponse response) {
        }

        @Override
        public void agentAttachFailed(Scope scope, PhaseType phase, Throwable throwable) {
        }

        @Override
        public void completed(Scope scope, PhaseType phase, ChaosAppResponse response) {
            callback.agentAttached(scope, phase, response);
        }

        @Override
        public void failed(Scope scope, PhaseType phase, Throwable throwable) {
            callback.agentAttachFailed(scope, phase, throwable);
        }
    }

}

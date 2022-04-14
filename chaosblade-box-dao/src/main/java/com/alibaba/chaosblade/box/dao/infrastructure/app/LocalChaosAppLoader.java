package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosApp;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.*;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ChaosAppException;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.BaseChaosMethodDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosFunctionDescriptor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosListenerDescriptor;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Loader for Chaos App. Load all appResults by SPI.
 *
 * @author sunju
 */
@Slf4j
public final class LocalChaosAppLoader implements ChaosAppLoader {

    private final Map<String, ChaosAppDescriptor> loadedChaosApps = new ConcurrentHashMap<>();

    private static LocalChaosAppLoader chaosAppLoader;

    private static AtomicBoolean inited = new AtomicBoolean(false);

    private List<MiniAppProcessor> miniAppProcessors;

    private LocalChaosAppLoader(ChaosAppLoaderConfig chaosAppLoaderConfig) {
        this.miniAppProcessors = chaosAppLoaderConfig.getMiniAppProcessors();
    }

    public static void init(ChaosAppLoaderConfig chaosAppLoaderConfig) {
        assert chaosAppLoaderConfig != null;
        if (inited.compareAndSet(false, true)) {
            LocalChaosAppLoader loader = new LocalChaosAppLoader(chaosAppLoaderConfig);
            loader.load();
            chaosAppLoader = loader;
        }
    }

    public static LocalChaosAppLoader getInstance() {
        if (chaosAppLoader == null || !inited.get()) {
            throw new IllegalStateException("please call init() first");
        }
        return chaosAppLoader;
    }

    @Override
    public ChaosAppDescriptor getChaosAppDescriptor(String namespace) {
        checkArgument(!isNullOrEmpty(namespace), "Get ChaosApp descriptor failed. ChaosApp code can't be null or empty.");

        ChaosAppDescriptor descriptor = loadedChaosApps.get(namespace);

        if (null == descriptor) {
            return loadChaosApp(namespace);
        }

        return descriptor;
    }

    @Override
    public Collection<ChaosAppDescriptor> getAllChaosAppDescriptors() {
        return this.loadedChaosApps.values();
    }

    @Override
    public void load() {
        if (!loadedChaosApps.isEmpty()) {
            loadedChaosApps.clear();
        }

        ServiceLoader<ChaosApp> apps = ServiceLoader.load(ChaosApp.class);
        try {
            for (ChaosApp app : apps) {
                ChaosApplication configuration = app.getClass().getAnnotation(ChaosApplication.class);
                if (null != configuration && !Strings.isNullOrEmpty(configuration.code())) {
                    ChaosAppDescriptor descriptor = ChaosAppDescriptor.of(app, configuration);
                    descriptor.setMethodDescriptors(getMethodDescriptors(app, configuration.code()));

                    setAuthor(app, descriptor);
                    // check duplicated app, code of chaosapp must be unique
                    if (null != loadedChaosApps.get(descriptor.getNamespace())) {
                        throw new ChaosAppException("Duplicated ChaosApp for code[" + descriptor.getNamespace() + "].");
                    }
                    applyAfterInitChaosApp(descriptor);
                    loadedChaosApps.put(descriptor.getNamespace(), descriptor);
                } else {
                    throw new ChaosAppException("ChaosApp[" + app.getClass().getSimpleName()
                        + "] no annotation[ChaosAppConfiguration] present or code is empty.");
                }
            }
        } catch (Exception ex) {
            log.error("init app failed", ex);
        }
    }

    private void applyAfterInitChaosApp(ChaosAppDescriptor chaosAppDescriptor) {
        if (miniAppProcessors != null) {
            for (MiniAppProcessor miniAppProcessor : miniAppProcessors) {
                miniAppProcessor.afterInit(chaosAppDescriptor);
            }
        }
    }

    private ChaosAppDescriptor loadChaosApp(String namespace) {
        if (Strings.isNullOrEmpty(namespace)) {
            return null;
        }

        ChaosAppDescriptor exist = loadedChaosApps.get(namespace);
        if (null != exist) {
            return exist;
        }

        ServiceLoader<ChaosApp> apps = ServiceLoader.load(ChaosApp.class);
        for (ChaosApp app : apps) {
            ChaosApplication configuration = app.getClass().getAnnotation(ChaosApplication.class);
            if (null != configuration && !Strings.isNullOrEmpty(configuration.code())) {
                ChaosAppDescriptor descriptor = ChaosAppDescriptor.of(app, configuration);

                setAuthor(app, descriptor);

                descriptor.setMethodDescriptors(getMethodDescriptors(app, configuration.code()));

                if (namespace.equals(descriptor.getNamespace())) {
                    loadedChaosApps.put(descriptor.getNamespace(), descriptor);
                    return descriptor;
                }
            }
        }
        return null;
    }

    private void setAuthor(ChaosApp app, ChaosAppDescriptor descriptor) {
        Author author = app.getClass().getAnnotation(Author.class);
        if (null != author) {
            if (!Strings.isNullOrEmpty(author.empId())) {
                descriptor.setEmpId(author.empId());
            }
            if (!Strings.isNullOrEmpty(author.userId())) {
                descriptor.setEmpId(author.userId());
            }

            descriptor.setForPublic(author.forPublic());
        }
    }

    private List<BaseChaosMethodDescriptor> getMethodDescriptors(ChaosApp app, String appCode) {
        List<BaseChaosMethodDescriptor> methodDescriptors = new ArrayList<>();
        for (Method method : app.getClass().getMethods()) {
            if (isInvokableMethod(method)) {
                if (method.isAnnotationPresent(ChaosFunction.class)) {
                    ChaosFunction function = method.getAnnotation(ChaosFunction.class);
                    methodDescriptors.add(ChaosFunctionDescriptor.of(appCode, method, function));
                }
                if (method.isAnnotationPresent(ChaosListener.class)) {
                    ChaosListener listener = method.getAnnotation(ChaosListener.class);
                    methodDescriptors.add(ChaosListenerDescriptor.of(appCode, method, listener));
                }
                if (method.isAnnotationPresent(ChaosFunctions.class)) {
                    ChaosFunctions chaosFunctions = method.getAnnotation(ChaosFunctions.class);
                    for (ChaosFunction chaosFunction : chaosFunctions.values()) {
                        ChaosFunctionDescriptor chaosFunctionDescriptor = ChaosFunctionDescriptor.of(appCode, method,
                                chaosFunction);
                        mergeMetadata(chaosFunctionDescriptor, chaosFunctions);
                        methodDescriptors.add(chaosFunctionDescriptor);
                    }
                }
            }
        }
        return methodDescriptors;
    }

    private void mergeMetadata(ChaosFunctionDescriptor chaosFunctionDescriptor, ChaosFunctions chaosFunctions) {
        if (Strings.isNullOrEmpty(chaosFunctionDescriptor.getDescription())) {
            chaosFunctionDescriptor.setDescription(chaosFunctions.description());
        }
        chaosFunctionDescriptor.setInvokeMode(chaosFunctions.mode());
    }

    private boolean isInvokableMethod(Method method) {
        return Modifier.isPublic(method.getModifiers())
            && !Modifier.isStatic(method.getModifiers())
            && ChaosAppResponse.class.isAssignableFrom(method.getReturnType())
            && (method.isAnnotationPresent(ChaosFunction.class) || method.isAnnotationPresent(ChaosListener.class)) || method
            .isAnnotationPresent(
                ChaosFunctions.class);
    }



}

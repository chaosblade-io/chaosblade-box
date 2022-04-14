package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
public final class ResourceUtils {

    public static InputStream loadResource(String classPath, ClassLoader classLoader) {
        ClassLoader loader = classLoader;
        if (classLoader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader.getResourceAsStream(classPath);
    }

    public static <T> List<T> getResources(ApplicationContext applicationContext, Class<T> tClass, String properties,
        List<String> configs) {
        List<Resource> resources = getResources(applicationContext, properties, configs, true);
        List<T> instances = new ArrayList<>();
        for (Resource resource : resources) {
            EncodedResource encodedResource = new EncodedResource(resource, "utf-8");
            Reader reader = null;
            try {
                reader = encodedResource.getReader();
                String content = IOUtils.toString(reader);
                T instance = JSON.parseObject(content, tClass);
                instances.add(instance);
            } catch (IOException e) {
                log.error("read config failed", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
        return instances;
    }

    public static List<Resource> getResources(ApplicationContext applicationContext, String propertyName,
        List<String> locations,
        boolean validate) {
        List<Resource> resources = new ArrayList<Resource>();
        for (String location : locations) {
            for (Resource resource : doGetResources(location, applicationContext)) {
                if (resource.exists()) {
                    resources.add(resource);
                } else if (validate) {
                    throw new ResourceNotFoundException(propertyName, resource);
                }
            }
        }
        return resources;
    }

    public static Resource[] doGetResources(String location, ApplicationContext applicationContext) {
        try {
            SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(applicationContext,
                Collections.singletonList(location));
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load resources from " + location,
                ex);
        }
    }

}

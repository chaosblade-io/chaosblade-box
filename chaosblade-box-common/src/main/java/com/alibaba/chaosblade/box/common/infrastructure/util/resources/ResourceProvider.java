package com.alibaba.chaosblade.box.common.infrastructure.util.resources;

import java.net.URL;

/**
 * @author haibin.lhb
 *
 *
 */
public abstract class ResourceProvider {
    /**
     * Return the resource URL specified by the given String if it is to be overwritten.
     *
     * @param name           the name of the resource
     * @param resourceBundle the class for which a resource should be loaded
     * @return the resource, or {@code null} if no overwritten resource is found
     */
    public abstract URL getResource(String name, Class<?> resourceBundle);

    /**
     * Set a new system-wide {@link ResourceProvider}.
     *
     * @param p singleton instance
     * @throws IllegalArgumentException if the parameter is {@code null}.
     */
    public static void setProvider(ResourceProvider p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        theInstance = p;
    }

    /**
     * Gets the currently installed system-wide {@link ResourceProvider}.
     *
     * @return always non-null.
     */
    public static ResourceProvider getProvider() {
        return theInstance;
    }

    /**
     * Short for {@code getProvider().getResource()}
     */
    public static URL findResource(String name, Class<?> resourceBundleClass) {
        return theInstance.getResource(name, resourceBundleClass);
    }

    /**
     * The default resource provider which just calls {@link Class#getResource(String)}.
     */
    public static final ResourceProvider DEFAULT = new ResourceProvider() {
        @Override
        public URL getResource(String name, Class<?> resourceBundleClass) {
            URL url = resourceBundleClass.getClassLoader().getResource("i18n/" + name);
            if (url == null) {
                url = resourceBundleClass.getResource(name);
            }
            return url;
        }
    };

    private static volatile ResourceProvider theInstance = DEFAULT;
}

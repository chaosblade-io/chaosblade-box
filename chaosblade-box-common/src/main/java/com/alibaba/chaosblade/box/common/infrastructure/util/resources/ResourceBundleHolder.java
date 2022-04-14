package com.alibaba.chaosblade.box.common.infrastructure.util.resources;

/**
 * @author haibin.lhb
 *
 *
 */

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maintains {@link ResourceBundle}s per locale.
 */
public final class ResourceBundleHolder implements Serializable {
    /**
     * Need to cache, but not tie up a classloader refernce in cases of unloading
     */
    private static final Map<Class<?>, WeakReference<ResourceBundleHolder>> cache =
        new WeakHashMap<>();

    /**
     * Gets a {@link ResourceBundleHolder} for the given class,
     * by utilizing a cache if possible.
     */
    public synchronized static ResourceBundleHolder get(Class<?> clazz) {
        WeakReference<ResourceBundleHolder> entry = cache.get(clazz);
        if (entry != null) {
            ResourceBundleHolder rbh = entry.get();
            if (rbh != null) { return rbh; }
        }

        ResourceBundleHolder rbh = new ResourceBundleHolder(clazz);
        cache.put(clazz, new WeakReference<>(rbh));
        return rbh;
    }

    private transient final Map<Locale, ResourceBundle> bundles = new ConcurrentHashMap<Locale, ResourceBundle>();
    public final Class<?> owner;

    /**
     * {@link Locale} object that corresponds to the base bundle.
     */
    private static final Locale ROOT = new Locale("");

    /**
     * @param owner The name of the generated resource bundle class.
     * @deprecated Use {@link #get(Class)}
     */
    public ResourceBundleHolder(Class<?> owner) {
        this.owner = owner;
    }

    /**
     * Work around deserialization issues.
     */
    private Object readResolve() throws ObjectStreamException {
        return get(owner);
    }

    /**
     * Loads {@link ResourceBundle} for the locale.
     */
    public ResourceBundle get(Locale locale) {
        ResourceBundle rb = bundles.get(locale);
        if (rb != null) { return rb; }

        // try to update the map
        synchronized (this) {
            rb = bundles.get(locale);
            if (rb != null) { return rb; }

            // turns out this is totally unsable because the getBundle method
            // always checks Locale.getDefault() and that wins over the bundle for the root locale. 
            // bundles.put(locale, rb=ResourceBundle.getBundle(owner.getName(),locale,owner.getClassLoader()));

            Locale next = getBaseLocale(locale);
            String s = locale.toString();
            URL res = ResourceProvider.findResource(
                owner.getSimpleName() + (s.length() > 0 ? '_' + s : "") + ".properties", owner);
            if (res != null) {
                // found property file for this locale.
                try {
                    URLConnection uc = res.openConnection();
                    uc.setUseCaches(false);
                    InputStream is = uc.getInputStream();
                    ResourceBundleImpl
                        bundle = new ResourceBundleImpl(is);
                    is.close();
                    rb = bundle;
                    if (next != null) { bundle.setParent(get(next)); }
                    bundles.put(locale, bundle);
                } catch (IOException e) {
                    MissingResourceException x = new MissingResourceException("Unable to load resource " + res,
                        owner.getName(), null);
                    x.initCause(e);
                    throw x;
                }
            } else {
                if (next != null)
                // no matching resource, so just use the locale for the base
                {
                    rb = get(next);
                    if (rb != null) {
                        bundles.put(locale, rb);
                    }
                }
            }

        }
        return rb;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + owner.getName() + "]";
    }

    static class ResourceBundleImpl extends PropertyResourceBundle {
        ResourceBundleImpl(InputStream stream) throws IOException {
            super(stream);
        }

        @Override
        protected void setParent(ResourceBundle parent) {
            super.setParent(parent);
        }

    }

    /**
     * Returns the locale to fall back to.
     */
    private Locale getBaseLocale(Locale l) {
        if (l.getVariant().length() > 0) { return new Locale(l.getLanguage(), l.getCountry()); }
        if (l.getCountry().length() > 0) { return new Locale(l.getLanguage()); }
        if (l.getLanguage().length() > 0) { return ROOT; }
        return null;
    }

    /**
     * Formats a resource specified by the given key by using the default locale
     */
    public String format(String key, Object... args) throws UnsupportedEncodingException {
        boolean zh = "zh".equals(LocaleProvider.getLocale().getLanguage());
        String value = get(LocaleProvider.getLocale()).getString(key);
        if (zh) {

            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");

        }
        return MessageFormat.format(value, args);
    }

    /**
     * Clear the cache used by {@link #get(Class)}. This is useful in case of changes to {@link ResourceProvider}.
     */
    public synchronized static void clearCache() {
        cache.clear();
    }
}


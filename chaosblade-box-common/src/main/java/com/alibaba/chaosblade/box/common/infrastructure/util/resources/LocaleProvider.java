package com.alibaba.chaosblade.box.common.infrastructure.util.resources;

/**
 * @author haibin.lhb
 *
 * 
 */
/*
 * The MIT License
 *
 * Copyright (c) 2007-, the localizer project contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.util.Locale;

/**
 * Determines the locale, normally from the context.
 *
 * <p>
 * For example, in webapps, you might use the current request's <tt>Accept-Language</tt>
 * header, or maybe it's just an invocation to {@link Locale#getDefault()}.
 *
 * <p>
 */
public abstract class LocaleProvider {
    /**
     * Determines the locale to be used.
     *
     * @return must not be null.
     */
    public abstract Locale get();

    public static void setProvider(LocaleProvider p) {
        if (p == null) { throw new IllegalArgumentException(); }
        theInstance = p;
    }

    /**
     * @return always non-null.
     */
    public static LocaleProvider getProvider() {
        return theInstance;
    }

    /**
     * Short for {@code getProvider().get()}
     */
    public static Locale getLocale() {
        return theInstance.get();
    }

    /**
     * {@link LocaleProvider} that uses {@link Locale#getDefault()}.
     */
    public static final LocaleProvider DEFAULT = new LocaleProvider() {
        @Override
        public Locale get() {
            return Locale.getDefault();
        }
    };

    private static volatile LocaleProvider theInstance = DEFAULT;
}

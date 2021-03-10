/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.common.utils;

import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;

/**
 * @author yefei
 */
public class Preconditions {

    private Preconditions() {
    }

    public static <T> T checkNotNull(T reference, BizException bizException) {
        if (reference == null) {
            throw bizException;
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, ExceptionMessageEnum exceptionMessageEnum) {
        if (reference == null) {
            throw new BizException(exceptionMessageEnum);
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, String message) {
        if (reference == null) {
            throw new BizException(message);
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, ExceptionMessageEnum exceptionMessageEnum, T data) {
        if (reference == null) {
            throw new BizException(exceptionMessageEnum, data);
        } else {
            return reference;
        }
    }

    public static void checkArgument(boolean b, ExceptionMessageEnum exceptionMessageEnum) {
        if (b) {
            throw new BizException(exceptionMessageEnum);
        }
    }
}

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

package com.alibaba.chaosbox.service.task.log.i18n;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author yefei
 */
public class TaskLogUtil {

    public static void info(Logger logger, TaskLogType taskLogType, Long taskId, String... args) {
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);

        String[] arguments = new String[]{String.valueOf(taskId)};
        if (args != null) {
            arguments = ArrayUtil.addAll(arguments, args);
        }
        logger.info(messageSource.getMessage(taskLogType.getCode(), arguments, LocaleContextHolder.getLocale()), taskId);
    }

    public static void error(Logger logger, TaskLogType taskLogType, Long taskId, Throwable throwable, String... args) {
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);

        String[] arguments = new String[]{String.valueOf(taskId)};
        if (args != null) {
            arguments = ArrayUtil.addAll(arguments, args);
        }
        logger.error(messageSource.getMessage(taskLogType.getCode(), arguments, LocaleContextHolder.getLocale()), taskId, throwable);
    }
}

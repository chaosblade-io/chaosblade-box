/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.box.common.sdk.entity;

/**
 * @author Changjun Xiao
 */
public class MatcherSpecBean {
    private String name;
    private String desc;
    private String defaultValue;
    private boolean noArgs;
    private boolean required;
    private boolean requiredWhenDestroyed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNoArgs() {
        return noArgs;
    }

    public void setNoArgs(boolean noArgs) {
        this.noArgs = noArgs;
    }

    public void setDefaultValue(String defaultValue){
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequiredWhenDestroyed() {
        return requiredWhenDestroyed;
    }

    public void setRequiredWhenDestroyed(boolean requiredWhenDestroyed) {
        this.requiredWhenDestroyed = requiredWhenDestroyed;
    }
}

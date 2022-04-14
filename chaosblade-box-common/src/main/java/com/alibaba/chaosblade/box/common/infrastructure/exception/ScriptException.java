package com.alibaba.chaosblade.box.common.infrastructure.exception;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author haibin
 * 
 *
 */
public class ScriptException extends RuntimeException {

    private List<String> scriptStack;
    private String scriptId;
    private String lang;

    public ScriptException(String message, Throwable cause, List<String> scriptStack, String scriptId, String lang) {
        super(message, cause);
        this.scriptStack = Collections.unmodifiableList(Objects.requireNonNull(scriptStack));
        this.scriptId = Objects.requireNonNull(scriptId);
        this.lang = Objects.requireNonNull(lang);
    }

    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }




}

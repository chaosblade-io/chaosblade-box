package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionDependency {

    /**
     * 前置依赖
     */
    public static final Integer TYPE_BEFORE = 0;

    /**
     * 后置依赖
     */
    public static final Integer TYPE_AFTER = 1;

    Integer type;

    String code;
    Integer phase;

    public boolean isBefore() {
        return Objects.equals(TYPE_BEFORE, type);
    }

    public boolean isAfter() {
        return Objects.equals(TYPE_AFTER, type);
    }

    boolean required = true;

    public SceneFunctionDependency(Integer type, String code, Integer phase) {
        this.type = type;
        this.code = code;
        this.phase = phase;
    }

    public static SceneFunctionDependency before(String code, PhaseType phaseType) {
        return new SceneFunctionDependency(TYPE_BEFORE, code, phaseType.getType());
    }

    public static SceneFunctionDependency after(String code, PhaseType phaseType) {
        return new SceneFunctionDependency(TYPE_AFTER, code, phaseType.getType());
    }
}

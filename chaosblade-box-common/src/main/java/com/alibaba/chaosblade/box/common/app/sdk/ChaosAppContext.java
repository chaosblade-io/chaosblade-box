package com.alibaba.chaosblade.box.common.app.sdk;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.ActivityScope;
import lombok.Data;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunju
 */
@ToString
@Data
public final class ChaosAppContext {

    /**
     * Running environment
     */
    private EnvironmentEnum environment;

    /**
     * Phase which current node belongs. Such as PREPARE,ATTACK,CHECK,RECOVER
     */
    private PhaseType phase;

    /**
     * current activity scope info ,if activity has no scopes,the value is null
     */
    private ActivityScope activityScope;

    /**
     * userId
     */
    private String userId;

    private String subUserId;

    private String stsToken;

    /**
     * currentNamespace
     */
    private String namespace;

    /**
     * Context data, could be read by all nodes in flow.
     *
     * Your app MUST extends {@link BaseChaosApp} or implements {@link ChaosAppContextAware} interface.
     *
     * Example:
     *
     * <p>In ChaosApp</p>
     * <code>
     * Object someValue = getContext().getData("some key"); // get data from context
     * getContext().setData("some key", someValue); // set data to context
     * </code>
     * <br/>
     *
     * <p>Expression</p>
     * <p>Expression MUST surround with #{}. $context is inner variable for access context object.</p>
     * <code>
     * // get phase
     * #{$context.phase}
     *
     * // get data value
     * getContext().addData("name", "Jack Ma");
     * #{$context.data.name}
     * </code>
     */
    private Map<String, Object> data = new ConcurrentHashMap<>();

    public void addData(String key, Object value) {
        if (null == this.data) {
            this.data = new ConcurrentHashMap<>();
        }

        if (null != key && !key.isEmpty() && null != value) {
            this.data.put(key, value);
        }
    }

    public Object getData(String key) {
        if (null == this.data) {
            return null;
        }

        if (null != key && !key.isEmpty()) {
            return this.data.get(key);
        }

        return null;
    }

    public void setData(Map<String, Object> data) {
        if (null == data) {
            this.data = new ConcurrentHashMap<>();
        } else {
            this.data = data;
        }
    }

    public ChaosAppContext() {

    }
}

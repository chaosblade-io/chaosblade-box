package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.infrastructure.configuration.ConfigurationScope;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author haibin.lhb
 *
 * 
 */
@Data
public class ApplicationConfiguration {

    public static Integer STATUS_NORMAL = 0;

    public static Integer STATUS_INVALID = 1;

    /**
     * k8s默认容器名称
     */
    public static String CONFIG_K8S_DEFAULT_CONTAINER_NAME = "config.k8s.container.name";

    /**
     * 默认进程名称
     */
    public static String CONFIG_JAVA_DEFAULT_PROCESS_NAME = "config.java.process.name";

    private String name;

    private String alias;

    private String value;

    private ConfigurationComponent component = new ConfigurationComponent();

    private ConfigurationScope scope = new ConfigurationScope();

    /**
     * 对应的参数alias
     */
    private String functionParamAlias;

    private Integer priority;

    private String appId;

    private String description;

    /**
     * 是否启用
     */
    private Integer status;

    private Integer phaseFlag;

    /**
     * 应用类型,允许为null，此时表示支持所有的应用类型
     *
     * @see
     */
    private Integer appType;

    /**
     * 是否用配置值来覆盖用户的输入值
     */
    private Boolean override = false;

    @JSONField(name = "gmt_modified")
    private Date gmtModified = new Date();

    public boolean support(PhaseType phaseType, String appCode) {
        return supportAppCode(appCode) && supportPhase(phaseType);
    }

    public boolean supportAppCode(String appCode) {
        if (appCode == null) {
            return false;
        }
        if (scope.getAppCodes().isEmpty()) {
            return true;
        }
        return scope.getAppCodes().stream().anyMatch(
            s -> s.equals(appCode) || appCode.startsWith(s) || appCode.endsWith(s));
    }

    public boolean supportPhase(PhaseType phaseType) {
        return phaseFlag != null && PhaseType.isSupport(phaseFlag, phaseType.getCompareFlag());
    }

}

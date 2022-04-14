package com.alibaba.chaosblade.box.common.common.domain.application;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class HostFilter {

    /**
     * 全部返回
     */
    public static String TYPE_ALL = "all";

    /**
     * 任意返回一台
     */
    public static String TYPE_ANY = "any";

    /**
     * 按百分比返回
     */
    public static String TYPE_PERCENT = "percent";

    /**
     * 暂时没用
     */
    public static String TYPE_SPECIFIED = "specified";

    /**
     * 过滤的类型，必须要填
     */
    private String type;

    /**
     * 需要返回的百分比，请保证type是:TYPE_PERCENT
     */
    private Integer percent;

    /**
     * 用户指定的IP地址列表，需要过滤掉不合法的IP
     */
    private List<String> hosts;

}

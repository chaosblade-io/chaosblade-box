package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.common.common.util.KVPair;
import lombok.Data;

import java.util.List;

/**
 * @author haibin.lhb
 * 
 *
 */
@Data
public class ConfigurationComponent {

    public static String TYPE_INPUT = "input";
    public static String TYPE_RADIO = "radio";
    public static String TYPE_SEARCH = "search";
    public static String TYPE_SELECT = "select";
    public static String TYPE_PASSWORD = "password";

    /**
     * radio,input,search,select
     */
    String type;

    /**
     * 是否必须
     */
    boolean required;

    /**
     * for all types.
     * default value is [KEY] if type is radio or select.
     */
    String defaultValue;

    /**
     * for radio or select type.
     * KEY is inner unique value.VALUE is display text for user.
     */
    List<KVPair<String, String>> options;

    /**
     * for search type only.
     */
    String requestUrl;



}

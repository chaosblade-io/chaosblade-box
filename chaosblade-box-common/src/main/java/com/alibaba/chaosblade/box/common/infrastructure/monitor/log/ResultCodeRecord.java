package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;

import lombok.Data;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ResultCodeRecord extends RecordObject {

    public static Integer SOURCE_MAPPING = 0;

    public static Integer SOURCE_API = 1;

    /**
     * 来自HSF
     */
    public static final Integer SOURCE_INTERNAL_HSF = 2;

    /**
     * 小程序调用
     */
    public static final Integer SOURCE_MINIAPP = 3;

    private int codeState = 0;

    private Boolean success;

    private String code;

    private String entrance;

    private String traceId;

    private Long cost;

    private String userId;

    private Integer source;

}

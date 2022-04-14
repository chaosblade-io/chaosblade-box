package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ChaosbladeErrorMessageDesc implements Serializable {

    @JSONField(name = "error_part")
    private String partError;

    @JSONField(name = "should_fail")
    private Boolean shouldFail;

    @JSONField(name = "cn_desc")
    private String cnDesc;

    @JSONField(name = "showOriginError")
    private boolean showOriginError;

    private Integer status;
}

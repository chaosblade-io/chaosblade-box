package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class CodeMetaData {

    private String type;

    private String solution;

    @JSONField(name = "en")
    private String enDesc;

    @JSONField(name = "cn")
    private String cnDesc;

}

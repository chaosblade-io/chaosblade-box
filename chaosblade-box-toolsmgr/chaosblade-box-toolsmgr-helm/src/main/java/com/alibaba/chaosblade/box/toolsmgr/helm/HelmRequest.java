package com.alibaba.chaosblade.box.toolsmgr.helm;

import com.alibaba.chaosblade.box.toolsmgr.api.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yefei
 */
@Data
public class HelmRequest extends Request {

    private String name;

    private String namespace;
}


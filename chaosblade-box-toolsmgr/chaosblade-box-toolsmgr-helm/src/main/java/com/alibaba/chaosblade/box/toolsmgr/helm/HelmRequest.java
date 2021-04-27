package com.alibaba.chaosblade.box.toolsmgr.helm;

import com.alibaba.chaosblade.box.toolsmgr.api.Request;
import lombok.Data;

/**
 * @author yefei
 */
@Data
public class HelmRequest extends Request {

    private String name;

    private String namespace;

    private String kubeconfig;
}


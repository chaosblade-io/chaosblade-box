package com.alibaba.chaosblade.box.toolsmgr.ansible;

import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * 命令扩展参数
 *
 */
public class AnsibleCommandExtraVarsUtil {

    /**
     * 特殊变量采用 extra-var方式 添加到ad-hoc 命令行中，不存储hosts文件
     *
     * @param ansibleContext
     * @return
     */
    public static String getExtraVars(MgrRequest mgrRequest) {
        StringBuilder sb = new StringBuilder();

        sb.append(" --extra-vars '");
        if (mgrRequest.getInstancePort() != null) {
            sb.append(" ansible_ssh_port=");
            sb.append(mgrRequest.getInstancePort());
        } else {
            sb.append(" ansible_ssh_port=" + AnsibleConstants.ANSIBLE_LINUX_DEFAULT_PORT);
        }

        if (mgrRequest.getNeedPassword()) {
            if (StringUtils.isBlank(mgrRequest.getInstancePassword())) {
                throw new IllegalArgumentException("use privilege escalation, must input password");
            }
            sb.append(" ansible_ssh_pass=");
            sb.append(mgrRequest.getInstancePassword());
        }

        sb.append("'");

        return sb.toString();
    }
}

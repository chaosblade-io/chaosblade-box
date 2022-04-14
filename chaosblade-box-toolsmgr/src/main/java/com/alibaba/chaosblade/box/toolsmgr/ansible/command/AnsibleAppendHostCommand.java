package com.alibaba.chaosblade.box.toolsmgr.ansible.command;

import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleConstants;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;

import java.util.List;

/**
 * hosts文件存储只存储固定变量，不存储可变参数
 *
 * @author: xinyuan.ymm
 * @create: 2022-01-07 9:41 PM
 */
public class AnsibleAppendHostCommand implements AnsibleCommand, AnsibleConstants {


    @Override
    public String getCommand(MgrRequest mgrRequest) {

        return String.format("echo '%s' >> /etc/ansible/hosts",
                mgrRequest.getInstanceIp());
    }

    @Override
    public boolean resultPredicate(List<String> result, boolean ignoreBackgroundTimeout) {
        return true;
    }
}

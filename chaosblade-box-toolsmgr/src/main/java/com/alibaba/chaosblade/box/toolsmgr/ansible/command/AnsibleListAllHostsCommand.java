package com.alibaba.chaosblade.box.toolsmgr.ansible.command;

import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleConstants;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;

import java.util.List;

/**
 * @author: xinyuan.ymm
 * @create: 2022-01-07 9:41 PM
 */
public class AnsibleListAllHostsCommand implements AnsibleCommand, AnsibleConstants {

    @Override
    public String getCommand(MgrRequest mgrRequest) {
        return "ansible all --list-hosts";
    }

    @Override
    public boolean resultPredicate(List<String> result, boolean ignoreBackgroundTimeout) {
        if(result.size() > 0 && result.stream().findFirst().get().contains("hosts")){
            return true;
        }
        return false;
    }
}

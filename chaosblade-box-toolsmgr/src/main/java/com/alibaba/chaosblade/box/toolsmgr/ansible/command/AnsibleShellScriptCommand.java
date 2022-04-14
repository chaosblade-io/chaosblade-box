package com.alibaba.chaosblade.box.toolsmgr.ansible.command;

import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommandExtraVarsUtil;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleConstants;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import com.google.common.base.Joiner;

import java.util.List;

/**
 * @author: xinyuan.ymm
 * @create: 2022-01-07 9:41 PM
 */
public class AnsibleShellScriptCommand implements AnsibleCommand, AnsibleConstants {


    @Override
    public String getCommand(MgrRequest mgrRequest) {

        String sb = String.format("ansible %s", mgrRequest.getInstanceIp());
        if (!mgrRequest.getInstanceUser().isEmpty()) {
            sb = String.format("%s -u %s", sb, mgrRequest.getInstanceUser());
        }
        sb = String.format("%s -m shell -a 'sh -c  \"%s\"'", sb, mgrRequest.getCommand());


        return String.format("%s %s", sb, AnsibleCommandExtraVarsUtil.getExtraVars(mgrRequest));
    }
    @Override
    public boolean resultPredicate(List<String> result, boolean ignoreBackgroundTimeout) {
        if (result != null && result.size() > 0) {
            String resultContent = Joiner.on(" ").join(result);
            if(ignoreBackgroundTimeout && resultContent.contains("timed out waiting for module completion")){
                return true;
            }

            if (resultContent.contains("FAILED")) {
                return false;
            }
            if (resultContent.contains("rc=0")
                || resultContent.contains("SUCCESS")
                || resultContent.contains("CHANGED")) {
                return true;
            }
        }
        return false;
    }
}

package com.alibaba.chaosblade.box.toolsmgr.ansible;

import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;

import java.util.List;

/**
 * @author: xinyuan
 * @create: 2022-01-07 9:41 PM
 */

public interface AnsibleCommand {


    String getCommand(MgrRequest mgrRequest);

    boolean resultPredicate(List<String> result, boolean ignoreBackgroundTimeout);

}

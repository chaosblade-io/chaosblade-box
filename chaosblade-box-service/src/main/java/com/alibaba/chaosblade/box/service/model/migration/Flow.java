package com.alibaba.chaosblade.box.service.model.migration;

import java.util.List;

/**
 * @author sunpeng
 * @date 2020/7/14
 * @email wb-sp762090@alibaba-inc.com
 */
public class Flow {

    /**
     * 准备阶段
     */
    private List<FlowActivity> prepare;

    /**
     * 注入阶段
     */
    private FlowActivity attack;

    /**
     * 验证阶段
     */
    private List<FlowActivity> check;

    /**
     * 恢复阶段
     */
    private List<FlowActivity> recover;

    public List<FlowActivity> getPrepare() {
        return prepare;
    }

    public void setPrepare(List<FlowActivity> prepare) {
        this.prepare = prepare;
    }

    public FlowActivity getAttack() {
        return attack;
    }

    public void setAttack(FlowActivity attack) {
        this.attack = attack;
    }

    public List<FlowActivity> getCheck() {
        return check;
    }

    public void setCheck(List<FlowActivity> check) {
        this.check = check;
    }

    public List<FlowActivity> getRecover() {
        return recover;
    }

    public void setRecover(List<FlowActivity> recover) {
        this.recover = recover;
    }
}

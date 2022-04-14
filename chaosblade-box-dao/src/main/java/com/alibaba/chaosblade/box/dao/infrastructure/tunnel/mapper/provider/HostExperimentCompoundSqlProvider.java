package com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider;

/**
 * @author haibin
 * 
 *
 */
public class HostExperimentCompoundSqlProvider {

    public String hostExperiment() {
        return "SELECT a.experiment_task_id,"
            + "       b.user_id"
            + "  FROM `t_chaos_app_execute_result` as a,"
            + "       t_chaos_experiment_task as b"
            + " WHERE a.experiment_task_id= b.task_id"
            + "   and b.run_state!= 4"
            + "   and a.host_ip= #{hostIp}"
            + " GROUP BY a.experiment_task_id";
    }

}

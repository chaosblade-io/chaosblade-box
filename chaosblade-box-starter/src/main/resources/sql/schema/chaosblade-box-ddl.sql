CREATE DATABASE IF NOT EXISTS chaosblade;
USE chaosblade;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS  `t_chaos_blade_exp_uid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `host` varchar(60) NOT NULL COMMENT '目标机器',
  `app_code` varchar(60) NOT NULL COMMENT '小程序code',
  `experiment_task_id` varchar(64) NOT NULL DEFAULT '' COMMENT '演练任务ID',
  `exp_uid` varchar(500) DEFAULT NULL COMMENT 'chaos blade的uid',
  `app_execution_id` varchar(200) DEFAULT NULL COMMENT '小程序执行id',
  `attributes` varchar(500) DEFAULT NULL COMMENT '附加属性',
  `activity_task_id` varchar(200) DEFAULT NULL COMMENT '活动ID',
  `expired` tinyint(3) NOT NULL DEFAULT '0',
  `expired_time` datetime DEFAULT NULL COMMENT '过期时间',
  `activity_id` varchar(500) DEFAULT NULL,
  `configuration_id` varchar(200) DEFAULT NULL,
  `sub_exp_uid` varchar(500) DEFAULT NULL COMMENT '内层expId',
  `request_id` varchar(200) DEFAULT NULL COMMENT 'request_id',
  PRIMARY KEY (`id`),
  KEY `chaos_blade_exp_uid_app_execution_id_index` (`app_execution_id`),
  KEY `chaos_blade_exp_uid_activity_task_id_host_index` (`activity_task_id`,`host`),
  KEY `uid_exp_uid_index` (`exp_uid`(20)),
  KEY `chaos_blade_exp_uid_experiment_task_id_expired_index` (`experiment_task_id`,`expired`),
  KEY `chaos_blade_exp_uid` (`expired`,`experiment_task_id`),
  KEY `idx_experimenttaskid` (`experiment_task_id`),
  KEY `idx_request_id` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chaos_blade执行记录查询';

CREATE TABLE IF NOT EXISTS `t_chaos_namespace` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `user_id` varchar(128) DEFAULT NULL,
  `name` varchar(100) DEFAULT '' COMMENT '名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `license_key` varchar(128) NOT NULL DEFAULT '' COMMENT 'ak',
  `secret_key` varchar(128) NOT NULL DEFAULT '' COMMENT 'sk',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_uid_namespace_delete` (`user_id`,`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='全局环境变量';

CREATE TABLE IF NOT EXISTS `t_chaos_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) NOT NULL DEFAULT '' COMMENT '用户id',
  `user_name` varchar(200) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_password` varchar(200) NOT NULL DEFAULT '' COMMENT '用户名密码',
  `user_status` tinyint(11) NOT NULL DEFAULT '0' COMMENT '用户状态0正常,1不正常',
  `license` varchar(128) NOT NULL DEFAULT '' COMMENT '用户license',
  `access_key` varchar(128) NOT NULL DEFAULT '' COMMENT 'ak',
  `secret_key` varchar(128) NOT NULL DEFAULT '' COMMENT 'sk',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `is_deleted` int(3) NOT NULL DEFAULT '0' COMMENT '是否被删除，1:是，0:否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_mini_flow` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `flow_id` varchar(200) NOT NULL COMMENT '微流程ID',
  `name` varchar(200) DEFAULT NULL COMMENT '名字',
  `group_id` varchar(200) NOT NULL COMMENT '微流程组',
  `experiment_id` varchar(200) NOT NULL COMMENT '演练ID',
  `required` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否必须的节点,默认不是',
  `flow_order` bigint(20) unsigned NOT NULL COMMENT '顺序',
  `group_order` bigint(20) unsigned NOT NULL COMMENT '顺序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_flowid` (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微流程表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_mini_flow_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `group_id` varchar(200) NOT NULL COMMENT 'groupId',
  `group_name` varchar(200) NOT NULL COMMENT '微流程组名',
  `experiment_id` varchar(200) NOT NULL COMMENT '演练ID',
  `hosts` longtext NOT NULL COMMENT '演练对象地址',
  `required` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否必须的节点,默认不是',
  `group_order` bigint(20) unsigned NOT NULL COMMENT '顺序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_groupid` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='experiment_mini_flow_group';

CREATE TABLE IF NOT EXISTS `t_chaos_expertise_evaluation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `expertise_id` varchar(500) NOT NULL COMMENT '经验ID',
  `evaluation_id` varchar(500) NOT NULL COMMENT '经验评估id',
  `description` text NOT NULL COMMENT '描述',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_evaluation_id` (`evaluation_id`(50))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专家经验';

CREATE TABLE IF NOT EXISTS `t_chaos_activity_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库自增',
  `task_id` varchar(64) NOT NULL DEFAULT '' COMMENT '运行ID',
  `activity_id` varchar(64) NOT NULL DEFAULT '' COMMENT '活动ID',
  `experiment_task_id` varchar(64) NOT NULL DEFAULT '' COMMENT '任务ID',
  `host_ip` varchar(64) NOT NULL DEFAULT '' COMMENT '运行的服务器',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `phase` varchar(250) DEFAULT NULL COMMENT '所处阶段',
  `gmt_end` datetime DEFAULT NULL COMMENT '结束事件',
  `error_message` text COMMENT '错误信息',
  `run_state` tinyint(3) unsigned DEFAULT NULL COMMENT '运行状态',
  `result_state` tinyint(3) unsigned DEFAULT NULL COMMENT '结果标志',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `gmt_start` datetime DEFAULT NULL COMMENT '开始时间',
  `namespace` varchar(64) NOT NULL DEFAULT 'default' COMMENT '环境标识',
  `phase_task_id` varchar(200) DEFAULT NULL COMMENT '阶段的任务ID',
  `user_type` varchar(200) DEFAULT NULL COMMENT '用户类型',
  `user_id` varchar(200) DEFAULT NULL COMMENT '用户id',
  `task_mode` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '任务运行模式',
  `pre_activity_task_id` varchar(500) DEFAULT NULL COMMENT '上一个任务ID',
  `next_activity_task_id` varchar(500) DEFAULT NULL COMMENT '下一个任务ID',
  `user_check_state` tinyint(3) unsigned DEFAULT NULL COMMENT '用户检查结果',
  `run_param` longtext COMMENT '运行参数',
  `activity_order` int(11) NOT NULL DEFAULT '0' COMMENT '执行顺序',
  `app_code` varchar(100) NOT NULL DEFAULT '' COMMENT 'app_code',
  `attributes` varchar(2000) DEFAULT NULL COMMENT 'task扩展字段',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `attack_activity_task_id` varchar(200) DEFAULT NULL COMMENT '攻击的activity_task_id',
  `activity_name` varchar(200) DEFAULT NULL,
  `executable_app_code` varchar(200) DEFAULT NULL COMMENT '真实可执行的小程序code',
  `flow_id` varchar(200) DEFAULT NULL COMMENT '微流程ID',
  `app_id` bigint(20) unsigned DEFAULT NULL COMMENT '应用ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_runnerid` (`task_id`),
  KEY `chaos_activity_task_experiment_task_id_index` (`experiment_task_id`),
  KEY `chaos_activity_task_experiment_task_id_phase_index` (`experiment_task_id`,`phase`),
  KEY `idx_experimenttaskid_phase_appcode` (`experiment_task_id`,`phase`,`app_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练任务表';

CREATE TABLE IF NOT EXISTS `t_chaos_app_execute_result` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增',
  `result_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'resultId',
  `data` text COMMENT '运行结果',
  `error_message` text COMMENT '错误信息',
  `host_ip` varchar(64) DEFAULT NULL COMMENT '小程序运行的目标机器',
  `activity_task_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'activity_task_id',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `gmt_create` datetime DEFAULT NULL COMMENT '新增时间',
  `app_code` varchar(256) DEFAULT NULL COMMENT '小程序的code',
  `nodegroup` varchar(500) DEFAULT NULL COMMENT '应用分组',
  `run_state` tinyint(3) unsigned DEFAULT NULL COMMENT '运行状态',
  `task_id` varchar(501) NOT NULL DEFAULT '' COMMENT '任务ID',
  `gmt_end` datetime DEFAULT NULL COMMENT '结束时间',
  `user_id` varchar(200) DEFAULT NULL COMMENT '用户ID',
  `user_type` tinyint(3) unsigned DEFAULT NULL COMMENT '用户类型',
  `result_state` tinyint(3) unsigned DEFAULT NULL COMMENT '结果',
  `chaos_blade_exp_uid` varchar(200) DEFAULT NULL COMMENT 'chaos_uid',
  `device_id` varchar(100) DEFAULT NULL COMMENT '设备ID',
  `device_configuration_id` varchar(100) DEFAULT NULL COMMENT '设备配置ID',
  `device_name` varchar(100) DEFAULT NULL COMMENT '设备名字',
  `experiment_task_id` varchar(100) NOT NULL DEFAULT '' COMMENT '演练任务ID',
  `start_time` datetime DEFAULT NULL COMMENT '增加start_time字段',
  `executable_app_code` varchar(200) DEFAULT NULL COMMENT 'ALTER TABLE `t_chaos_activity_task`\n如果是裂变小程序，executableAppCode为父code,否则就等于小程序的code',
  `error_code` varchar(200) DEFAULT NULL COMMENT '错误码',
  `phase` varchar(250) DEFAULT NULL COMMENT '所处阶段',
  `version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '乐观锁',
  `agent_code` int(11) NOT NULL DEFAULT '-1' COMMENT 'agent_code',
  `origin_request` longtext COMMENT '原始请求',
  `origin_response` longtext COMMENT '原始响应',
  `app_configuration_id` varchar(200) DEFAULT NULL COMMENT 'app设备的configurationId',
  `async` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是异步执行',
  PRIMARY KEY (`id`),
  KEY `idx_ac_code_host` (`activity_task_id`(20),`app_code`(20),`host_ip`(20)),
  KEY `unique_chaos_app_execute_result` (`task_id`(20)),
  KEY `agent_code` (`agent_code`),
  KEY `chaos_app_execute_result_experiment_task_id_host_ip_index` (`experiment_task_id`,`host_ip`),
  KEY `chaos_app_run_state_phase_async_index` (`run_state`,`phase`,`async`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小程序执行记录表';

CREATE TABLE IF NOT EXISTS `t_chaos_application_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `relation_id` varchar(64) NOT NULL COMMENT '关联关系ID',
  `app_id` bigint(20) unsigned NOT NULL COMMENT '演练实验ID',
  `outer_id` varchar(100) NOT NULL COMMENT '外部对象ID',
  `outer_description` varchar(300) DEFAULT NULL COMMENT '外部对象描述',
  `relation_type` varchar(100) NOT NULL COMMENT '关联对象类型',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `experiment_task_state` bigint(20) DEFAULT NULL COMMENT '执行状态',
  `experiment_task_result` bigint(20) DEFAULT NULL COMMENT '执行结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练关系表';

CREATE TABLE IF NOT EXISTS `t_chaos_changelog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `changelog_id` varchar(100) NOT NULL COMMENT '变更ID',
  `change_type` varchar(30) NOT NULL COMMENT '变更类型',
  `change_description` text NOT NULL COMMENT '变更描述',
  `target_id` varchar(100) DEFAULT NULL COMMENT '变更对象ID',
  `target_description` text COMMENT '变更对象描述',
  `target_type` varchar(30) NOT NULL COMMENT '变更对象类型',
  `operator_id` varchar(100) DEFAULT NULL COMMENT '变更操作人ID',
  `operator_description` text COMMENT '变更操作人描述',
  `operator_type` varchar(30) DEFAULT NULL COMMENT '变更操作人类型',
  `property_id` varchar(100) DEFAULT NULL COMMENT '更变属性ID',
  `property_description` text COMMENT '变更属性描述',
  `property_type` varchar(30) DEFAULT NULL COMMENT '变更属性类型',
  `property_change_type` varchar(30) DEFAULT NULL COMMENT '变更属性的变更类型',
  `type_description` text NOT NULL COMMENT 'type描述',
  `error_code` varchar(500) DEFAULT NULL COMMENT '错误码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变更记录表';

CREATE TABLE IF NOT EXISTS `t_chaos_distribute_lock` (
  `name` varchar(64) NOT NULL,
  `lock_until` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `locked_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `locked_by` varchar(255) NOT NULL,
  `gmt_create` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `gmt_modified` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分布式锁表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `experiment_id` varchar(64) NOT NULL COMMENT '实验id',
  `name` varchar(256) NOT NULL COMMENT '实验名称',
  `description` text COMMENT '实验描述',
  `state` tinyint(4) NOT NULL COMMENT '实验状态',
  `deleted` tinyint(3) unsigned DEFAULT '1' COMMENT '是否删除 0 未删除 1 删除',
  `task_id` varchar(64) DEFAULT NULL COMMENT '最近或者当前运行的任务ID',
  `rollback_definition` varchar(256) DEFAULT NULL COMMENT '回滚策略',
  `version` bigint(20) unsigned DEFAULT NULL COMMENT '版本号',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户ID',
  `namespace` varchar(64) NOT NULL DEFAULT 'default' COMMENT '环境标识',
  `user_type` varchar(32) DEFAULT NULL COMMENT '用户类型',
  `result_state` tinyint(3) unsigned DEFAULT NULL COMMENT '最近一次运行结果',
  `mini_app_desc` varchar(500) DEFAULT '' COMMENT '小程序描述信息',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0',
  `outer_id` varchar(200) DEFAULT NULL COMMENT '外部id',
  `level` varchar(100) DEFAULT NULL COMMENT '关联的故障等级定义',
  `owner_user_id` varchar(100) DEFAULT NULL COMMENT '演练的负责人ID',
  `scheduler_config` varchar(200) DEFAULT NULL,
  `attributes` longtext COMMENT '属性',
  `run_mode` varchar(50) DEFAULT NULL COMMENT '运行模式，阶段或者顺序执行',
  `duration` int(255) NOT NULL DEFAULT '0' COMMENT '演练持续时间',
  `is_template` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否模板,默认不是',
  `source` tinyint(4) DEFAULT '0' COMMENT '来源',
  PRIMARY KEY (`id`),
  UNIQUE KEY `chaos_experiment_experiment_id_is_delete_uindex` (`experiment_id`,`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实验表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_activity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(64) NOT NULL DEFAULT '' COMMENT '活动ID',
  `experiment_id` varchar(64) NOT NULL DEFAULT '' COMMENT '实验ID',
  `phase` varchar(25) NOT NULL DEFAULT '' COMMENT '活动所属阶段',
  `activity_order` int(11) NOT NULL DEFAULT '0' COMMENT '同一阶段内活动执行顺序',
  `activity_priority` tinyint(4) NOT NULL COMMENT '活动优先级',
  `pointcut_type` tinyint(4) DEFAULT NULL COMMENT '作为切片时候的类型,0:Before ,1:After',
  `parent_activity_id` varchar(64) DEFAULT NULL COMMENT '作为切片时候的目标活动',
  `activity_definition` longtext NOT NULL COMMENT '活动定义',
  `gmt_create` datetime DEFAULT NULL COMMENT '新增时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `activity_runner_id` varchar(64) DEFAULT NULL COMMENT '当前或者最近一次运行的runner',
  `app_code` varchar(64) DEFAULT NULL COMMENT 'app_code',
  `activity_task_id` varchar(500) DEFAULT NULL COMMENT '任务ID',
  `state` tinyint(4) DEFAULT NULL COMMENT '运行状态',
  `version` int(10) unsigned DEFAULT NULL COMMENT '版本号',
  `user_check_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否需要用户校验',
  `is_deleted` tinyint(3) unsigned DEFAULT '0' COMMENT '是否删除',
  `activity_name` varchar(500) NOT NULL DEFAULT '' COMMENT 'activity_name',
  `attack_activity_id` varchar(500) DEFAULT NULL COMMENT '如果是恢复环节对应的attack_activity_id',
  `executable_app_code` varchar(200) DEFAULT NULL COMMENT '如果是裂变小程序，executableAppCode为父code,否则就等于小程序的code',
  `flow_id` varchar(200) DEFAULT NULL COMMENT '微流程ID',
  PRIMARY KEY (`id`),
  KEY `idx_experiment_id` (`experiment_id`(10),`is_deleted`),
  KEY `idx_activityid` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动表';

CREATE TABLE `t_chaos_experiment_guard` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `guard_id` varchar(200) NOT NULL COMMENT 'id',
  `experiment_id` varchar(200) NOT NULL COMMENT '演练ID',
  `app_code` varchar(200) NOT NULL COMMENT '小程序code',
  `name` varchar(200) DEFAULT NULL COMMENT '名字',
  `action_type` int(10) unsigned NOT NULL COMMENT '守护节点类型',
  `argument` longtext NOT NULL COMMENT '演练参数',
  `required` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否必须的节点,默认不是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_guard_id` (`guard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练全局节点配置';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_guard_instance` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `instance_id` varchar(500) NOT NULL COMMENT 'instanceId',
  `name` varchar(200) DEFAULT NULL COMMENT 'name',
  `experiment_task_id` varchar(200) NOT NULL COMMENT 'experimentTaskId',
  `guard_id` varchar(200) NOT NULL COMMENT 'guardId',
  `state` varchar(200) NOT NULL COMMENT 'state',
  `app_code` varchar(200) NOT NULL COMMENT 'app_code',
  `argument` longtext NOT NULL COMMENT '参数',
  `action_type` tinyint(4) NOT NULL COMMENT 'type',
  `triggered_reason` varchar(1000) DEFAULT NULL COMMENT 'triggeredReason',
  `value` longtext COMMENT 'value',
  `activity_task_id` varchar(200) DEFAULT NULL COMMENT 'activityTaskId',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instance_id` (`instance_id`(100)),
  KEY `chaos_experiment_guard_instance_experiment_task_id_index` (`experiment_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='t_chaos_experiment_guard_instance';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_host_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `host_ip` varchar(200) NOT NULL DEFAULT '' COMMENT '机器私有地址',
  `device_id` varchar(200) DEFAULT '' COMMENT '设备id',
  `configuration_id` varchar(200) NOT NULL DEFAULT '' COMMENT 'configurationId',
  `outer_id` varchar(200) NOT NULL DEFAULT '' COMMENT '外部id',
  `app_name` varchar(200) DEFAULT NULL COMMENT '应用名',
  `relation_type` int(11) NOT NULL COMMENT '关系类型',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `experiment_task_state` bigint(20) DEFAULT NULL COMMENT '执行状态',
  `app_configuration_id` varchar(500) DEFAULT NULL COMMENT 'app configuration_id',
  `kub_namespace` varchar(500) DEFAULT NULL COMMENT '集群namespace',
  `namespace` varchar(500) DEFAULT NULL COMMENT 'namespace',
  `scope_type` int(11) DEFAULT NULL COMMENT '机器类型',
  `user_id` varchar(500) DEFAULT NULL COMMENT '用户ID',
  `cluster_id` varchar(500) DEFAULT NULL COMMENT '集群ID',
  `injection_target_name` varchar(500) DEFAULT NULL COMMENT '注入目标名称',
  `injection_target_type` varchar(500) DEFAULT NULL COMMENT '注入目标类型',
  `app_id` varchar(500) DEFAULT NULL COMMENT '应用Id',
  PRIMARY KEY (`id`),
  KEY `idx_app_configuration_id` (`app_configuration_id`(200)),
  KEY `idx_run_state` (`experiment_task_state`),
  KEY `idx_configuration_id` (`configuration_id`),
  KEY `idx_outer_id` (`outer_id`),
  KEY `idx_injection_target_type` (`injection_target_type`(200)),
  KEY `idx_relation_type` (`relation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练机器关系表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `relation_id` varchar(64) NOT NULL COMMENT '关联关系ID',
  `experiment_id` varchar(64) NOT NULL COMMENT '演练实验ID',
  `outer_id` varchar(100) NOT NULL COMMENT '外部对象ID',
  `outer_description` varchar(300) DEFAULT NULL COMMENT '外部对象描述',
  `relation_type` varchar(100) NOT NULL COMMENT '关联对象类型',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练关系表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_tag` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `tag_id` varchar(500) NOT NULL COMMENT '标签ID',
  `experiment_id` varchar(500) NOT NULL COMMENT '演练ID',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '关系类型，默认是0,代表演练',
  `tag_name` varchar(200) DEFAULT NULL COMMENT 'tagName',
  `user_id` varchar(200) DEFAULT NULL COMMENT 'userId',
  PRIMARY KEY (`id`),
  KEY `idx_experimentid` (`experiment_id`(200))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练和标签的关系';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增',
  `task_id` varchar(64) NOT NULL DEFAULT '' COMMENT '任务ID',
  `experiment_id` varchar(64) NOT NULL DEFAULT '' COMMENT '演练ID',
  `memo` varchar(256) DEFAULT NULL COMMENT '任务备注',
  `activity_task_id` varchar(64) DEFAULT NULL COMMENT '当前任务的活动记录id',
  `modifier` varchar(64) DEFAULT '' COMMENT '任务修改人',
  `gmt_create` datetime DEFAULT NULL COMMENT '任务创建',
  `gmt_modified` datetime DEFAULT NULL COMMENT '任务修改',
  `task_type` tinyint(3) unsigned DEFAULT NULL COMMENT '任务类型，1(自动),0(手动）',
  `result` text COMMENT '执行结果，只有任务类型是Auto才会有',
  `gmt_end` datetime DEFAULT NULL COMMENT '结束事件',
  `host_ip` varchar(64) DEFAULT NULL COMMENT '任务所在的机器地址',
  `activity_id` varchar(500) DEFAULT NULL COMMENT '当前运行的activity',
  `run_state` tinyint(3) unsigned DEFAULT NULL COMMENT '运行状态',
  `result_state` tinyint(3) unsigned DEFAULT NULL COMMENT '结果状态',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户ID',
  `namespace` varchar(64) NOT NULL DEFAULT 'default' COMMENT '环境标识',
  `gmt_start` datetime DEFAULT NULL COMMENT '开始时间',
  `user_type` varchar(100) DEFAULT NULL COMMENT '用户类型',
  `error_message` text COMMENT '错误信息',
  `outer_id` varchar(100) DEFAULT NULL COMMENT '外部ID',
  `experiment_task_context` text COMMENT '演练上下文',
  `duration` int(255) NOT NULL DEFAULT '0' COMMENT '演练持续时间',
  `name` varchar(200) DEFAULT NULL COMMENT '演练名字',
  `source` tinyint(4) DEFAULT '0',
  `feedback_status` tinyint(4) DEFAULT NULL COMMENT '反馈状态',
  `outer_user_id` varchar(200) DEFAULT NULL COMMENT '外部用户ID',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_experimentid` (`experiment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实验任务';

CREATE TABLE IF NOT EXISTS `t_chaos_expertise` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `expertise_id` varchar(500) NOT NULL COMMENT '经验ID',
  `name` varchar(500) NOT NULL COMMENT '名字',
  `function_desc` text NOT NULL COMMENT '功能描述',
  `background_desc` text NOT NULL COMMENT '背景描述',
  `design_concept` text NOT NULL COMMENT '设计理念/架构原则',
  `state` tinyint(4) NOT NULL COMMENT '专家经验状态,0代表可用,1代表不可用,2代表删除',
  `experiment_id` varchar(500) NOT NULL COMMENT '演练ID',
  `run_time` varchar(1000) DEFAULT NULL COMMENT '运行环境',
  `user_id` varchar(1000) DEFAULT NULL COMMENT '用户_id',
  `namespace` varchar(500) NOT NULL DEFAULT 'default' COMMENT 'namespace',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '经验库类型 0:系统经验库,1:管理员经验库',
  `scope_type` varchar(20) DEFAULT NULL COMMENT '支持的机器类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_expertise_id` (`expertise_id`(50))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专家经验';

CREATE TABLE IF NOT EXISTS `t_chaos_function_parameter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `function_id` varchar(100) NOT NULL COMMENT '关联的场景方法ID',
  `name` varchar(300) NOT NULL COMMENT '参数名',
  `alias` varchar(100) NOT NULL COMMENT '参数的属性名',
  `description` varchar(600) DEFAULT NULL COMMENT '参数描述',
  `parameter_id` varchar(100) NOT NULL COMMENT '参数ID',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `component` text COMMENT '参数前端组件定义，包括组件类型、选项、默认值、联动关系',
  `type` tinyint(4) DEFAULT NULL COMMENT '参数类型',
  `sequence` int(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `chaos_function_parameter_function_id_alias_is_delete_index` (`function_id`,`alias`,`is_delete`),
  KEY `idx_functionid_isdelete` (`function_id`,`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='场景方法的参数定义表';

CREATE TABLE IF NOT EXISTS `t_chaos_scene` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `scene_id` varchar(100) NOT NULL COMMENT '场景ID',
  `code` varchar(100) NOT NULL COMMENT '场景编码',
  `name` varchar(300) NOT NULL COMMENT '场景名称',
  `description` varchar(600) DEFAULT NULL COMMENT '场景描述',
  `version` varchar(10) NOT NULL DEFAULT '1.0.0' COMMENT '版本',
  `state` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '状态;0-草稿;1-正常;',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '用户ID',
  `is_public` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否对所有用户公开',
  `sub_user_id` varchar(100) DEFAULT NULL COMMENT '子账号ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='场景表';

CREATE TABLE IF NOT EXISTS `t_chaos_scene_authorized` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `authorized_id` varchar(100) NOT NULL COMMENT '授权ID',
  `function_id` varchar(100) NOT NULL COMMENT '授权的小程序ID',
  `function_name` varchar(300) NOT NULL COMMENT '授权的小程序名称',
  `grant_from` varchar(100) NOT NULL COMMENT '授权的用户ID',
  `grant_to` varchar(100) NOT NULL COMMENT '被授权的用户ID',
  `permission` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '权限类型',
  `enabled` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '小程序状态',
  `is_public` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否为公共小程序',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `phase` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '小程序所属阶段',
  `function_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '小程序创建时间',
  `source` tinyint(4) NOT NULL DEFAULT '0' COMMENT '来源',
  `support_host` tinyint(4) DEFAULT NULL COMMENT '是否支持主机类型',
  `support_k8s` tinyint(4) DEFAULT NULL COMMENT '是否支持K8S类型',
  `function_code` varchar(300) DEFAULT NULL COMMENT '小程序CODE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权表';

CREATE TABLE IF NOT EXISTS `t_chaos_scene_function` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `function_id` varchar(100) NOT NULL COMMENT '方法ID',
  `scene_id` varchar(100) DEFAULT '' COMMENT '对应的场景ID',
  `code` varchar(100) NOT NULL COMMENT '场景方法的编码',
  `name` varchar(300) NOT NULL COMMENT '场景方法的名称',
  `description` varchar(600) DEFAULT NULL COMMENT '场景方法的描述',
  `agent_required` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否依赖Agent执行',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `enabled` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否生效',
  `pre_dep_app_code` varchar(150) DEFAULT '' COMMENT '前置依赖的appCode',
  `next_dep_app_code` varchar(150) DEFAULT NULL COMMENT '后置依赖的appCode',
  `phase_flag` tinyint(4) DEFAULT NULL COMMENT '支持哪个阶段的标志位',
  `chaos_blade_action_type` tinyint(4) DEFAULT NULL COMMENT 'chaos_blade_action_type',
  `source` tinyint(3) unsigned DEFAULT NULL COMMENT '来源,是chaos-blade还是用户自己写',
  `categories` text COMMENT '小程序归属的类目',
  `support_scope_types` text COMMENT '小程序支持的演练对象类型',
  `version` varchar(100) NOT NULL DEFAULT '1.0.0' COMMENT '版本号',
  `system_versions` varchar(300) DEFAULT NULL COMMENT '支持的系统列表',
  `support_os_types` text COMMENT '小程序支持的机器操作系统类型',
  `parent_code` varchar(100) DEFAULT NULL COMMENT 'parent code',
  `risk_level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '风险等级，数字越大风险越高',
  `dependencies` varchar(600) DEFAULT NULL COMMENT '依赖的小程序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='场景函数表';

CREATE TABLE IF NOT EXISTS `t_chaos_scene_function_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `category_id` varchar(100) NOT NULL COMMENT '类目ID',
  `name` varchar(300) NOT NULL COMMENT '类目名称',
  `phase` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '类目所属阶段',
  `parent_id` varchar(100) DEFAULT NULL COMMENT '上级类目ID，为空表示根类目',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '类目的类型',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除，0-未删除，1-删除',
  `support_host` tinyint(4) DEFAULT NULL COMMENT '是否支持主机类型',
  `support_k8s` tinyint(4) DEFAULT NULL COMMENT '是否支持K8S类型',
  `support_cloud` tinyint(4) DEFAULT '0' COMMENT '是否支持云资源',
  `support_os_types` text COMMENT '目录支持的机器操作系统类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小程序类目表';

CREATE TABLE IF NOT EXISTS `t_chaos_scene_function_category_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  `category_id` varchar(200) NOT NULL,
  `code` varchar(200) NOT NULL,
  `function_id` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `chaos_scene_function_category_relation_category_id_index` (`category_id`),
  KEY `chaos_scene_function_category_relation_code_index` (`code`),
  KEY `chaos_scene_function_category_relation_function_id_index` (`function_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_scene_function_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `relation_id` varchar(100) NOT NULL COMMENT '关系ID',
  `out_function_id` varchar(100) NOT NULL COMMENT '父级小程序ID',
  `function_id` varchar(100) NOT NULL COMMENT '小程序ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小程序映射表';

CREATE TABLE IF NOT EXISTS `t_chaos_scheduler_job` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '任务名字',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
  `enabled` tinyint(4) NOT NULL COMMENT '是否启用',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户ID',
  `outer_id` varchar(100) DEFAULT NULL COMMENT '外部系统ID',
  `business_type` int(11) DEFAULT NULL COMMENT '业务类型',
  `business_id` varchar(100) DEFAULT NULL COMMENT '业务ID',
  `extra_infos` varchar(200) DEFAULT NULL COMMENT '额外信息',
  `scheduler_bean_class` varchar(200) NOT NULL DEFAULT '' COMMENT '定时任务bean class',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `job_id` varchar(200) NOT NULL DEFAULT '',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_tag` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `name` varchar(60) NOT NULL COMMENT '标签名',
  `code` varchar(60) NOT NULL COMMENT '标签编码',
  `type` tinyint(3) unsigned DEFAULT '0' COMMENT '标签类型;0-业务',
  `tag_id` varchar(60) NOT NULL COMMENT '标签字符串ID',
  `creator_name` varchar(60) DEFAULT NULL COMMENT '标签创建人名称',
  `creator_emp_id` varchar(60) DEFAULT NULL COMMENT '标签创建人工号',
  `user_id` varchar(60) DEFAULT NULL COMMENT '创建标签的用户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';

CREATE TABLE IF NOT EXISTS `t_chaos_application` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `user_id` varchar(100) NOT NULL COMMENT '用户账号',
  `namespace` varchar(32) NOT NULL DEFAULT 'default' COMMENT '命名空间',
  `app_name` varchar(128) NOT NULL COMMENT '应用名',
  `app_type` tinyint(4) DEFAULT NULL COMMENT '应用类型',
  `disabled` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态',
  `dimension` tinyint(4) DEFAULT NULL COMMENT '划分维度',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_cid` (`user_id`,`app_name`),
  KEY `disabled` (`disabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chaos应用表';

CREATE TABLE IF NOT EXISTS `t_chaos_application_configuration` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '新建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `alias` varchar(200) NOT NULL COMMENT '配置key',
  `value` varchar(500) DEFAULT NULL,
  `scope` text COMMENT '生效范围',
  `app_id` bigint(24) NOT NULL COMMENT '应用id',
  `override` tinyint(2) DEFAULT '0' COMMENT '是否覆盖用户输入',
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_chaos_application_configuration_alias_app_id_index` (`alias`,`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用配置表';

CREATE TABLE IF NOT EXISTS `t_chaos_application_device` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `user_id` varchar(100) NOT NULL COMMENT '用户账号',
  `namespace` varchar(32) NOT NULL DEFAULT 'default' COMMENT '命名空间',
  `app_name` varchar(128) NOT NULL COMMENT '应用名',
  `pid` bigint(20) unsigned DEFAULT NULL COMMENT '进程Id',
  `app_id` bigint(20) unsigned NOT NULL COMMENT '应用ID',
  `group_name` varchar(256) NOT NULL COMMENT '分组名',
  `cluster_id` varchar(64) DEFAULT NULL COMMENT '集群',
  `device_type` tinyint(4) DEFAULT '0' COMMENT '设备类型，Host 是 0，Container 是 1',
  `configuration_id` varchar(128) NOT NULL COMMENT '设备全局唯一标识',
  `host_configuration_id` varchar(128) DEFAULT NULL COMMENT '主机配置ID',
  `is_deleted` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '设备状态, 0 是可用，1 是不可用',
  `last_health_ping_time` bigint(20) NOT NULL COMMENT '最后一次健康检查时间',
  `connect_time` bigint(11) NOT NULL COMMENT '连接时间',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '状态',
  `device_name` varchar(128) DEFAULT NULL COMMENT 'host_name',
  `private_ip` varchar(64) DEFAULT NULL,
  `public_ip` varchar(64) DEFAULT NULL,
  `dimension` tinyint(4) DEFAULT NULL COMMENT '资源划分维度',
  `kub_namespace` varchar(500) DEFAULT NULL COMMENT 'k8s namespace',
  `os_type` tinyint(4) DEFAULT '0' COMMENT '设备OS类型： linux 是 0, windows 是 1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid_cid_aid` (`user_id`,`configuration_id`,`app_id`),
  UNIQUE KEY `uk_uid_cid` (`user_id`,`configuration_id`),
  KEY `t_chaos_application_device_configuration_id_index` (`configuration_id`),
  KEY `status_app_id` (`app_id`,`status`),
  KEY `status_health` (`status`,`last_health_ping_time`),
  KEY `t_chaos_application_device_configuration_id_index_2` (`configuration_id`),
  KEY `t_chaos_application_device_last_health_ping_time_index` (`last_health_ping_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chaos应用设备表';

CREATE TABLE IF NOT EXISTS `t_chaos_application_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_id` bigint(20) NOT NULL COMMENT 'appId',
  `app_name` varchar(128) NOT NULL COMMENT '应用名',
  `name` varchar(128) NOT NULL COMMENT '应用名',
  `display` varchar(256) DEFAULT NULL COMMENT '分组名对应的展示名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_cid` (`app_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chaos应用表';

CREATE TABLE IF NOT EXISTS `t_chaos_experiment_task_feedback` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `feedback_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'feedback_id',
  `experiment_task_id` varchar(300) NOT NULL COMMENT '演练任务ID',
  `user_id` varchar(200) NOT NULL COMMENT '用户ID',
  `sub_user_id` varchar(200) DEFAULT NULL COMMENT '子账号ID',
  `experiment_id` varchar(200) NOT NULL COMMENT '演练ID',
  `memo` longtext COMMENT '备注',
  `expectation_status` tinyint(4) DEFAULT NULL COMMENT '符合预期',
  `business_status` tinyint(4) DEFAULT NULL COMMENT '业务影响',
  `feedback_time` datetime DEFAULT NULL COMMENT '反馈时间',
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  `extra_component` longtext COMMENT '额外信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `feedback_id` (`feedback_id`),
  UNIQUE KEY `experiment_task_id` (`experiment_task_id`(15))
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演练任务反馈表';

CREATE TABLE IF NOT EXISTS `t_chaos_device` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `user_id` varchar(100) NOT NULL COMMENT '用户账号',
  `namespace` varchar(32) NOT NULL DEFAULT 'default' COMMENT '命名空间',
  `vpc_id` varchar(64) NOT NULL DEFAULT '' COMMENT 'vpc id',
  `provider` varchar(64) DEFAULT NULL COMMENT '设备供应商，aliyun',
  `cluster_id` varchar(64) DEFAULT NULL COMMENT '集群',
  `cluster_name` varchar(128) DEFAULT NULL COMMENT '集群名称',
  `version` varchar(32) DEFAULT NULL,
  `chaos_version` varchar(32) DEFAULT NULL COMMENT 'chaosblade版本',
  `public_ip` varchar(64) DEFAULT NULL COMMENT '对外 ip',
  `private_ip` varchar(64) DEFAULT NULL,
  `parent_ip` varchar(64) DEFAULT NULL COMMENT '宿主机 ip',
  `device_id` varchar(128) DEFAULT NULL COMMENT '实例 id, host 是 instanceId, container 是 containerId',
  `device_type` tinyint(4) DEFAULT '0' COMMENT '设备类型，Host 是 0，Container 是 1',
  `device_name` varchar(512) DEFAULT NULL,
  `device_role` varchar(128) DEFAULT NULL COMMENT '设备角色',
  `serial_number` varchar(64) DEFAULT NULL COMMENT '设备序列号',
  `os_version` varchar(128) DEFAULT NULL COMMENT '设备系统',
  `hostname` varchar(128) DEFAULT NULL,
  `spec` varchar(64) DEFAULT NULL COMMENT '实例规格类型',
  `cpu` int(11) DEFAULT NULL COMMENT 'CPU核心数',
  `mem` int(11) DEFAULT NULL,
  `configuration_id` varchar(128) NOT NULL COMMENT '设备全局唯一标识',
  `host_configuration_id` varchar(128) DEFAULT NULL COMMENT '主机配置ID',
  `host_instance_id` varchar(256) DEFAULT NULL COMMENT '主机实例ID',
  `parent_configuration_id` varchar(128) DEFAULT NULL COMMENT '父设备ID',
  `parent_device_type` tinyint(4) DEFAULT NULL COMMENT '父设备类型',
  `parent_device_name` varchar(512) DEFAULT NULL COMMENT '父设备名称',
  `parent_device_space` varchar(128) DEFAULT NULL COMMENT '父设备命名空间',
  `status` tinyint(4) DEFAULT '0' COMMENT '设备状态, 0 是不可用，1 是可用',
  `enable` tinyint(4) DEFAULT NULL COMMENT '是否开启',
  `connect_time` bigint(20) DEFAULT NULL COMMENT '建连时间',
  `install_mode` varchar(64) DEFAULT NULL COMMENT '安装模式： HOST、KUBERNETES、CS_KUBERNETES、CS_SWARM',
  `md5` varchar(128) DEFAULT NULL COMMENT 'md5',
  `command_id` varchar(64) DEFAULT NULL COMMENT '云助手命令id',
  `command_time` bigint(20) DEFAULT NULL COMMENT '命令请求时间',
  `request_id` varchar(64) DEFAULT NULL COMMENT '安装请求ID',
  `device_create_time` varchar(128) DEFAULT NULL,
  `uptime` varchar(128) DEFAULT NULL COMMENT '启动时间',
  `request_result` varchar(128) DEFAULT NULL COMMENT '请求结果',
  `reason` longtext COMMENT '故障原因',
  `reason_code` varchar(64) DEFAULT NULL COMMENT '故障原因编码',
  `ext_info` longtext COMMENT '扩展信息',
  `last_health_ping_time` bigint(11) NOT NULL COMMENT '最后一次健康检查时间',
  `last_online_time` bigint(11) DEFAULT NULL COMMENT '最后一次健康时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0',
  `experiment_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否演练过，默认没有',
  `last_Experiment_Time` datetime DEFAULT NULL COMMENT '最近一次演练的时间',
  `vswitch_id` varchar(128) DEFAULT NULL COMMENT '交换机ID列表',
  `os_type` tinyint(4) DEFAULT '0' COMMENT '设备OS类型： linux 是 0, windows 是 1',
  `port` int(128) NOT NULL DEFAULT '-1' COMMENT '设备端口号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid_cid` (`user_id`,`configuration_id`),
  KEY `IDX_UID_DTYPE` (`user_id`,`device_type`) USING BTREE,
  KEY `idx_cid` (`configuration_id`),
  KEY `idx_uid_did` (`user_id`,`device_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备数据表，Host、Container 基础数据';

CREATE TABLE IF NOT EXISTS `t_chaos_tools` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `gmt_modified` datetime NOT NULL COMMENT 'modified time',
  `gmt_create` datetime NOT NULL COMMENT 'create time',
  `configuration_id` varchar(128) DEFAULT NULL COMMENT 'configuration_id',
  `cluster_id` varchar(64) DEFAULT NULL COMMENT 'cluster_id',
  `name` varchar(128) NOT NULL COMMENT 'tools name',
  `version` varchar(128) NOT NULL COMMENT 'version',
  `url` varchar(1024) DEFAULT NULL COMMENT 'download url',
  `device_type` tinyint(3) unsigned DEFAULT NULL COMMENT 'device type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='chaos tools';

CREATE TABLE `t_chaos_workspace_relation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `relation_id` varchar(100) NOT NULL COMMENT '关联关系ID',
  `workspace_id` varchar(100) DEFAULT NULL COMMENT '工作空间ID',
  `outer_id` varchar(100) NOT NULL DEFAULT '' COMMENT '被关联对象ID，如：关联实验，则为实验ID',
  `outer_description` text COMMENT '被关联对象描述，如：关联实验，则为实验名；',
  `relation_type` varchar(100) NOT NULL DEFAULT 'experiment' COMMENT '关联对象类型。experiment-实验',
  `is_delete` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  `permission` tinyint(3) unsigned DEFAULT '0' COMMENT '权限标志位',
  `namespace` varchar(255) DEFAULT NULL COMMENT '命名空间',
  `experiment_creator` varchar(200) DEFAULT NULL COMMENT '演练创建人员',
  PRIMARY KEY (`id`),
  KEY `chaos_workspace_relation_experiment_creator_index` (`experiment_creator`),
  KEY `chaos_workspace_relation_is_delete_index` (`is_delete`),
  KEY `idx_outerid_workspaceid_isdelete` (`outer_id`,`is_delete`),
  KEY `idx_workspaceid_relationtype_isdelete` (`is_delete`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作空间关联关系表';

CREATE TABLE IF NOT EXISTS `t_chaos_application_device_tag` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `tag_id` varchar(500) NOT NULL COMMENT '标签ID',
  `configuration_id` varchar(100) DEFAULT NULL COMMENT '机器唯一标识',
  `tag_name` varchar(200) DEFAULT NULL COMMENT 'tagName',
  `app_id` varchar(200) DEFAULT NULL COMMENT '应用ID',
  `group_name` varchar(200) DEFAULT NULL COMMENT '应用分组名称',
  `user_id` varchar(200) DEFAULT NULL COMMENT 'userId',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用下机器与标签关系';

CREATE TABLE IF NOT EXISTS `t_chaos_scheduler_trigger_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `job_group` varchar(200) DEFAULT NULL,
  `job_id` varchar(200) DEFAULT NULL,
  `executor_address` varchar(200) DEFAULT NULL,
  `executor_handler` varchar(200) DEFAULT NULL,
  `executor_param` longtext,
  `end_time` datetime DEFAULT NULL,
  `trigger_message` varchar(200) DEFAULT NULL,
  `business_id` varchar(200) DEFAULT NULL,
  `fire_time` datetime DEFAULT NULL,
  `fire_instance_id` varchar(200) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_chaos_scheduler_trigger_log_fire_instance_id_index` (`fire_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_migration_configuration` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) NOT NULL DEFAULT '' COMMENT 'chaos_user表中的user_id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `cloud_ak` varchar(32) NOT NULL DEFAULT '' COMMENT '阿里云账号',
  `cloud_sk` varchar(32) NOT NULL DEFAULT '' COMMENT '阿里云密码',
  `metric_migration` int(11) NOT NULL COMMENT '数据迁移配置转换为二进制：01演练数据，10演练经验库',
  `db_url` varchar(32) NOT NULL DEFAULT '' COMMENT '数据库url格式，host:port',
  `db_account` varchar(32) NOT NULL DEFAULT '' COMMENT '数据库账号',
  `db_password` varchar(32) NOT NULL DEFAULT '' COMMENT '数据库密码',
  `migration_result` varchar(128) NOT NULL DEFAULT '' COMMENT '迁移结果',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userdb` (`user_id`,`db_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) DEFAULT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `t_chaos_m_quartz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `t_chaos_m_quartz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `t_chaos_m_quartz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `t_chaos_m_quartz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_simple_triggers`
(
    SCHED_NAME      varchar(120) not null,
    TRIGGER_NAME    varchar(200) not null,
    TRIGGER_GROUP   varchar(200) not null,
    REPEAT_COUNT    bigint(7)    not null,
    REPEAT_INTERVAL bigint(12)   not null,
    TIMES_TRIGGERED bigint(10)   not null,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint t_chaos_m_quartz_simple_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references `t_chaos_m_quartz_triggers` (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `t_chaos_m_quartz_paused_trigger_grps`
(
    SCHED_NAME    varchar(120) not null,
    TRIGGER_GROUP varchar(200) not null,
    primary key (SCHED_NAME, TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS t_chaos_m_quartz_blob_triggers
(
    SCHED_NAME    varchar(120) not null,
    TRIGGER_NAME  varchar(200) not null,
    TRIGGER_GROUP varchar(200) not null,
    BLOB_DATA     blob         null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint t_chaos_m_quartz_blob_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references t_chaos_m_quartz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS t_chaos_m_quartz_calendars
(
    SCHED_NAME    varchar(120) not null,
    CALENDAR_NAME varchar(200) not null,
    CALENDAR      blob         not null,
    primary key (SCHED_NAME, CALENDAR_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS t_chaos_m_quartz_job_info
(
    id               bigint auto_increment
        primary key,
    source_key       varchar(128) default '' null comment '任务来源ID',
    job_group        varchar(255) default '' not null comment '执行器主键ID，可以存储租户ID',
    job_cron         varchar(128)            not null comment '任务执行CRON',
    job_desc         varchar(255) default '' null comment '任务描述',
    job_type         int                     null comment '任务类型',
    concurrent       tinyint      default 1  not null comment '是否支持并发',
    author           varchar(64)  default '' not null comment '作者',
    route_strategy   varchar(64)             null,
    executor_handler varchar(255) default '' not null comment '执行器任务handler',
    executor_param   text                    null comment '执行器任务参数',
    job_status       int                     null,
    gmt_create       datetime                not null,
    gmt_modified     datetime                not null,
    constraint uk
        unique (job_group, job_cron, executor_handler)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS t_chaos_m_quartz_simprop_triggers
(
    SCHED_NAME    varchar(120)   not null,
    TRIGGER_NAME  varchar(200)   not null,
    TRIGGER_GROUP varchar(200)   not null,
    STR_PROP_1    varchar(512)   null,
    STR_PROP_2    varchar(512)   null,
    STR_PROP_3    varchar(512)   null,
    INT_PROP_1    int            null,
    INT_PROP_2    int            null,
    LONG_PROP_1   bigint         null,
    LONG_PROP_2   bigint         null,
    DEC_PROP_1    decimal(13, 4) null,
    DEC_PROP_2    decimal(13, 4) null,
    BOOL_PROP_1   varchar(1)     null,
    BOOL_PROP_2   varchar(1)     null,
    primary key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    constraint t_chaos_m_quartz_simprop_triggers_ibfk_1
        foreign key (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) references t_chaos_m_quartz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS t_chaos_m_quartz_trigger_log
(
    id               bigint auto_increment
        primary key,
    job_group        varchar(255) default ''  not null comment '执行器主键ID',
    job_id           bigint                   not null comment '任务，主键ID',
    executor_address varchar(255)             null comment '执行器地址，本次执行的地址',
    executor_handler varchar(255)             null comment '执行器任务handler',
    executor_param   varchar(512)             null comment '执行器任务参数',
    trigger_time     datetime                 null comment '调度-时间',
    trigger_code     varchar(255) default '0' null comment '调度-结果',
    trigger_msg      varchar(2048)            null comment '调度-日志',
    handle_time      datetime                 null comment '执行-时间',
    handle_code      varchar(255) default '0' null comment '执行-状态',
    handle_msg       varchar(2048)            null comment '执行-日志',
    gmt_create       datetime                 not null,
    gmt_modified     datetime                 not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index idx_gmt_create
    on t_chaos_m_quartz_trigger_log (gmt_create);









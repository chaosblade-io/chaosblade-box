/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

CREATE DATABASE IF NOT EXISTS chaosblade;
USE chaosblade;
SET NAMES utf8mb4;

create table if not exists t_chaos_application
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create   datetime                      not null comment 'create time',
    gmt_modified datetime                      not null comment 'modified time',
    namespace    varchar(32) default 'default' not null comment 'namespace',
    app_name     varchar(128)                  not null comment 'application name',
    app_type     tinyint                       null comment 'application type 0-host, 1-k8s',
    constraint app unique (namespace, app_name),
    key `INX_APPLICATION_NA_APP_NAME` (namespace, app_name),
    key `INX_APPLICATION_APP_NAME` (app_name)
) ENGINE = InnoDB
    COMMENT 'application'
  DEFAULT CHARSET = utf8;

create table if not exists t_chaos_application_device
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create   datetime                      not null comment 'create time',
    gmt_modified datetime                      not null comment 'modified time',
    namespace    varchar(32) default 'default' not null comment 'namespace',
    app_id       bigint unsigned               not null comment 'application id',
    group_id     bigint unsigned               not null comment 'application group id',
    app_name     varchar(128)                  not null comment 'application name',
    group_name   varchar(256)                  not null comment 'application group name',
    device_id    varchar(64)                   null,
    key `INX_APPLICATION_DEVICE_APP_ID_DEVICE_ID` (app_id, device_id)
)
    ENGINE = InnoDB
    COMMENT 'application device relation'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_application_group
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create   datetime     not null comment 'create time',
    gmt_modified datetime     not null comment 'modified time',
    app_id       bigint       not null comment 'appId',
    app_name     varchar(128) not null comment 'application name',
    group_name   varchar(128) not null comment 'application group name',
    constraint uk_uid_cid
        unique (app_id, group_name),
    key `INX_APP_GROUP_APP_ID` (app_id)
)
    ENGINE = InnoDB
    comment 'application_group'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_device
(
    id                   bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create           datetime               not null comment 'create time',
    gmt_modified         datetime               not null comment 'modified time',
    ip                   varchar(128) default null comment 'ip',
    hostname             varchar(256)           null comment 'hostname',
    version              varchar(256)           null comment 'os version',
    cpu_core             int                    null comment 'CPU core size',
    memory_size          int                    null comment 'memory size',
    status               tinyint      default 0 null comment '0-offline / 1-online',
    connect_time         datetime               null comment 'connect_time',
    install_mode         varchar(64)            null comment 'install_mode： host、k8s_helm',
    uptime               varchar(128)           null comment 'uptime',
    type                 tinyint                not null comment 'type 0-host,1-node, 2-pod',
    last_ping_time       datetime               null comment 'last ping time',
    last_online_time     datetime               null comment 'last ping result time',
    is_experimented      tinyint      default 0 not null comment 'is experimented',
    last_experiment_time datetime               null comment 'last experiment time',
    last_task_id         bigint                 null comment 'last task id',
    last_task_status     tinyint                null comment 'last task status',
    key `INX_DEVICE_IP` (ip)
)
    ENGINE = InnoDB
    comment 'device'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_device_node
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_modified datetime     not null comment 'modified time',
    gmt_create   datetime     not null comment 'create time',
    device_id    bigint       not null,
    cluster_id   varchar(256) null comment 'cluster id',
    cluster_name varchar(256) null comment 'cluster name',
    node_name    varchar(256) not null comment 'node name',
    node_ip      varchar(128) null comment 'node ip',
    node_version varchar(128) null comment 'node version',
    key `INX_DEVICE_NODE_DEVICE_ID` (device_id)
)
    ENGINE = InnoDB
    comment 'k8s-node'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_device_pod
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create   datetime        not null comment 'create time',
    gmt_modified datetime        not null comment 'modified time',
    node_id      bigint          not null comment 'node ip',
    device_id    bigint unsigned null,
    namespace    varchar(256)    null comment 'namespace',
    pod_name     varchar(128)    not null comment 'pod name',
    pod_ip       varchar(128)    null comment 'pod ip',
    containers   longtext        null comment 'containers, json',
    key `INX_DEVICE_POD_DEVICE_ID` (device_id)
)
    ENGINE = InnoDB
    comment 'k8s-pod'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_experiment
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create   datetime                    not null comment 'create time',
    gmt_modified datetime                    not null comment 'modified time',
    name         varchar(256)                not null comment 'experiment name',
    description  text                        null comment 'experiment description',
    version      bigint unsigned             null comment 'experiment version',
    task_id      bigint unsigned             null comment 'current or last task id ',
    metric       longtext                    null comment 'metric config',
    run_model    varchar(16) default 'PHASE' not null comment 'run model，PHASE/SEQUENCE',
    duration     int unsigned                null comment 'duration',
    dimension    varchar(64)                 null comment 'dimension, host, k8s'
)
    ENGINE = InnoDB
    comment 'experiment'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_experiment_activity
(
    id                  bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create          datetime                null comment 'create time',
    gmt_modified        datetime                null comment 'modified time',
    activity_name       varchar(500) default '' not null comment 'activity name',
    experiment_id       bigint                  not null comment 'experiment id',
    flow_id             bigint                  null comment 'flow id',
    phase               varchar(25)  default '' not null comment 'phase',
    activity_order      int          default 0  not null comment 'order of execution of activities within the same phase',
    activity_priority   tinyint                 null comment '活动优先级',
    activity_definition longtext                not null comment 'activity definition, contains parameter machines and so on',
    scene_code          varchar(64)             null comment 'scene code',
    key `INX_EXPERIMENT_ACTIVITY_EXPERIMENT_ID` (experiment_id)
)
    ENGINE = InnoDB
    comment 'experiment activity'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_experiment_activity_task
(
    id                    bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create            datetime                null comment 'create time',
    gmt_modified          datetime                null comment 'modified time',
    activity_id           bigint unsigned         null comment 'activity id',
    activity_name         varchar(200)            null,
    experiment_task_id    bigint unsigned         null comment 'experiment task id',
    flow_id               bigint                  null comment 'flow id',
    phase                 varchar(250)            null comment 'phase',
    gmt_start             datetime                null comment 'start time',
    gmt_end               datetime                null comment 'end time',
    run_status            tinyint unsigned        null comment 'run status',
    result_status         tinyint unsigned        null comment 'result status',
    error_message         text                    null comment 'error message',
    pre_activity_task_id  varchar(500)            null comment 'pre activity task id',
    next_activity_task_id varchar(500)            null comment 'next activity task id',
    run_param             longtext                null comment 'run param',
    activity_order        int          default 0  not null comment 'order of execution of activities within the same phase',
    scene_code            varchar(100) default '' not null comment 'scene_code',
    app_id                bigint unsigned         null comment 'application id',
    key `INX_EXPERIMENT_ACTIVITY_ID` (activity_id),
    key `INX_EXPERIMENT_ACTIVITY_TASK_ID` (experiment_task_id)
)
    ENGINE = InnoDB
    comment 'experiment activity task'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_experiment_activity_task_record
(
    id                 bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create         datetime         null comment 'create time',
    gmt_modified       datetime         null comment 'modified time',
    experiment_task_id bigint unsigned  null comment 'experiment task d',
    flow_id            bigint           null comment 'flow id',
    activity_task_id   bigint           not null comment 'activity_task_id',
    success            tinyint unsigned null comment '0-false, 1-true',
    code               varchar(200)     null comment 'code',
    error_message      longtext         null comment 'error message',
    result             longtext         null comment 'result',
    device_id          bigint unsigned  null comment 'device id',
    hostname           longtext         null comment 'hostname',
    ip                 varchar(64)      null comment 'ip',
    scene_code         varchar(256)     null comment 'scene code',
    gmt_start          datetime         null comment 'start time',
    gmt_end            datetime         null comment 'end time ',
    phase              varchar(250)     null comment 'phase',
    key `INX_EXPERIMENT_R_ACTIVITY_ID` (activity_task_id),
    key `INX_EXPERIMENT_R_TASK_ID` (experiment_task_id)
)
    ENGINE = InnoDB
    comment 'experiment activity task_ record'
    DEFAULT CHARSET = utf8;

create table if not exists t_chaos_experiment_mini_flow
(
    id            bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create    datetime        not null comment 'create time',
    gmt_modified  datetime        not null comment 'modified time',
    group_id      bigint unsigned not null comment 'group id',
    experiment_id bigint unsigned not null comment 'experiment id'
)
    comment 'experiment mini flow';

create table if not exists t_chaos_experiment_mini_flow_group
(
    id            bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create    datetime        not null comment 'create time',
    gmt_modified  datetime        not null comment 'modified time',
    group_name    varchar(200)    not null comment 'group name',
    experiment_id bigint unsigned not null comment 'experiment id',
    hosts         longtext        not null comment 'experiment machine info'
)
    comment 'experiment mini flow group';

create table if not exists t_chaos_experiment_task
(
    id               bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create       datetime           null comment 'create time',
    gmt_modified     datetime           null comment 'modified time',
    task_name        varchar(64)        null comment 'task name',
    experiment_id    bigint             not null comment 'experiment id',
    activity_id      varchar(500)       null comment 'current activity id',
    activity_task_id varchar(64)        null comment 'current activity task id',
    gmt_start        datetime           null comment 'start time',
    gmt_end          datetime           null comment 'end time',
    task_type        tinyint unsigned   null comment 'task type，1(auto),0(manual）',
    result           text               null comment 'result',
    hosts            longtext           null comment 'experiment machine info',
    run_status       tinyint unsigned   null comment 'run status',
    result_status    tinyint unsigned   null comment 'result status',
    error_message    text               null comment 'error message',
    duration         int(255) default 0 not null comment 'duration',
    metric           longtext           null comment 'metric config',
    key `INX_EXPERIMENT_TASK_EXP_ID` (experiment_id)
)
    comment 'experiment task' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_experiment_task_log
(
    id               bigint unsigned auto_increment
        primary key,
    gmt_create       datetime        not null comment 'create time',
    gmt_modified     datetime        not null comment 'modified time',
    content          longtext        not null comment 'content zh',
    content_en       longtext default null comment 'content en',
    log_date         datetime        not null comment 'log date',
    task_id          bigint unsigned null comment 'task id',
    activity_task_id bigint unsigned null comment 'activity task id',
    key `INX_EXPERIMENT_TASK_LOG_TASK_ID` (task_id)
)
    comment 'experiment task log' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_probes
(
    id               bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create       datetime         null comment 'create time',
    gmt_modified     datetime         null comment 'modified time',
    install_mode     tinyint unsigned null comment 'install mode',
    success          tinyint unsigned null comment 'success',
    version          varchar(64)      null comment 'version',
    device_id        bigint           null comment 'device id',
    ip               varchar(32)      not null comment 'ip',
    hostname         varchar(256)     null comment 'hostname',
    cluster_id       varchar(256)     null comment 'cluster id',
    cluster_name     varchar(256)     null comment 'cluster name',
    node_name        varchar(256)     null comment 'node name',
    agent_type       tinyint unsigned null comment '0-Host，1-Kubernetes',
    status           tinyint(3)       null comment 'status',
    error_message    longtext         null comment 'error message',
    last_ping_time   datetime         null comment 'last ping time',
    last_online_time datetime         null comment 'last ping result time',
    deploy_blade     tinyint(1) default 1 comment 'deploy blade',
    constraint `PROBES_IP_AGENT_TYPE` unique (ip, agent_type)
)
    comment 'probes' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_scene
(
    id                  bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create          datetime                   not null comment 'create time',
    gmt_modified        datetime                   not null comment 'modified time',
    categories          longtext                   null comment 'categories',
    scene_code          varchar(100)               not null comment 'scene code',
    scene_name          varchar(300)               not null comment 'scene name',
    pre_scene_id        bigint unsigned            null comment 'pre scene id',
    next_scene_id       bigint unsigned            null comment 'next scene id',
    description         varchar(600)               null comment 'description',
    version             varchar(10)                not null comment 'version',
    status              tinyint unsigned default 0 not null comment 'status',
    use_count           int                        null comment 'use count',
    scene_order         int                        null comment 'scene order',
    support_phase       tinyint                    null comment 'support phase, 0/1/2',
    original            varchar(100)               null comment 'original',
    support_scope       longtext                   null comment 'support scope',
    required_java_agent tinyint          default 0 null comment 'required java agent'
)
    comment 'scene' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_scene_category
(
    id            bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create    datetime         not null comment 'create time',
    gmt_modified  datetime         not null comment 'modified time',
    name          varchar(300)     not null comment 'category name',
    category_code varchar(128) comment 'category code',
    parent_id     bigint unsigned  null comment 'parent id',
    level         tinyint unsigned not null comment 'level',
    support_scope longtext         null comment 'support scope'
)
    comment 'scene category' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_scene_param
(
    id            bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_create    datetime                   not null comment 'create time',
    gmt_modified  datetime                   not null comment 'modified time',
    scene_id      bigint unsigned            not null comment 'scene id',
    param_name    varchar(100)               not null comment 'param name',
    alias         varchar(300)               not null comment 'alias',
    description   varchar(600)               null comment 'description',
    param_order   int              default 0 not null comment 'param order',
    default_value varchar(200)               null comment 'default value',
    is_required   tinyint unsigned default 0 not null comment 'is required',
    component     longtext                   null comment 'component'
)
    comment 'scene param' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_tools
(
    id           bigint unsigned auto_increment comment 'primary key'
        primary key,
    gmt_modified datetime         not null comment 'modified time',
    gmt_create   datetime         not null comment 'create time',
    device_id    bigint           not null,
    name         varchar(128)     not null comment 'tools name',
    version      varchar(128)     not null comment 'version',
    url          varchar(1024)    not null comment 'download url',
    device_type  tinyint unsigned null comment 'device type'
)
    comment 'chaos tools' DEFAULT CHARSET = utf8;

create table if not exists t_chaos_metric_category
(
    id           bigint(100) unsigned auto_increment
        primary key,
    gmt_create   datetime               not null comment 'modified time',
    gmt_modified datetime               not null comment 'create time',
    name         varchar(256)           null,
    parent_id    bigint                 null comment 'parent id',
    level        bigint                 null comment 'level',
    unit         varchar(50) default '' null comment 'unit',
    code         varchar(50)            not null comment 'code',
    params       longtext               null
)
    ENGINE = InnoDB comment 'metric category'
    DEFAULT CHARSET = utf8;

create table if not exists `t_chaos_metric_task`
(
    `id`            bigint(100) unsigned NOT NULL AUTO_INCREMENT,
    `gmt_create`    datetime             NOT NULL COMMENT 'modified time',
    `gmt_modified`  datetime             NOT NULL COMMENT 'create time',
    `task_id`       bigint(20) unsigned  NOT NULL,
    `device_id`     bigint(20) unsigned DEFAULT NULL,
    `ip`            varchar(64)         DEFAULT NULL COMMENT 'ip',
    `hostname`      varchar(100)        DEFAULT NULL COMMENT 'hostname',
    `date`          datetime             NOT NULL COMMENT 'record date',
    `value`         varchar(128)         NOT NULL COMMENT 'value',
    `unit`          varchar(50)         DEFAULT NULL COMMENT 'unit',
    `category_id`   bigint(20)           NOT NULL COMMENT 'category id',
    `category_code` varchar(50)          NOT NULL COMMENT 'category code',
    `metric`        longtext            DEFAULT NULL COMMENT 'metric',
    PRIMARY KEY (`id`),
    key `INX_METRIC_TASK_TASK_ID` (task_id),
    key `INX_METRIC_TASK_DATE` (date)
) ENGINE = InnoDB COMMENT 'metric task'
  DEFAULT CHARSET = utf8;





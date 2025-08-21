# 压测集成功能说明

## 概述

本功能实现了在故障演练过程中集成压测的能力，支持在故障注入前启动压测，并在演练过程中管理压测任务的生命周期。

## 功能特性

### 1. 压测策略管理
- 支持为演练计划配置压测策略
- 可设置压测启动时机（故障注入前多少秒启动）
- 支持压测持续时间配置
- 支持压测失败时是否中止演练的策略

### 2. 压测任务生命周期管理
- 自动在演练开始时创建压测任务
- 支持压测任务状态实时同步
- 演练停止时自动停止关联的压测任务
- 支持手动停止压测任务

### 3. 压测引擎集成
- 支持与JMeter压测引擎的集成
- 提供压测启动、停止、状态查询等API
- 支持获取压测结果和事件流水

## 数据库表结构

### 压测任务表 (t_chaos_load_test_task)
```sql
CREATE TABLE IF NOT EXISTS `t_chaos_load_test_task` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `task_id` varchar(64) NOT NULL COMMENT '压测任务ID',
  `strategy_id` varchar(64) NOT NULL COMMENT '压测策略ID',
  `experiment_task_id` varchar(64) NOT NULL COMMENT '演练任务ID',
  `execution_id` varchar(128) DEFAULT NULL COMMENT '压测引擎执行ID',
  `status` varchar(32) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `error_message` text COMMENT '错误信息',
  `result_path` varchar(512) DEFAULT NULL COMMENT '结果文件路径',
  `report_path` varchar(512) DEFAULT NULL COMMENT '报告路径',
  `log_path` varchar(512) DEFAULT NULL COMMENT '日志路径',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `namespace` varchar(64) NOT NULL DEFAULT 'default' COMMENT '命名空间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_id` (`task_id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_experiment_task_id` (`experiment_task_id`),
  KEY `idx_execution_id` (`execution_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='压测任务表';
```

## 配置说明

### 1. 压测引擎配置
在 `application.yml` 中配置压测引擎地址：
```yaml
chaosblade:
  loadtest:
    engine:
      host: localhost:8008
```

### 2. 压测策略配置
压测策略通过压测策略管理接口进行配置，主要参数包括：
- `definitionId`: 压测定义ID，关联具体的JMX文件
- `experimentId`: 关联的演练计划ID
- `startBeforeFaultSec`: 在故障注入前多少秒启动压测
- `trafficDurationSec`: 压测持续时间（秒）
- `abortOnLoadFailure`: 压测失败时是否中止演练

## API接口

### 1. 压测任务查询
- `GET /GetLoadTestTask`: 根据任务ID查询压测任务
- `GET /GetLoadTestTaskByExperimentTaskId`: 根据演练任务ID查询压测任务
- `POST /QueryLoadTestTasks`: 分页查询压测任务

### 2. 压测任务控制
- `POST /StopLoadTestTask`: 停止压测任务
- `POST /SyncLoadTestTaskStatus`: 同步压测任务状态

### 3. 压测结果获取
- `GET /GetLoadTestResults`: 获取压测结果
- `GET /GetLoadTestEvents`: 获取压测事件流水

## 使用流程

### 1. 创建压测定义
首先需要创建压测定义，上传JMX文件并配置压测参数。

### 2. 配置压测策略
为演练计划配置压测策略，设置压测启动时机和持续时间。

### 3. 启动演练
启动演练时，系统会自动检查是否有关联的压测策略：
- 如果有压测策略且已启用，会在指定时间启动压测
- 压测任务与演练任务并行执行
- 可通过API查询压测任务状态

### 4. 演练完成
演练完成时，系统会根据策略决定是否停止压测：
- 手动停止演练：自动停止压测
- 异常结束演练：自动停止压测
- 正常完成演练：压测继续运行（可配置）

## 状态管理

### 压测任务状态
- `PENDING`: 等待中
- `RUNNING`: 运行中
- `SUCCEEDED`: 成功
- `FAILED`: 失败
- `STOPPED`: 已停止
- `TIMEOUT`: 超时

### 状态同步机制
- 定时任务每30秒同步一次压测任务状态
- 支持手动触发状态同步
- 异常状态自动处理和恢复

## 监控和日志

### 1. 压测监控
- 定时监控所有运行中的压测任务
- 自动处理超时和异常任务
- 提供压测任务执行统计

### 2. 日志记录
- 详细记录压测任务的创建、启动、停止过程
- 记录与压测引擎的交互日志
- 异常情况的错误日志和处理记录

## 注意事项

### 1. 压测引擎依赖
- 需要部署独立的JMeter压测引擎
- 确保压测引擎服务可用性
- 配置正确的压测引擎地址

### 2. 资源管理
- 压测会消耗额外的系统资源
- 建议在测试环境中使用
- 注意压测对目标系统的影响

### 3. 数据清理
- 定期清理过期的压测任务数据
- 清理压测结果文件和日志
- 监控数据库存储空间

## 故障排查

### 1. 压测启动失败
- 检查压测引擎服务状态
- 验证JMX文件路径和内容
- 检查网络连接和权限

### 2. 状态同步异常
- 检查压测引擎API可用性
- 查看同步任务日志
- 验证数据库连接状态

### 3. 演练集成问题
- 检查压测策略配置
- 验证演练任务状态
- 查看拦截器执行日志

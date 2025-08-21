# 压测策略管理功能

## 功能概述

压测策略管理功能提供了完整的压测策略CRUD操作，支持将压测定义与实验进行关联，实现在混沌工程实验中集成压测能力。

## 数据模型

### 压测策略数据结构

```json
{
  "id": "lstrategy_01G...",                // 策略ID，主键
  "enable": true,                          // 是否启用
  "definitionId": "ldef_01H...",          // 压测定义ID，关联t_chaos_load_test_definition表
  "experimentId": "1957308844015296513",   // 实验ID，关联t_chaos_experiment表
  "startBeforeFaultSec": 45,              // 故障前预热时间（秒）
  "trafficDurationSec": 600,              // 压测持续时长（秒）
  "abortOnLoadFailure": true,             // 压测异常时是否中止演练
  "createdAt": "2023-12-01T10:00:00+08:00",
  "updatedAt": "2023-12-01T10:00:00+08:00"
}
```

## 数据库表结构

```sql
CREATE TABLE IF NOT EXISTS `t_chaos_load_test_strategy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `strategy_id` varchar(64) NOT NULL COMMENT '策略ID',
  `enable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否启用：0-禁用，1-启用',
  `definition_id` varchar(64) NOT NULL COMMENT '压测定义ID',
  `experiment_id` varchar(64) NOT NULL COMMENT '实验ID',
  `start_before_fault_sec` int(11) NOT NULL DEFAULT '0' COMMENT '故障前预热时间（秒）',
  `traffic_duration_sec` int(11) NOT NULL DEFAULT '60' COMMENT '压测持续时长（秒）',
  `abort_on_load_failure` tinyint(4) NOT NULL DEFAULT '0' COMMENT '压测异常时是否中止演练',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `namespace` varchar(64) NOT NULL DEFAULT 'default' COMMENT '命名空间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_strategy_id` (`strategy_id`),
  UNIQUE KEY `uk_experiment_id` (`experiment_id`, `is_delete`),
  KEY `idx_definition_id` (`definition_id`),
  KEY `idx_user_namespace` (`user_id`, `namespace`),
  KEY `idx_enable` (`enable`),
  KEY `idx_gmt_create` (`gmt_create`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='压测策略表';
```

## 业务约束

1. **唯一性约束**：一个实验只能绑定一个压测策略（experiment_id唯一性约束）
2. **关联性约束**：压测定义必须存在才能创建策略
3. **关联性约束**：实验必须存在才能创建策略
4. **权限约束**：用户只能操作自己命名空间下的策略

## API 接口

### 1. 创建压测策略

**接口**: `POST /chaos/CreateLoadTestStrategy`

**请求参数**:
```json
{
  "enable": true,
  "definitionId": "ldef_01H...",
  "experimentId": "1957308844015296513",
  "startBeforeFaultSec": 45,
  "trafficDurationSec": 600,
  "abortOnLoadFailure": true,
  "namespace": "default"
}
```

**响应**:
```json
{
  "success": true,
  "result": "lstrategy_xxxxxxxxxx"
}
```

### 2. 更新压测策略

**接口**: `POST /chaos/UpdateLoadTestStrategy`

**请求参数**:
```json
{
  "id": "lstrategy_xxxxxxxxxx",
  "enable": false,
  "startBeforeFaultSec": 60,
  "trafficDurationSec": 300,
  "namespace": "default"
}
```

### 3. 删除压测策略

**接口**: `POST /chaos/DeleteLoadTestStrategy`

**请求参数**:
```
id=lstrategy_xxxxxxxxxx&namespace=default
```

### 4. 查询压测策略详情

**接口**: `POST /chaos/GetLoadTestStrategy`

**请求参数**:
```
id=lstrategy_xxxxxxxxxx&namespace=default
```

**响应**:
```json
{
  "success": true,
  "result": {
    "id": "lstrategy_xxxxxxxxxx",
    "enable": true,
    "definitionId": "ldef_01H...",
    "experimentId": "1957308844015296513",
    "startBeforeFaultSec": 45,
    "trafficDurationSec": 600,
    "abortOnLoadFailure": true,
    "createdAt": "2023-12-01T10:00:00+08:00",
    "updatedAt": "2023-12-01T10:00:00+08:00"
  }
}
```

### 5. 根据实验ID查询压测策略

**接口**: `POST /chaos/GetLoadTestStrategyByExperimentId`

**请求参数**:
```
experimentId=1957308844015296513&namespace=default
```

### 6. 根据压测定义ID查询策略列表

**接口**: `POST /chaos/GetLoadTestStrategiesByDefinitionId`

**请求参数**:
```
definitionId=ldef_01H...&namespace=default
```

### 7. 分页查询压测策略

**接口**: `POST /chaos/QueryLoadTestStrategies`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 20,
  "definitionId": "ldef_01H...",
  "experimentId": "1957308844015296513",
  "enable": true,
  "namespace": "default"
}
```

**响应**:
```json
{
  "success": true,
  "result": {
    "page": 1,
    "pageSize": 20,
    "total": 1,
    "pages": 1,
    "data": [
      {
        "id": "lstrategy_xxxxxxxxxx",
        "enable": true,
        "definitionId": "ldef_01H...",
        "experimentId": "1957308844015296513",
        "startBeforeFaultSec": 45,
        "trafficDurationSec": 600,
        "abortOnLoadFailure": true,
        "createdAt": "2023-12-01T10:00:00+08:00",
        "updatedAt": "2023-12-01T10:00:00+08:00"
      }
    ]
  }
}
```

### 8. 查询所有压测策略（不分页）

**接口**: `POST /chaos/ListAllLoadTestStrategies`

**请求参数**:
```
namespace=default
```

## 代码架构

### 1. DAO 层

- **实体类**: `LoadTestStrategyDO` - 压测策略数据对象
- **Mapper**: `LoadTestStrategyMapper` - MyBatis Plus 映射接口
- **Repository**: `LoadTestStrategyRepository` - 数据访问层封装

### 2. Service 层

- **接口**: `LoadTestStrategyService` - 业务逻辑接口
- **实现**: `LoadTestStrategyServiceImpl` - 业务逻辑实现
- **模型类**:
  - `LoadTestStrategyVO` - 视图对象
  - `LoadTestStrategyCreateRequest` - 创建请求
  - `LoadTestStrategyUpdateRequest` - 更新请求
  - `LoadTestStrategyQueryRequest` - 查询请求

### 3. Controller 层

- **控制器**: `LoadTestStrategyController` - REST API 控制器

## 测试用例

### 1. 单元测试

- `LoadTestStrategyServiceImplTest` - Service层单元测试
- `LoadTestStrategyControllerTest` - Controller层单元测试
- `LoadTestStrategyRepositoryTest` - Repository层单元测试

### 2. 集成测试

- `LoadTestStrategyIntegrationTest` - 完整功能集成测试

## 使用示例

### 创建压测策略

```bash
curl -X POST http://localhost:8080/chaos/CreateLoadTestStrategy \
  -H "Content-Type: application/json" \
  -d '{
    "enable": true,
    "definitionId": "ldef_01H...",
    "experimentId": "1957308844015296513",
    "startBeforeFaultSec": 45,
    "trafficDurationSec": 600,
    "abortOnLoadFailure": true,
    "namespace": "default"
  }'
```

### 查询压测策略

```bash
curl -X POST "http://localhost:8080/chaos/GetLoadTestStrategy?id=lstrategy_xxxxxxxxxx&namespace=default"
```

### 更新压测策略

```bash
curl -X POST http://localhost:8080/chaos/UpdateLoadTestStrategy \
  -H "Content-Type: application/json" \
  -d '{
    "id": "lstrategy_xxxxxxxxxx",
    "enable": false,
    "startBeforeFaultSec": 60
  }'
```

### 删除压测策略

```bash
curl -X POST "http://localhost:8080/chaos/DeleteLoadTestStrategy?id=lstrategy_xxxxxxxxxx&namespace=default"
```

## 注意事项

1. 所有接口都需要用户登录认证
2. 用户只能操作自己命名空间下的策略
3. 一个实验只能绑定一个压测策略
4. 删除操作为逻辑删除，不会物理删除数据
5. 创建策略前需要确保压测定义和实验都已存在

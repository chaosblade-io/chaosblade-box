# 压测策略管理功能 - Postman 测试用例

## 环境配置

**基础URL**: `http://localhost:7001`
**Content-Type**: `application/json`

## 前置条件

在测试压测策略之前，需要先创建测试数据：

### 1. 创建压测定义（前置数据）

```http
POST http://localhost:7001/CreateLoadTestDefinition
Content-Type: application/json

{
  "name": "测试压测定义",
  "engineType": "JMETER",
  "endpoint": "http://test.example.com",
  "entry": "URL",
  "urlCase": {
    "method": "GET",
    "path": "/api/test",
    "headers": {},
    "params": {}
  },
  "namespace": "default"
}
```

### 2. 创建实验（前置数据）

```http
POST http://localhost:7001/CreateExperiment
Content-Type: application/json

{
  "name": "测试实验",
  "description": "用于测试压测策略的实验",
  "namespace": "default"
}
```

## 压测策略测试用例

### 测试用例 1: 创建压测策略

**接口**: `POST /CreateLoadTestStrategy`

```http
POST http://localhost:7001/CreateLoadTestStrategy
Content-Type: application/json

{
  "enable": true,
  "definitionId": "ldef_xxxxxxxxxx",
  "experimentId": "1234567890123456789",
  "startBeforeFaultSec": 45,
  "trafficDurationSec": 600,
  "abortOnLoadFailure": true,
  "namespace": "default"
}
```

**预期结果**:
```json
{
  "success": true,
  "result": "lstrategy_xxxxxxxxxx"
}
```

### 测试用例 2: 查询压测策略详情

**接口**: `POST /GetLoadTestStrategy`

```http
POST http://localhost:7001/GetLoadTestStrategy?id=lstrategy_xxxxxxxxxx&namespace=default
```

**预期结果**:
```json
{
  "success": true,
  "result": {
    "id": "lstrategy_xxxxxxxxxx",
    "enable": true,
    "definitionId": "ldef_xxxxxxxxxx",
    "experimentId": "1234567890123456789",
    "startBeforeFaultSec": 45,
    "trafficDurationSec": 600,
    "abortOnLoadFailure": true,
    "createdAt": "2023-12-01T10:00:00+08:00",
    "updatedAt": "2023-12-01T10:00:00+08:00"
  }
}
```

### 测试用例 3: 更新压测策略

**接口**: `POST /UpdateLoadTestStrategy`

```http
POST http://localhost:7001/UpdateLoadTestStrategy
Content-Type: application/json

{
  "id": "lstrategy_xxxxxxxxxx",
  "enable": false,
  "startBeforeFaultSec": 60,
  "trafficDurationSec": 300,
  "abortOnLoadFailure": false,
  "namespace": "default"
}
```

**预期结果**:
```json
{
  "success": true
}
```

### 测试用例 4: 根据实验ID查询压测策略

**接口**: `POST /GetLoadTestStrategyByExperimentId`

```http
POST http://localhost:7001/GetLoadTestStrategyByExperimentId?experimentId=1234567890123456789&namespace=default
```

**预期结果**:
```json
{
  "success": true,
  "result": {
    "id": "lstrategy_xxxxxxxxxx",
    "enable": false,
    "definitionId": "ldef_xxxxxxxxxx",
    "experimentId": "1234567890123456789",
    "startBeforeFaultSec": 60,
    "trafficDurationSec": 300,
    "abortOnLoadFailure": false,
    "createdAt": "2023-12-01T10:00:00+08:00",
    "updatedAt": "2023-12-01T10:00:00+08:00"
  }
}
```

### 测试用例 5: 根据压测定义ID查询策略列表

**接口**: `POST /GetLoadTestStrategiesByDefinitionId`

```http
POST http://localhost:7001/GetLoadTestStrategiesByDefinitionId?definitionId=ldef_xxxxxxxxxx&namespace=default
```

**预期结果**:
```json
{
  "success": true,
  "result": [
    {
      "id": "lstrategy_xxxxxxxxxx",
      "enable": false,
      "definitionId": "ldef_xxxxxxxxxx",
      "experimentId": "1234567890123456789",
      "startBeforeFaultSec": 60,
      "trafficDurationSec": 300,
      "abortOnLoadFailure": false,
      "createdAt": "2023-12-01T10:00:00+08:00",
      "updatedAt": "2023-12-01T10:00:00+08:00"
    }
  ]
}
```

### 测试用例 6: 分页查询压测策略

**接口**: `POST /QueryLoadTestStrategies`

```http
POST http://localhost:7001/QueryLoadTestStrategies
Content-Type: application/json

{
  "pageNum": 1,
  "pageSize": 10,
  "definitionId": "ldef_xxxxxxxxxx",
  "enable": false,
  "namespace": "default"
}
```

**预期结果**:
```json
{
  "success": true,
  "result": {
    "page": 1,
    "pageSize": 10,
    "total": 1,
    "pages": 1,
    "data": [
      {
        "id": "lstrategy_xxxxxxxxxx",
        "enable": false,
        "definitionId": "ldef_xxxxxxxxxx",
        "experimentId": "1234567890123456789",
        "startBeforeFaultSec": 60,
        "trafficDurationSec": 300,
        "abortOnLoadFailure": false,
        "createdAt": "2023-12-01T10:00:00+08:00",
        "updatedAt": "2023-12-01T10:00:00+08:00"
      }
    ]
  }
}
```

### 测试用例 7: 查询所有压测策略（不分页）

**接口**: `POST /ListAllLoadTestStrategies`

```http
POST http://localhost:7001/ListAllLoadTestStrategies?namespace=default
```

**预期结果**:
```json
{
  "success": true,
  "result": [
    {
      "id": "lstrategy_xxxxxxxxxx",
      "enable": false,
      "definitionId": "ldef_xxxxxxxxxx",
      "experimentId": "1234567890123456789",
      "startBeforeFaultSec": 60,
      "trafficDurationSec": 300,
      "abortOnLoadFailure": false,
      "createdAt": "2023-12-01T10:00:00+08:00",
      "updatedAt": "2023-12-01T10:00:00+08:00"
    }
  ]
}
```

### 测试用例 8: 删除压测策略

**接口**: `POST /DeleteLoadTestStrategy`

```http
POST http://localhost:7001/DeleteLoadTestStrategy?id=lstrategy_xxxxxxxxxx&namespace=default
```

**预期结果**:
```json
{
  "success": true
}
```

## 异常场景测试用例

### 测试用例 9: 创建重复绑定的策略（应该失败）

```http
POST http://localhost:7001/CreateLoadTestStrategy
Content-Type: application/json

{
  "enable": true,
  "definitionId": "ldef_xxxxxxxxxx",
  "experimentId": "1234567890123456789",
  "startBeforeFaultSec": 30,
  "trafficDurationSec": 300,
  "abortOnLoadFailure": false,
  "namespace": "default"
}
```

**预期结果**:
```json
{
  "success": false,
  "message": "该实验已绑定压测策略"
}
```

### 测试用例 10: 查询不存在的策略（应该失败）

```http
POST http://localhost:7001/GetLoadTestStrategy?id=lstrategy_nonexistent&namespace=default
```

**预期结果**:
```json
{
  "success": false,
  "message": "压测策略不存在"
}
```

### 测试用例 11: 使用不存在的压测定义创建策略（应该失败）

```http
POST http://localhost:7001/CreateLoadTestStrategy
Content-Type: application/json

{
  "enable": true,
  "definitionId": "ldef_nonexistent",
  "experimentId": "9876543210987654321",
  "startBeforeFaultSec": 45,
  "trafficDurationSec": 600,
  "abortOnLoadFailure": true,
  "namespace": "default"
}
```

**预期结果**:
```json
{
  "success": false,
  "message": "压测定义不存在"
}
```

### 测试用例 12: 使用不存在的实验创建策略（应该失败）

```http
POST http://localhost:7001/CreateLoadTestStrategy
Content-Type: application/json

{
  "enable": true,
  "definitionId": "ldef_xxxxxxxxxx",
  "experimentId": "nonexistent_experiment",
  "startBeforeFaultSec": 45,
  "trafficDurationSec": 600,
  "abortOnLoadFailure": true,
  "namespace": "default"
}
```

**预期结果**:
```json
{
  "success": false,
  "message": "实验不存在"
}
```

## 测试执行步骤

### 第一步：准备测试环境

1. **启动应用**
   ```bash
   cd /data/mj/chaosblade-box/chaosblade-box-starter/target
   java -jar chaosblade-box-1.0.5.jar
   ```

2. **验证服务启动**
   ```http
   GET http://localhost:7001/actuator/health
   ```

### 第二步：创建前置数据

1. **创建压测定义**（记录返回的definitionId）
2. **创建实验**（记录返回的experimentId）

### 第三步：执行压测策略测试

按照以下顺序执行测试用例：

1. **创建压测策略** → 记录返回的strategyId
2. **查询策略详情** → 验证数据正确性
3. **更新策略** → 验证更新功能
4. **根据实验ID查询** → 验证关联查询
5. **根据定义ID查询** → 验证关联查询
6. **分页查询** → 验证查询功能
7. **查询所有策略** → 验证列表功能
8. **异常场景测试** → 验证业务约束
9. **删除策略** → 验证删除功能

### 第四步：验证业务约束

1. **唯一性约束测试**：尝试为同一个实验创建第二个策略
2. **关联性约束测试**：使用不存在的定义ID或实验ID
3. **权限约束测试**：使用不同的namespace

## 注意事项

### 1. 用户认证

如果系统启用了用户认证，需要在请求头中添加认证信息：

```
Cookie: JSESSIONID=xxxxxxxxxx
```

或者在Session中设置用户信息。

### 2. 数据库准备

确保数据库中已经创建了相关表：
- `t_chaos_load_test_definition`
- `t_chaos_experiment`
- `t_chaos_load_test_strategy`

### 3. ID格式说明

- **策略ID**: `lstrategy_` + 32位随机字符串
- **定义ID**: `ldef_` + 32位随机字符串
- **实验ID**: 19位数字字符串

### 4. 时间格式

所有时间字段使用ISO 8601格式：`yyyy-MM-dd'T'HH:mm:ssXXX`

## 常见问题排查

### 1. 404 Not Found
- 检查URL路径是否正确
- 确认服务是否正常启动

### 2. 500 Internal Server Error
- 检查数据库连接
- 查看应用日志

### 3. 业务逻辑错误
- 确认前置数据是否存在
- 检查参数格式是否正确

### 4. 权限错误
- 确认用户是否已登录
- 检查namespace是否正确

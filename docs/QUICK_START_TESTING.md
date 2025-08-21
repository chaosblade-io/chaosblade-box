# 压测策略管理功能 - 快速测试指南

## 🚀 快速开始

### 1. 启动应用

```bash
cd /data/mj/chaosblade-box/chaosblade-box-starter/target
java -jar chaosblade-box-1.0.5.jar
```

应用将在端口 **7001** 启动。

### 2. 验证服务状态

在浏览器或Postman中访问：
```
GET http://localhost:7001/actuator/health
```

看到类似响应表示服务正常：
```json
{"status":"UP"}
```

## 📋 Postman 测试步骤

### 第一步：导入测试集合

1. 打开Postman
2. 点击 **Import** 
3. 选择文件 `LoadTestStrategy_Postman_Collection.json`
4. 导入成功后会看到 "压测策略管理API测试" 集合

### 第二步：设置环境变量

在Postman中设置以下变量：

| 变量名 | 值 | 说明 |
|--------|-----|------|
| `baseUrl` | `http://localhost:7001` | 服务基础URL |
| `definitionId` | `ldef_xxxxxxxxxx` | 压测定义ID（需要先创建） |
| `experimentId` | `1234567890123456789` | 实验ID（需要先创建） |

### 第三步：创建前置数据

#### 3.1 创建压测定义

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
    "path": "/api/test"
  },
  "namespace": "default"
}
```

**记录返回的 `definitionId`**

#### 3.2 创建实验

```http
POST http://localhost:7001/CreateExperiment
Content-Type: application/json

{
  "name": "测试实验",
  "description": "用于测试压测策略的实验",
  "namespace": "default"
}
```

**记录返回的 `experimentId`**

### 第四步：执行压测策略测试

按顺序执行以下测试：

1. **创建压测策略** ✅
2. **查询策略详情** ✅
3. **更新策略** ✅
4. **根据实验ID查询** ✅
5. **根据定义ID查询** ✅
6. **分页查询** ✅
7. **查询所有策略** ✅
8. **删除策略** ✅
9. **异常测试** ✅

## 🧪 核心测试用例

### 测试用例 1：创建压测策略

```http
POST http://localhost:7001/CreateLoadTestStrategy
Content-Type: application/json

{
  "enable": true,
  "definitionId": "{{definitionId}}",
  "experimentId": "{{experimentId}}",
  "startBeforeFaultSec": 45,
  "trafficDurationSec": 600,
  "abortOnLoadFailure": true,
  "namespace": "default"
}
```

**期望响应**：
```json
{
  "success": true,
  "result": "lstrategy_xxxxxxxxxx"
}
```

### 测试用例 2：查询策略详情

```http
POST http://localhost:7001/GetLoadTestStrategy?id={{strategyId}}&namespace=default
```

**期望响应**：
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

### 测试用例 3：更新策略

```http
POST http://localhost:7001/UpdateLoadTestStrategy
Content-Type: application/json

{
  "id": "{{strategyId}}",
  "enable": false,
  "startBeforeFaultSec": 60,
  "trafficDurationSec": 300,
  "namespace": "default"
}
```

## 🔍 验证要点

### 1. 功能验证

- ✅ 创建策略成功，返回策略ID
- ✅ 查询策略返回正确数据
- ✅ 更新策略生效
- ✅ 关联查询正常工作
- ✅ 分页查询返回正确格式
- ✅ 删除策略成功

### 2. 业务约束验证

- ✅ 一个实验只能绑定一个策略
- ✅ 不存在的定义ID创建失败
- ✅ 不存在的实验ID创建失败
- ✅ 查询不存在的策略返回失败

### 3. 数据格式验证

- ✅ 策略ID格式：`lstrategy_` + 32位字符
- ✅ 时间格式：ISO 8601
- ✅ 布尔值正确转换
- ✅ 分页数据结构正确

## 🚨 常见问题

### 问题 1：404 Not Found
**原因**：URL路径错误或服务未启动
**解决**：检查URL和服务状态

### 问题 2：500 Internal Server Error
**原因**：数据库连接问题或业务逻辑错误
**解决**：检查数据库配置和应用日志

### 问题 3：业务逻辑错误
**原因**：前置数据不存在或参数错误
**解决**：确认压测定义和实验已创建

### 问题 4：权限错误
**原因**：用户未登录或namespace不匹配
**解决**：检查用户认证和namespace参数

## 📊 测试报告模板

```
测试日期：2023-12-01
测试环境：localhost:7001
测试人员：[您的姓名]

测试结果：
✅ 创建压测策略：通过
✅ 查询策略详情：通过
✅ 更新策略：通过
✅ 根据实验ID查询：通过
✅ 根据定义ID查询：通过
✅ 分页查询：通过
✅ 查询所有策略：通过
✅ 删除策略：通过
✅ 异常场景测试：通过

总结：所有功能正常，可以投入使用。
```

## 🎯 下一步

测试完成后，您可以：

1. **集成到现有系统**：将压测策略与混沌工程实验流程集成
2. **性能测试**：测试大量数据下的查询性能
3. **安全测试**：验证权限控制和数据隔离
4. **监控配置**：设置业务指标监控和告警

祝您测试顺利！🎉

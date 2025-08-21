# 压测集成功能测试用例

## 用户故事

**作为一名** 混沌工程师  
**我希望** 在故障演练过程中同时进行压力测试  
**以便于** 更真实地评估系统在压力和故障双重条件下的稳定性和性能表现

## 测试场景概述

本测试用例将验证从压测文件上传到演练执行完成的完整流程，确保压测功能与演练系统的无缝集成。

## 压测集成逻辑说明

### 自动触发机制
1. **演练启动拦截**：通过 `LoadTestExperimentTaskInterceptor` 拦截器，在演练任务创建时自动检查是否有关联的压测策略
2. **异步启动**：如果发现启用的压测策略，会根据 `startBeforeFaultSec` 配置异步启动压测任务
3. **状态同步**：通过 `LoadTestMonitorService` 定时同步压测任务状态
4. **自动清理**：通过 `LoadTestExperimentTaskFinishedInterceptor` 在演练完成时自动处理压测任务

### 关键组件
- **拦截器**：`LoadTestExperimentTaskInterceptor`、`LoadTestExperimentTaskFinishedInterceptor`
- **任务管理器**：`LoadTestTaskManager`
- **监控服务**：`LoadTestMonitorService`
- **API控制器**：`LoadTestTaskController`

## 前置条件

1. 压测引擎服务已部署并运行在 `localhost:8008`
2. 演练系统已正常运行
3. 测试用户已登录系统
4. 实验ID `1958418358818971650` 已存在且可用
5. 准备好测试用的JMX压测脚本文件

## 测试数据准备

### 测试用户信息
```json
{
  "userId": "test_user_001",
  "namespace": "default",
  "userName": "测试用户"
}
```

### 测试JMX文件
- 文件名：`api_performance_test.jmx`
- 文件大小：< 10MB
- 内容：包含HTTP请求的标准JMeter测试计划

## 测试用例

### TC001: 压测文件上传

**测试目标**：验证JMX文件上传功能

**前置条件**：
- 用户已登录
- 准备好有效的JMX文件

**测试步骤**：
1. 调用文件上传接口
   ```
   POST /api/file/upload
   Content-Type: multipart/form-data
   
   file: api_performance_test.jmx
   type: loadtest
   ```

**预期结果**：
- 响应状态码：200
- 返回文件路径：`uploads/loadtest/api_performance_test.jmx`
- 文件成功保存到服务器

**验证点**：
- [ ] 文件上传成功
- [ ] 返回正确的文件路径
- [ ] 文件内容完整性校验

---

### TC002: 创建压测定义

**测试目标**：验证压测定义创建功能

**前置条件**：
- JMX文件已上传成功

**测试步骤**：
1. 调用创建压测定义接口
   ```
   POST /CreateLoadTestDefinition
   Content-Type: application/json
   
   {
     "name": "API性能测试定义",
     "description": "用于测试API接口性能的压测定义",
     "contentRef": "uploads/loadtest/api_performance_test.jmx",
     "endpoint": "http://test-api.example.com:8080",
     "namespace": "default"
   }
   ```

**预期结果**：
- 响应状态码：200
- 返回压测定义ID：`loadtest_def_001`
- 定义状态为启用

**验证点**：
- [ ] 压测定义创建成功
- [ ] 返回有效的定义ID
- [ ] 定义信息正确保存

---

### TC003: 创建压测策略

**测试目标**：验证压测策略创建并绑定到演练

**前置条件**：
- 压测定义已创建
- 实验ID `1958418358818971650` 存在

**测试步骤**：
1. 调用创建压测策略接口
   ```
   POST /CreateLoadTestStrategy
   Content-Type: application/json
   
   {
     "definitionId": "loadtest_def_001",
     "experimentId": "1958418358818971650",
     "name": "API压测策略",
     "description": "在故障注入前30秒启动压测",
     "startBeforeFaultSec": 30,
     "trafficDurationSec": 300,
     "abortOnLoadFailure": 0,
     "enable": 1,
     "namespace": "default"
   }
   ```

**预期结果**：
- 响应状态码：200
- 返回策略ID：`loadtest_strategy_001`
- 策略与实验成功绑定

**验证点**：
- [ ] 压测策略创建成功
- [ ] 策略与实验正确绑定
- [ ] 策略配置参数正确

---

### TC004: 查询压测策略绑定状态

**测试目标**：验证压测策略查询功能

**测试步骤**：
1. 调用查询压测策略接口
   ```
   GET /GetLoadTestStrategyByExperimentId?experimentId=1958418358818971650&namespace=default
   ```

**预期结果**：
- 响应状态码：200
- 返回绑定的压测策略信息
- 策略状态为启用

**验证点**：
- [ ] 成功查询到绑定的策略
- [ ] 策略信息完整正确
- [ ] 策略状态为启用

---

### TC005: 启动演练（触发压测）

**测试目标**：验证演练启动时自动触发压测

**前置条件**：
- 压测策略已绑定到实验
- 压测引擎服务正常

**测试步骤**：
1. 调用启动演练接口
   ```
   POST /RunExperiment
   Content-Type: application/json

   {
     "experimentId": "1958418358818971650",
     "namespace": "default"
   }
   ```

2. 等待30秒（压测启动时间）

3. 查询演练任务状态
   ```
   POST /QueryExperimentTask
   Content-Type: application/json

   {
     "taskId": "{experimentTaskId}"
   }
   ```

**预期结果**：
- 演练任务创建成功，返回 `ExperimentRunResult` 包含任务ID
- 30秒后压测任务自动启动（根据策略配置的 `startBeforeFaultSec`）
- 演练任务状态为运行中

**验证点**：
- [ ] 演练任务启动成功
- [ ] 压测任务自动创建（通过 `LoadTestExperimentTaskInterceptor` 拦截器）
- [ ] 压测在指定时间启动
- [ ] 可以在日志中看到压测启动相关信息

---

### TC006: 查询压测任务状态

**测试目标**：验证压测任务状态查询功能

**前置条件**：
- 演练已启动
- 压测任务已创建

**测试步骤**：
1. 根据演练任务ID查询压测任务
   ```
   GET /GetLoadTestTaskByExperimentTaskId?experimentTaskId={experimentTaskId}&namespace=default
   ```

2. 根据压测任务ID查询详细状态
   ```
   GET /GetLoadTestTask?taskId={loadTestTaskId}&namespace=default
   ```

3. 查询压测任务列表
   ```
   POST /QueryLoadTestTasks
   Content-Type: application/json
   
   {
     "pageNum": 1,
     "pageSize": 10,
     "experimentTaskId": "{experimentTaskId}",
     "namespace": "default"
   }
   ```

**预期结果**：
- 成功查询到压测任务
- 任务状态为 `RUNNING`
- 任务信息完整

**验证点**：
- [ ] 成功查询压测任务
- [ ] 任务状态正确
- [ ] 任务信息完整
- [ ] 分页查询正常

---

### TC007: 同步压测任务状态

**测试目标**：验证压测状态同步功能

**测试步骤**：
1. 调用状态同步接口
   ```
   POST /SyncLoadTestTaskStatus?taskId={loadTestTaskId}&namespace=default
   ```

2. 再次查询任务状态验证同步结果

**预期结果**：
- 状态同步成功
- 返回最新的任务状态
- 状态信息与压测引擎一致

**验证点**：
- [ ] 状态同步成功
- [ ] 返回最新状态
- [ ] 状态信息准确

---

### TC008: 获取压测结果和事件

**测试目标**：验证压测结果获取功能

**前置条件**：
- 压测任务正在运行

**测试步骤**：
1. 获取压测事件流水
   ```
   GET /GetLoadTestEvents?taskId={loadTestTaskId}&tail=50&namespace=default
   ```

2. 等待压测完成后获取结果
   ```
   GET /GetLoadTestResults?taskId={loadTestTaskId}&namespace=default
   ```

**预期结果**：
- 成功获取事件流水
- 压测完成后可获取结果文件
- 结果包含报告路径和下载链接

**验证点**：
- [ ] 成功获取事件流水
- [ ] 事件信息完整
- [ ] 结果文件可访问
- [ ] 报告链接有效

---

### TC009: 手动停止压测任务

**测试目标**：验证手动停止压测功能

**前置条件**：
- 压测任务正在运行

**测试步骤**：
1. 调用停止压测接口
   ```
   POST /StopLoadTestTask?taskId={loadTestTaskId}&namespace=default
   ```

2. 查询任务状态验证停止结果
   ```
   GET /GetLoadTestTask?taskId={loadTestTaskId}&namespace=default
   ```

**预期结果**：
- 压测任务停止成功
- 任务状态变为 `STOPPED`
- 演练任务继续运行

**验证点**：
- [ ] 压测停止成功
- [ ] 任务状态正确更新
- [ ] 演练任务不受影响

---

### TC010: 停止演练（验证压测清理）

**测试目标**：验证演练停止时压测自动清理

**前置条件**：
- 演练任务正在运行
- 压测任务正在运行

**测试步骤**：
1. 重新启动一个压测任务（如果之前已停止）

2. 调用停止演练接口
   ```
   POST /StopExperimentTask
   Content-Type: application/json

   {
     "taskId": "{experimentTaskId}"
   }
   ```

3. 查询压测任务状态
   ```
   GET /GetLoadTestTaskByExperimentTaskId?experimentTaskId={experimentTaskId}&namespace=default
   ```

**预期结果**：
- 演练任务停止成功
- 关联的压测任务自动停止
- 压测任务状态变为 `STOPPED`

**验证点**：
- [ ] 演练停止成功
- [ ] 压测自动停止
- [ ] 状态更新正确

---

## 异常场景测试

### TC011: 压测引擎不可用

**测试目标**：验证压测引擎不可用时的处理

**测试步骤**：
1. 停止压测引擎服务
2. 启动演练任务
3. 观察系统行为

**预期结果**：
- 演练任务正常启动
- 压测任务创建但启动失败
- 系统记录错误日志但不影响演练

**验证点**：
- [ ] 演练不受影响
- [ ] 错误处理正确
- [ ] 日志记录完整

---

### TC012: 无效JMX文件

**测试目标**：验证无效JMX文件的处理

**测试步骤**：
1. 上传无效的JMX文件
2. 创建压测定义
3. 启动演练

**预期结果**：
- 文件上传成功但压测启动失败
- 系统返回明确的错误信息
- 演练任务不受影响

**验证点**：
- [ ] 错误信息明确
- [ ] 演练正常执行
- [ ] 异常处理正确

---

## 性能测试

### TC013: 并发演练压测

**测试目标**：验证多个演练同时进行压测的性能

**测试步骤**：
1. 同时启动5个演练任务
2. 每个演练都绑定压测策略
3. 监控系统资源使用情况

**预期结果**：
- 所有演练和压测正常启动
- 系统资源使用在合理范围内
- 无性能瓶颈或死锁

**验证点**：
- [ ] 并发处理正常
- [ ] 资源使用合理
- [ ] 无性能问题

---

## 测试执行检查清单

### 环境准备
- [ ] 压测引擎服务已启动
- [ ] 数据库连接正常
- [ ] 测试数据已准备
- [ ] 网络连接正常

### 功能验证
- [ ] 文件上传功能正常
- [ ] 压测定义管理正常
- [ ] 压测策略绑定正常
- [ ] 演练启动触发压测正常
- [ ] 压测状态查询正常
- [ ] 压测结果获取正常
- [ ] 压测停止功能正常
- [ ] 演练停止清理正常

### 异常处理
- [ ] 压测引擎异常处理正常
- [ ] 无效文件处理正常
- [ ] 网络异常处理正常
- [ ] 并发场景处理正常

### 数据一致性
- [ ] 压测任务状态一致
- [ ] 演练任务关联正确
- [ ] 数据库记录完整
- [ ] 日志记录准确

## 测试报告模板

### 测试结果汇总
| 测试用例 | 执行状态 | 结果 | 备注 |
|---------|---------|------|------|
| TC001 | ✅ | 通过 | 文件上传正常 |
| TC002 | ✅ | 通过 | 定义创建正常 |
| ... | ... | ... | ... |

### 发现的问题
1. 问题描述
2. 重现步骤
3. 影响程度
4. 建议修复方案

### 测试结论
- 功能完整性：✅/❌
- 性能表现：✅/❌
- 稳定性：✅/❌
- 用户体验：✅/❌

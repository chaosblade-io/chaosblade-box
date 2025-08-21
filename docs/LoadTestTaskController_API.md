# 压测任务管理接口文档

## 概述

LoadTestTaskController 提供了完整的压测任务管理功能，包括任务查询、状态管理、结果获取和性能监控等。

**基础信息：**
- 控制器路径：`LoadTestTaskController`
- 认证方式：需要用户登录（@LoginUser）
- 响应格式：统一使用 `Response<T>` 包装

## 接口列表

### 1. 查询压测任务

#### 1.1 查询单个压测任务（智能查询）

**接口描述：** 支持通过压测任务ID或演练任务ID查询压测任务，系统会自动识别参数类型

**请求信息：**
```
GET /GetLoadTestTask
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| taskId | String | 是 | - | 任务ID（可以是压测任务ID或演练任务ID） |
| experimentTaskId | String | 否 | - | 演练任务ID（如果提供则优先使用此参数） |
| namespace | String | 否 | default | 命名空间 |

**响应数据：**
```json
{
  "success": true,
  "result": {
    "taskId": "task_123456",
    "strategyId": "strategy_001",
    "experimentTaskId": "exp_789012",
    "executionId": "exec_345678",
    "status": "RUNNING",
    "statusDescription": "运行中",
    "startTime": "2025-08-22T10:30:00+08:00",
    "endTime": null,
    "errorMessage": null,
    "resultPath": "/results/task_123456",
    "reportPath": "/reports/task_123456",
    "logPath": "/logs/task_123456",
    "createdAt": "2025-08-22T10:29:00+08:00",
    "updatedAt": "2025-08-22T10:30:00+08:00"
  }
}
```

**使用示例：**
```bash
# 通过压测任务ID查询
curl "http://localhost:7001/GetLoadTestTask?taskId=task_123456"

# 通过演练任务ID查询
curl "http://localhost:7001/GetLoadTestTask?taskId=exp_789012"

# 明确指定演练任务ID
curl "http://localhost:7001/GetLoadTestTask?taskId=any&experimentTaskId=exp_789012"
```

#### 1.2 根据演练任务ID查询压测任务

**接口描述：** 明确通过演练任务ID查询关联的压测任务

**请求信息：**
```
GET /GetLoadTestTaskByExperimentTaskId
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| experimentTaskId | String | 是 | - | 演练任务ID |
| namespace | String | 否 | default | 命名空间 |

**响应数据：** 同上述 LoadTestTaskVO 结构

#### 1.3 分页查询压测任务

**接口描述：** 支持多条件分页查询压测任务列表

**请求信息：**
```
POST /QueryLoadTestTasks
```

**请求体：**
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "strategyId": "strategy_001",
  "experimentTaskId": "exp_789012",
  "status": "RUNNING",
  "namespace": "default"
}
```

**请求参数说明：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| pageNum | Integer | 是 | 1 | 页码 |
| pageSize | Integer | 是 | 10 | 页大小 |
| strategyId | String | 否 | - | 压测策略ID |
| experimentTaskId | String | 否 | - | 演练任务ID |
| status | String | 否 | - | 任务状态 |
| namespace | String | 否 | default | 命名空间 |

**响应数据：**
```json
{
  "success": true,
  "result": {
    "page": 1,
    "pageSize": 10,
    "pages": 5,
    "total": 50,
    "data": [
      {
        "taskId": "task_123456",
        "strategyId": "strategy_001",
        "experimentTaskId": "exp_789012",
        "status": "RUNNING",
        "statusDescription": "运行中",
        "startTime": "2025-08-22T10:30:00+08:00",
        "createdAt": "2025-08-22T10:29:00+08:00"
      }
    ]
  }
}
```

#### 1.4 查询所有压测任务

**接口描述：** 查询用户在指定命名空间下的所有压测任务（不分页）

**请求信息：**
```
GET /ListAllLoadTestTasks
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| namespace | String | 否 | default | 命名空间 |

**响应数据：**
```json
{
  "success": true,
  "result": [
    {
      "taskId": "task_123456",
      "strategyId": "strategy_001",
      "status": "RUNNING",
      "startTime": "2025-08-22T10:30:00+08:00"
    }
  ]
}
```

### 2. 任务控制操作

#### 2.1 停止压测任务

**接口描述：** 停止正在运行的压测任务

**请求信息：**
```
POST /StopLoadTestTask
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| taskId | String | 是 | - | 压测任务ID |
| namespace | String | 否 | default | 命名空间 |

**响应数据：**
```json
{
  "success": true,
  "result": true
}
```

**使用示例：**
```bash
curl -X POST "http://localhost:7001/StopLoadTestTask?taskId=task_123456"
```

#### 2.2 同步压测任务状态

**接口描述：** 从压测引擎同步最新的任务状态到本地数据库

**请求信息：**
```
POST /SyncLoadTestTaskStatus
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| taskId | String | 是 | - | 压测任务ID |
| namespace | String | 否 | default | 命名空间 |

**响应数据：** 返回同步后的 LoadTestTaskVO 对象

### 3. 结果和数据获取

#### 3.1 获取压测结果

**接口描述：** 获取压测任务的执行结果，支持通过任务ID或演练ID查询

**请求信息：**
```
GET /GetLoadTestResults
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| taskId | String | 否 | - | 压测任务ID |
| experimentId | String | 否 | - | 演练ID |
| namespace | String | 否 | default | 命名空间 |

**注意：** taskId 和 experimentId 必须提供其中一个

**响应数据：**
```json
{
  "success": true,
  "result": {
    "executionId": "exec_345678",
    "status": "COMPLETED",
    "resultPath": "/results/task_123456",
    "reportPath": "/reports/task_123456",
    "logPath": "/logs/task_123456",
    "resultUrl": "http://engine:8008/results/exec_345678",
    "reportUrl": "http://engine:8008/reports/exec_345678"
  }
}
```

**使用示例：**
```bash
# 通过任务ID获取
curl "http://localhost:7001/GetLoadTestResults?taskId=task_123456"

# 通过演练ID获取
curl "http://localhost:7001/GetLoadTestResults?experimentId=exp_789012"
```

#### 3.2 获取压测事件流水

**接口描述：** 获取压测任务的事件流水日志，支持通过任务ID或演练ID查询

**请求信息：**
```
GET /GetLoadTestEvents
```

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| taskId | String | 否 | - | 压测任务ID |
| experimentId | String | 否 | - | 演练ID |
| tail | Integer | 否 | 100 | 返回最近N条记录 |
| namespace | String | 否 | default | 命名空间 |

**注意：** taskId 和 experimentId 必须提供其中一个

**响应数据：**
```json
{
  "success": true,
  "result": {
    "executionId": "exec_345678",
    "events": [
      "2025-08-22T10:30:00 [INFO] 压测任务开始执行",
      "2025-08-22T10:30:05 [INFO] 连接目标服务器成功",
      "2025-08-22T10:30:10 [INFO] 开始发送请求",
      "2025-08-22T10:35:00 [INFO] 压测执行完成"
    ]
  }
}
```

**使用示例：**
```bash
# 通过任务ID获取最近50条事件
curl "http://localhost:7001/GetLoadTestEvents?taskId=task_123456&tail=50"

# 通过演练ID获取事件
curl "http://localhost:7001/GetLoadTestEvents?experimentId=exp_789012"
```

#### 3.3 获取性能指标时序数据

**接口描述：** 获取压测执行期间的性能指标时序数据，用于性能监控和分析

**请求信息：**
```
GET /api/metrics/performance/{executionId}/series
```

**路径参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| executionId | String | 是 | 压测引擎执行ID |

**请求参数：**
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| namespace | String | 否 | default | 命名空间 |

**响应数据：**
```json
{
  "success": true,
  "result": {
    "bucketSizeMs": 5000,
    "avgLatency": [
      [1692691800000, 120.5],
      [1692691805000, 125.3],
      [1692691810000, 118.7]
    ],
    "minLatency": [
      [1692691800000, 45.2],
      [1692691805000, 48.1],
      [1692691810000, 42.8]
    ],
    "maxLatency": [
      [1692691800000, 350.8],
      [1692691805000, 380.2],
      [1692691810000, 320.5]
    ],
    "p90": [
      [1692691800000, 200.1],
      [1692691805000, 210.5],
      [1692691810000, 195.3]
    ],
    "p95": [
      [1692691800000, 250.8],
      [1692691805000, 260.2],
      [1692691810000, 245.1]
    ],
    "p99": [
      [1692691800000, 320.5],
      [1692691805000, 340.8],
      [1692691810000, 310.2]
    ],
    "successRate": [
      [1692691800000, 99.5],
      [1692691805000, 99.2],
      [1692691810000, 99.8]
    ],
    "throughputReceived": [
      [1692691800000, 1024000.0],
      [1692691805000, 1048576.0],
      [1692691810000, 1073741.0]
    ],
    "throughputSent": [
      [1692691800000, 512000.0],
      [1692691805000, 524288.0],
      [1692691810000, 536870.0]
    ]
  }
}
```

**数据说明：**
- `bucketSizeMs`: 时间桶大小（毫秒），表示数据聚合的时间间隔
- 所有时序数据格式为 `[timestamp, value]`，其中 timestamp 为 Unix 时间戳（毫秒）
- `avgLatency/minLatency/maxLatency/p90/p95/p99`: 延迟指标（毫秒）
- `successRate`: 成功率（百分比）
- `throughputReceived/throughputSent`: 吞吐量（字节/秒）

**使用示例：**
```bash
curl "http://localhost:7001/api/metrics/performance/exec_345678/series"
```

## 数据模型

### LoadTestTaskVO

压测任务视图对象，包含任务的完整信息：

```json
{
  "taskId": "压测任务ID",
  "strategyId": "压测策略ID",
  "experimentTaskId": "演练任务ID",
  "executionId": "压测引擎执行ID",
  "status": "任务状态",
  "statusDescription": "状态描述",
  "startTime": "开始时间（ISO 8601格式）",
  "endTime": "结束时间（ISO 8601格式）",
  "errorMessage": "错误信息",
  "resultPath": "结果文件路径",
  "reportPath": "报告路径",
  "logPath": "日志路径",
  "createdAt": "创建时间（ISO 8601格式）",
  "updatedAt": "更新时间（ISO 8601格式）"
}
```

**任务状态说明：**
- `PENDING`: 等待中
- `RUNNING`: 运行中
- `COMPLETED`: 已完成
- `FAILED`: 失败
- `STOPPED`: 已停止
- `TIMEOUT`: 超时

### LoadTestTaskQueryRequest

分页查询请求对象：

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "strategyId": "压测策略ID（可选）",
  "experimentTaskId": "演练任务ID（可选）",
  "status": "任务状态（可选）",
  "namespace": "命名空间（可选）"
}
```

### PerformanceTimeseries

性能指标时序数据对象，所有时序数据都是 `[timestamp, value]` 格式的数组：

```json
{
  "bucketSizeMs": "时间桶大小（毫秒）",
  "avgLatency": "平均延迟时序数据",
  "minLatency": "最小延迟时序数据",
  "maxLatency": "最大延迟时序数据",
  "p90": "P90延迟时序数据",
  "p95": "P95延迟时序数据",
  "p99": "P99延迟时序数据",
  "successRate": "成功率时序数据",
  "throughputReceived": "接收吞吐量时序数据",
  "throughputSent": "发送吞吐量时序数据"
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未登录 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 使用注意事项

1. **认证要求**: 所有接口都需要用户登录，请确保请求头中包含有效的认证信息
2. **参数灵活性**: 部分接口支持多种参数组合，如通过 taskId 或 experimentId 查询
3. **命名空间**: 默认使用 "default" 命名空间，多租户环境下需要指定正确的命名空间
4. **时间格式**: 所有时间字段都使用 ISO 8601 格式，时区为 Asia/Shanghai
5. **分页查询**: 建议合理设置页大小，避免一次查询过多数据影响性能
6. **性能监控**: 性能指标时序接口适用于实时监控和历史数据分析

## 集成示例

### JavaScript 示例

```javascript
// 查询压测任务
async function getLoadTestTask(taskId) {
  const response = await fetch(`/GetLoadTestTask?taskId=${taskId}`);
  const data = await response.json();
  return data.result;
}

// 获取性能指标
async function getPerformanceMetrics(executionId) {
  const response = await fetch(`/api/metrics/performance/${executionId}/series`);
  const data = await response.json();
  return data.result;
}

// 停止压测任务
async function stopLoadTest(taskId) {
  const response = await fetch('/StopLoadTestTask', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `taskId=${taskId}`
  });
  const data = await response.json();
  return data.result;
}
```

### Python 示例

```python
import requests

class LoadTestClient:
    def __init__(self, base_url):
        self.base_url = base_url

    def get_task(self, task_id):
        """查询压测任务"""
        response = requests.get(f"{self.base_url}/GetLoadTestTask",
                              params={"taskId": task_id})
        return response.json()["result"]

    def get_performance_metrics(self, execution_id):
        """获取性能指标"""
        response = requests.get(f"{self.base_url}/api/metrics/performance/{execution_id}/series")
        return response.json()["result"]

    def stop_task(self, task_id):
        """停止压测任务"""
        response = requests.post(f"{self.base_url}/StopLoadTestTask",
                               data={"taskId": task_id})
        return response.json()["result"]

# 使用示例
client = LoadTestClient("http://localhost:7001")
task = client.get_task("task_123456")
metrics = client.get_performance_metrics("exec_345678")
```

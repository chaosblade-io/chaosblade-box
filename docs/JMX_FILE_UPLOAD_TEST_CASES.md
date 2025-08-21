# JMX文件上传功能 - 测试用例

## 功能概述

JMX文件上传功能允许用户将JMeter测试计划文件（.jmx）上传到指定的压测引擎服务器，并返回文件的访问URL，用于后续的压测定义创建和更新。

## 接口信息

**接口URL**: `POST /UploadJmxFile`
**Content-Type**: `multipart/form-data`

### 请求参数

| 参数名 | 类型 | 必需 | 说明 |
|--------|------|------|------|
| file | File | 是 | JMX文件，最大500MB |
| endpoint | String | 是 | 压测引擎端点，如 `http://1.94.151.57:8008` |
| namespace | String | 否 | 命名空间，默认为 `default` |

### 响应格式

```json
{
  "success": true,
  "result": {
    "fileName": "test_plan_20231201_123456_abc123.jmx",
    "originalFileName": "test_plan.jmx",
    "fileType": "jmx",
    "fileSize": 1024,
    "uploadPath": "/data/loadtest/files/test_plan_20231201_123456_abc123.jmx",
    "accessUrl": "http://1.94.151.57:8008/files/test_plan_20231201_123456_abc123.jmx",
    "uploadTime": 1703145600000,
    "uploadDate": "2023-12-01T10:00:00+08:00"
  }
}
```

## 测试用例

### 测试用例 1: 正常上传JMX文件

**测试目标**: 验证正常的JMX文件上传功能

**前置条件**:
- 压测引擎服务正常运行
- 准备有效的.jmx文件

**测试步骤**:
1. 准备一个有效的JMX文件（小于500MB）
2. 设置有效的压测引擎端点
3. 发送上传请求

**Postman配置**:
```
POST http://localhost:7001/UploadJmxFile
Content-Type: multipart/form-data

Form Data:
- file: [选择.jmx文件]
- endpoint: http://1.94.151.57:8008
- namespace: default
```

**预期结果**:
- HTTP状态码: 200
- success: true
- result包含完整的文件信息
- accessUrl可以访问

### 测试用例 2: 文件格式验证

**测试目标**: 验证只接受.jmx格式文件

**测试步骤**:
1. 上传非.jmx格式文件（如.txt、.xml等）
2. 观察响应结果

**预期结果**:
```json
{
  "success": false,
  "message": "文件格式必须为.jmx"
}
```

### 测试用例 3: 文件大小限制验证

**测试目标**: 验证文件大小不超过500MB的限制

**测试步骤**:
1. 准备一个超过500MB的.jmx文件
2. 尝试上传

**预期结果**:
```json
{
  "success": false,
  "message": "文件大小不能超过500MB"
}
```

### 测试用例 4: 空文件验证

**测试目标**: 验证不接受空文件

**测试步骤**:
1. 不选择文件或选择空文件
2. 发送请求

**预期结果**:
```json
{
  "success": false,
  "message": "文件不能为空"
}
```

### 测试用例 5: 端点格式验证

**测试目标**: 验证压测引擎端点格式

**测试数据**:
- 无效端点: `invalid-url`
- 无效协议: `ftp://example.com`
- 空端点: ``

**预期结果**:
```json
{
  "success": false,
  "message": "压测引擎端点格式无效"
}
```

### 测试用例 6: 压测引擎不可达

**测试目标**: 验证压测引擎服务不可达时的处理

**测试步骤**:
1. 使用不存在的端点地址
2. 上传文件

**预期结果**:
```json
{
  "success": false,
  "message": "文件上传失败: 网络请求失败: ..."
}
```

### 测试用例 7: 压测引擎返回错误

**测试目标**: 验证压测引擎返回错误时的处理

**测试步骤**:
1. 使用返回错误的压测引擎端点
2. 上传文件

**预期结果**:
```json
{
  "success": false,
  "message": "文件上传失败: HTTP 500: ..."
}
```

## 集成测试

### 测试用例 8: 与压测定义创建集成

**测试目标**: 验证上传的文件可以用于创建压测定义

**测试步骤**:
1. 上传JMX文件，获取accessUrl
2. 使用返回的accessUrl创建压测定义
3. 验证压测定义创建成功

**创建压测定义请求**:
```json
{
  "name": "基于上传文件的压测定义",
  "engineType": "JMETER",
  "endpoint": "http://1.94.151.57:8008",
  "entry": "JMX",
  "contentRef": "http://1.94.151.57:8008/files/test_plan_20231201_123456_abc123.jmx",
  "namespace": "default"
}
```

### 测试用例 9: 与压测定义更新集成

**测试目标**: 验证可以更新压测定义的JMX文件

**测试步骤**:
1. 创建一个压测定义
2. 上传新的JMX文件
3. 更新压测定义的contentRef
4. 验证更新成功

## 性能测试

### 测试用例 10: 大文件上传性能

**测试目标**: 验证大文件上传的性能和稳定性

**测试数据**:
- 文件大小: 100MB, 200MB, 400MB
- 并发上传: 1个, 3个, 5个

**性能指标**:
- 上传时间 < 5分钟（400MB文件）
- 内存使用稳定
- 无内存泄漏

### 测试用例 11: 并发上传测试

**测试目标**: 验证并发上传的处理能力

**测试步骤**:
1. 同时发起多个文件上传请求
2. 观察系统响应和资源使用

**预期结果**:
- 所有请求都能正确处理
- 系统资源使用合理
- 无死锁或阻塞

## 安全测试

### 测试用例 12: 恶意文件上传

**测试目标**: 验证对恶意文件的防护

**测试数据**:
- 包含恶意脚本的文件
- 超大文件名
- 特殊字符文件名

**预期结果**:
- 恶意文件被拒绝
- 系统不受影响
- 适当的错误提示

### 测试用例 13: 权限验证

**测试目标**: 验证用户权限控制

**测试步骤**:
1. 未登录用户尝试上传
2. 不同命名空间用户的隔离

**预期结果**:
- 未登录用户被拒绝
- 命名空间隔离正确

## 错误恢复测试

### 测试用例 14: 网络中断恢复

**测试目标**: 验证网络中断时的处理

**测试步骤**:
1. 开始文件上传
2. 模拟网络中断
3. 观察系统行为

**预期结果**:
- 适当的错误提示
- 资源正确释放
- 系统状态正常

### 测试用例 15: 服务重启恢复

**测试目标**: 验证服务重启后的状态

**测试步骤**:
1. 上传文件过程中重启服务
2. 重启后重新上传
3. 验证功能正常

## 自动化测试脚本

### cURL测试脚本

```bash
#!/bin/bash

# 测试JMX文件上传功能
BASE_URL="http://localhost:7001"
ENDPOINT="http://1.94.151.57:8008"
TEST_FILE="test_plan.jmx"

echo "=== JMX文件上传功能测试 ==="

# 测试1: 正常上传
echo "测试1: 正常上传JMX文件"
curl -X POST "$BASE_URL/UploadJmxFile" \
  -F "file=@$TEST_FILE" \
  -F "endpoint=$ENDPOINT" \
  -F "namespace=default"

echo -e "\n"

# 测试2: 文件格式错误
echo "测试2: 上传非JMX文件"
curl -X POST "$BASE_URL/UploadJmxFile" \
  -F "file=@test.txt" \
  -F "endpoint=$ENDPOINT" \
  -F "namespace=default"

echo -e "\n"

# 测试3: 端点格式错误
echo "测试3: 无效端点格式"
curl -X POST "$BASE_URL/UploadJmxFile" \
  -F "file=@$TEST_FILE" \
  -F "endpoint=invalid-url" \
  -F "namespace=default"

echo -e "\n"

echo "测试完成"
```

## 测试数据准备

### 创建测试用的JMX文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0" jmeter="5.4.1">
  <hashTree>
    <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="Test Plan" enabled="true">
      <stringProp name="TestPlan.comments"></stringProp>
      <boolProp name="TestPlan.functional_mode">false</boolProp>
      <boolProp name="TestPlan.tearDown_on_shutdown">true</boolProp>
      <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
      <elementProp name="TestPlan.arguments" elementType="Arguments" guiclass="ArgumentsPanel" testclass="Arguments" testname="User Defined Variables" enabled="true">
        <collectionProp name="Arguments.arguments"/>
      </elementProp>
      <stringProp name="TestPlan.user_define_classpath"></stringProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="Thread Group" enabled="true">
        <stringProp name="ThreadGroup.on_sample_error">continue</stringProp>
        <elementProp name="ThreadGroup.main_controller" elementType="LoopController" guiclass="LoopControlPanel" testclass="LoopController" testname="Loop Controller" enabled="true">
          <boolProp name="LoopController.continue_forever">false</boolProp>
          <stringProp name="LoopController.loops">1</stringProp>
        </elementProp>
        <stringProp name="ThreadGroup.num_threads">1</stringProp>
        <stringProp name="ThreadGroup.ramp_time">1</stringProp>
        <boolProp name="ThreadGroup.scheduler">false</boolProp>
        <stringProp name="ThreadGroup.duration"></stringProp>
        <stringProp name="ThreadGroup.delay"></stringProp>
        <boolProp name="ThreadGroup.same_user_on_next_iteration">true</boolProp>
      </ThreadGroup>
      <hashTree>
        <HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="HTTP Request" enabled="true">
          <elementProp name="HTTPsampler.Arguments" elementType="Arguments" guiclass="HTTPArgumentsPanel" testclass="Arguments" testname="User Defined Variables" enabled="true">
            <collectionProp name="Arguments.arguments"/>
          </elementProp>
          <stringProp name="HTTPSampler.domain">httpbin.org</stringProp>
          <stringProp name="HTTPSampler.port"></stringProp>
          <stringProp name="HTTPSampler.protocol">https</stringProp>
          <stringProp name="HTTPSampler.contentEncoding"></stringProp>
          <stringProp name="HTTPSampler.path">/get</stringProp>
          <stringProp name="HTTPSampler.method">GET</stringProp>
          <boolProp name="HTTPSampler.follow_redirects">true</boolProp>
          <boolProp name="HTTPSampler.auto_redirects">false</boolProp>
          <boolProp name="HTTPSampler.use_keepalive">true</boolProp>
          <boolProp name="HTTPSampler.DO_MULTIPART_POST">false</boolProp>
          <stringProp name="HTTPSampler.embedded_url_re"></stringProp>
          <stringProp name="HTTPSampler.connect_timeout"></stringProp>
          <stringProp name="HTTPSampler.response_timeout"></stringProp>
        </HTTPSamplerProxy>
        <hashTree/>
      </hashTree>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

将以上内容保存为 `test_plan.jmx` 文件用于测试。

## 注意事项

1. **文件大小**: 确保测试文件不超过500MB限制
2. **网络环境**: 确保能够访问压测引擎服务器
3. **权限**: 确保用户已登录并有相应权限
4. **清理**: 测试完成后清理上传的文件
5. **监控**: 观察系统资源使用情况

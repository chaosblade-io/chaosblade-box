# 压测定义管理功能

本文档描述了在 ChaosBlade-Box 项目中新增的压测定义管理功能。

## 功能概述

压测定义管理功能允许用户创建、管理和配置各种类型的压力测试定义，支持多种压测引擎（JMeter、K6、Locust 等）和不同的测试入口类型。

## 数据库设计

### 表结构

创建了新的数据库表 `t_chaos_load_test_definition`：

```sql
CREATE TABLE IF NOT EXISTS `t_chaos_load_test_definition` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `definition_id` varchar(64) NOT NULL COMMENT '压测定义ID',
  `name` varchar(256) NOT NULL COMMENT '压测定义名称',
  `engine_type` varchar(32) NOT NULL COMMENT '引擎类型：JMETER、K6、LOCUST、CUSTOM',
  `endpoint` varchar(512) NOT NULL COMMENT '目标端点',
  `entry` varchar(32) NOT NULL COMMENT '入口类型：JMX、SCRIPT、URL',
  `content_ref` varchar(1024) DEFAULT NULL COMMENT '文件URL引用',
  `url_case` longtext COMMENT 'URL型用例配置（JSON格式）',
  `created_by` varchar(64) NOT NULL COMMENT '创建者',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `namespace` varchar(64) NOT NULL DEFAULT 'default' COMMENT '命名空间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_definition_id` (`definition_id`),
  KEY `idx_user_namespace` (`user_id`, `namespace`),
  KEY `idx_name` (`name`),
  KEY `idx_engine_type` (`engine_type`),
  KEY `idx_created_by` (`created_by`),
  KEY `idx_gmt_create` (`gmt_create`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='压测定义表';
```

## 代码架构

### 1. DAO 层

- **实体类**: `LoadTestDefinitionDO` - 压测定义数据对象
- **Mapper**: `LoadTestDefinitionMapper` - MyBatis Plus 映射接口
- **Repository**: `LoadTestDefinitionRepository` - 数据访问层封装

### 2. Service 层

- **接口**: `LoadTestDefinitionService` - 业务逻辑接口
- **实现**: `LoadTestDefinitionServiceImpl` - 业务逻辑实现
- **模型类**:
  - `LoadTestDefinitionVO` - 视图对象
  - `LoadTestDefinitionCreateRequest` - 创建请求
  - `LoadTestDefinitionUpdateRequest` - 更新请求
  - `LoadTestDefinitionQueryRequest` - 查询请求

### 3. Controller 层

- **控制器**: `LoadTestDefinitionController` - REST API 控制器

## API 接口

### 1. 创建压测定义

**接口**: `POST /chaos/CreateLoadTestDefinition`

**请求参数**:
```json
{
  "name": "压测定义名称",
  "engineType": "JMETER",
  "endpoint": "http://example.com",
  "entry": "URL",
  "contentRef": "文件URL引用（可选）",
  "urlCase": {
    "method": "GET",
    "path": "/api/test",
    "headers": {}
  },
  "namespace": "default"
}
```

**响应**:
```json
{
  "success": true,
  "result": "ldef_xxxxxxxxxx"
}
```

### 2. 更新压测定义

**接口**: `POST /chaos/UpdateLoadTestDefinition`

**请求参数**:
```json
{
  "id": "ldef_xxxxxxxxxx",
  "name": "更新后的名称",
  "engineType": "K6",
  "endpoint": "http://updated-example.com",
  "entry": "SCRIPT",
  "namespace": "default"
}
```

### 3. 删除压测定义

**接口**: `POST /chaos/DeleteLoadTestDefinition`

**请求参数**:
```
id=ldef_xxxxxxxxxx&namespace=default
```

### 4. 查询压测定义详情

**接口**: `POST /chaos/GetLoadTestDefinition`

**请求参数**:
```
id=ldef_xxxxxxxxxx&namespace=default
```

**响应**:
```json
{
  "success": true,
  "result": {
    "id": "ldef_xxxxxxxxxx",
    "name": "压测定义名称",
    "engineType": "JMETER",
    "endpoint": "http://example.com",
    "entry": "URL",
    "contentRef": null,
    "urlCase": {},
    "createdBy": "用户名",
    "createdAt": "2023-12-01T10:00:00+08:00",
    "updatedAt": "2023-12-01T10:00:00+08:00"
  }
}
```

### 5. 分页查询压测定义

**接口**: `POST /chaos/QueryLoadTestDefinitions`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 20,
  "name": "搜索关键词（可选）",
  "engineType": "JMETER（可选）",
  "namespace": "default"
}
```

### 6. 查询所有压测定义

**接口**: `POST /chaos/ListAllLoadTestDefinitions`

**请求参数**:
```
namespace=default
```

## 支持的引擎类型

- **JMETER**: Apache JMeter 压测引擎
- **K6**: Grafana K6 压测引擎
- **LOCUST**: Locust 压测引擎
- **CUSTOM**: 自定义压测引擎

## 支持的入口类型

- **JMX**: JMeter 脚本文件
- **SCRIPT**: 脚本文件（如 K6 JavaScript、Locust Python）
- **URL**: 基于 URL 的简单压测配置

## 测试

项目包含了完整的单元测试和集成测试：

1. **运行测试脚本**:
   ```bash
   ./test_load_test_definition_api.sh
   ```

2. **编译项目**:
   ```bash
   mvn clean install -DskipTests
   ```

## 权限控制

- 所有操作都需要用户登录
- 用户只能操作自己命名空间下的压测定义
- 支持多租户隔离

## 扩展性

该设计支持未来扩展：

1. **新增引擎类型**: 只需在 `engine_type` 字段中添加新的枚举值
2. **新增入口类型**: 只需在 `entry` 字段中添加新的枚举值
3. **自定义配置**: 通过 `url_case` 字段存储 JSON 格式的自定义配置
4. **文件支持**: 通过 `content_ref` 字段引用外部文件

## 注意事项

1. 压测定义名称在同一用户的同一命名空间下必须唯一
2. 删除操作为逻辑删除，不会物理删除数据
3. 所有时间字段使用 Asia/Shanghai 时区
4. JSON 配置使用 FastJSON 进行序列化和反序列化
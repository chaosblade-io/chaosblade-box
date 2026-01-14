![logo](https://chaosblade.oss-cn-hangzhou.aliyuncs.com/doc/image/chaosblade-logo.png)

# ChaosBlade-Box：功能丰富的混沌工程平台

[English](./README.md) | 中文

![license](https://img.shields.io/github/license/chaosblade-io/chaosblade.svg)

> **注意**：关于弹性测试平台的更多信息，请查看 [main2](https://github.com/chaosblade-io/chaosblade-box/tree/main2) 分支。

## 简介

ChaosBlade-Box 是一个功能丰富的混沌工程平台，支持多种故障注入场景，当前支持的场景包括：

* [chaosblade-exec-os](https://github.com/chaosblade-io/chaosblade-exec-os)：基础资源实验场景实现
* [chaosblade-exec-docker](https://github.com/chaosblade-io/chaosblade-exec-docker)：Docker 容器实验场景实现，通过调用 Docker API 实现标准化
* [chaosblade-operator](https://github.com/chaosblade-io/chaosblade-operator)：Kubernetes 平台实验场景实现，混沌实验通过 Kubernetes 标准的 CRD 方式定义，使用 Kubernetes 资源操作方式（包括 kubectl、client-go 等）非常方便地创建、更新、删除实验场景，也可以使用上述 chaosblade cli 工具
* [chaosblade-exec-jvm](https://github.com/chaosblade-io/chaosblade-exec-jvm)：Java 应用实验场景实现，使用 Java Agent 技术动态挂载，无需任何接入，零成本使用，同时支持卸载，完全回收 Agent 创建的各种资源
* [chaosblade-exec-cplus](https://github.com/chaosblade-io/chaosblade-exec-cplus)：C++ 应用实验场景实现，使用 GDB 技术实现方法和代码行级别的实验场景注入
* [litmus-chaos](https://github.com/litmuschaos/litmus)：云原生混沌工程的工具集

## 快速开始

### 📖 部署文档

我们提供了详细的部署指南，支持主机环境和 Kubernetes 环境部署：

- **[中文部署文档](./DEPLOYMENT_zh.md)** - 详细的分步部署指南
- **[English Deployment Guide](./DEPLOYMENT_en.md)** - Detailed step-by-step deployment guide

部署文档包含：
- ✅ 系统要求和前置条件
- ✅ 主机环境部署（Docker、直接部署）
- ✅ Kubernetes 环境部署（Helm Chart）
- ✅ 配置说明和最佳实践
- ✅ 常见问题排查
- ✅ 升级和卸载指南

## 项目编译

进入克隆的项目根目录并执行编译：

### 构建项目

```bash
# 构建整个项目（包括前端和 Java 后端）
make build
```

### 构建 Docker 镜像

```bash
# 构建 Docker 镜像（可选参数：IMAGE_NAME, IMAGE_REGISTRY, VERSION）
make docker-build

# 示例：指定镜像仓库
make docker-build IMAGE_REGISTRY=registry.example.com/
```

### 构建 Helm 包

```bash
# 打包 Helm chart
make helm-package
```

### 清理构建产物

```bash
# 清理构建产物
make clean
```

### 查看所有可用命令

```bash
# 显示帮助信息，查看所有可用命令
make help
```

## 部署方式

### 方式一：主机环境部署

#### 快速开始

1. **获取程序包**：从 [RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases) 下载最新的 jar 包

2. **准备 MySQL**：可以使用本地数据库或云数据库，开发/测试环境可使用 Docker 部署

3. **配置环境变量**（推荐）：
```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword123"
```

4. **启动应用**：
```bash
java -jar chaosblade-box-1.1.0.jar \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}"
```

> **提示**：详细部署步骤请参考 [部署文档](./DEPLOYMENT_zh.md)

#### 访问应用

使用浏览器访问：http://127.0.0.1:7001

### 方式二：Kubernetes 环境部署

#### 快速开始

1. **获取 Helm Chart 包**：从 [RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases) 下载最新的 helm 包

2. **创建 Secret**（推荐）：
```bash
kubectl create secret generic chaosblade-secret \
  --from-literal=mysql-password="YourPassword" \
  -n chaosblade
```

3. **部署应用**：
```bash
helm install chaosblade-box chaosblade-box-1.1.0.tgz \
  --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) \
  --namespace chaosblade
```

> **提示**：详细部署步骤请参考 [部署文档](./DEPLOYMENT_zh.md)

## 参数说明

| 参数 | 说明 | 默认值 | 是否必需 |
|------|------|--------|---------|
| `spring.datasource.url` | MySQL 连接地址。如果使用 Helm 启动，无需指定 | - | Helm: 否<br>主机: 是 |
| `spring.datasource.username` | MySQL 用户名。如果使用 Helm 启动，无需指定 | root | Helm: 否<br>主机: 是 |
| `spring.datasource.password` | MySQL 密码 | - | **是** |
| `spring.data.redis.host` | Redis 主机地址 | localhost | 否 |
| `spring.data.redis.port` | Redis 端口 | 6379 | 否 |
| `spring.data.redis.password` | Redis 密码 | - | 否 |
| `chaos.cache.enable` | 是否启用缓存 | false | 否 |
| `chaos.function.sync.type` | 初始化混沌数据类型。首次启动可使用 `ALL` | ALL | 否 |
| `chaos.agent.version` | chaosblade-box-agent 版本 | 1.1.0 | 否 |
| `chaos.agent.repository` | chaosblade-box-agent 镜像仓库 | ghcr.io/chaosblade-io/chaosblade-box-agent | 否 |
| `chaos.agent.url` | chaosblade-box-agent 二进制包 URL | [OSS 链接] | 否 |
| `chaos.agent.helm` | chaosblade-box-agent Helm 包 URL | [OSS 链接] | 否 |
| `chaos.server.domain` | ChaosBlade Box 访问地址（用于 Agent 连接） | - | 主机部署必需 |

### 可用的 chaos.function.sync.type 值

- `ALL`：初始化所有混沌实验数据（首次部署推荐）
- `ChaosBlade`：仅初始化 ChaosBlade 相关数据
- `UserApp`：仅初始化用户应用数据
- `None`：不初始化数据（生产环境后续启动推荐）
- `LITMUS_CHAOS`：仅初始化 Litmus Chaos 数据

## 常见问题

### 1. 数据库连接失败
```bash
# 检查 MySQL 服务状态
docker ps | grep mysql

# 测试连接
mysql -h DATASOURCE_HOST -u DATASOURCE_USERNAME -p
```

### 2. Redis 连接失败
```bash
# 检查 Redis 服务状态
docker ps | grep redis

# 测试连接
redis-cli -h REDIS_HOST -p 6379 ping
```

### 3. Kubernetes Pod 无法启动
```bash
# 查看 Pod 日志
kubectl logs <pod-name> -n chaosblade

# 查看 Pod 详情
kubectl describe pod <pod-name> -n chaosblade

# 检查事件
kubectl get events -n chaosblade
```

### 4. 更多问题

请参考详细的 [部署文档](./DEPLOYMENT_zh.md) 获取完整的故障排查指南。

## 架构说明

### 组件架构

```
┌─────────────────────────────────────────────────────┐
│                  ChaosBlade Box                      │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────┐ │
│  │   Web UI     │  │   API Server │  │  Scheduler│ │
│  └──────────────┘  └──────────────┘  └───────────┘ │
└──────────────┬──────────────────────────────────────┘
               │
        ┌──────┴──────┐
        │             │
   ┌────▼────┐   ┌────▼────┐
   │  MySQL  │   │  Redis  │
   └─────────┘   └─────────┘
               │
        ┌──────┴──────────────────┐
        │                         │
   ┌────▼─────┐            ┌──────▼──────┐
   │  Agent   │            │   Agent     │
   │ (Host)   │            │ (K8s)       │
   └──────────┘            └─────────────┘
```

### 支持的实验场景

- **基础资源**：CPU、内存、磁盘、网络等
- **容器场景**：Docker 容器故障注入
- **Kubernetes**：Pod、Node、Service 等故障注入
- **应用层**：Java、C++ 应用级别的故障注入
- **云原生**：集成 Litmus Chaos 的云原生场景

## 功能特性

- ✅ 丰富的故障注入场景
- ✅ 友好的 Web UI 界面
- ✅ 支持主机和 Kubernetes 环境
- ✅ 实验编排和自动化
- ✅ 实时监控和报告
- ✅ 安全的权限管理
- ✅ 可扩展的插件架构
- ✅ 集成 Prometheus 监控
- ✅ 支持多租户隔离

## 升级指南

### 主机环境升级
```bash
# 1. 备份数据库
mysqldump -u root -p chaosblade > backup.sql

# 2. 停止旧版本
kill $(cat chaosblade-box.pid)

# 3. 启动新版本
java -jar chaosblade-box-1.1.0.jar ...
```

### Kubernetes 环境升级
```bash
# 使用 Helm 升级
helm upgrade chaosblade-box chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --namespace chaosblade
```

## 卸载

### 主机环境
```bash
# 停止应用
kill $(cat chaosblade-box.pid)

# 清理数据（可选）
docker stop mysql-8.0 redis
docker rm mysql-8.0 redis
```

### Kubernetes 环境
```bash
# 卸载 Helm Release
helm uninstall chaosblade-box -n chaosblade

# 删除命名空间
kubectl delete namespace chaosblade
```

## 反馈与问题

如需报告 Bug、提问或讨论，请提交 [GitHub Issues](https://github.com/chaosblade-io/chaosblade-box/issues)。

您也可以通过以下方式联系我们：
* **钉钉群**（推荐中文用户）：23177705
* **Slack 群组**：[chaosblade-io](https://join.slack.com/t/chaosblade-io/shared_invite/zt-f0d3r3f4-TDK13Wr3QRUrAhems28p1w)
* **Gitter 聊天室**：[chaosblade community](https://gitter.im/chaosblade-io/community)
* **邮箱**：chaosblade.io.01@gmail.com
* **Twitter**：[chaosblade.io](https://twitter.com/ChaosbladeI)

## 贡献

我们欢迎每一个贡献，即使只是标点符号。详见 [CONTRIBUTING](CONTRIBUTING.md)

## 企业登记

我们开源项目的初衷是降低混沌工程在企业中的实施门槛，因此我们非常重视项目在企业中的使用情况。欢迎大家在此 [ISSUE](https://github.com/chaosblade-io/chaosblade/issues/32) 登记。登记后，您将被邀请加入企业邮件组，讨论混沌工程在企业落地过程中遇到的问题并分享落地经验。

## 开源许可

ChaosBlade-Box 使用 Apache License 2.0 许可证。详见 [LICENSE](LICENSE) 文件。

## 相关项目

- [ChaosBlade](https://github.com/chaosblade-io/chaosblade) - 混沌工程实验执行工具
- [ChaosBlade Operator](https://github.com/chaosblade-io/chaosblade-operator) - Kubernetes 混沌工程 Operator
- [Litmus](https://github.com/litmuschaos/litmus) - 云原生混沌工程框架

---

**Star 我们的项目，获取最新更新！** ⭐

# ChaosBlade-Box 部署文档

[English](./DEPLOYMENT_en.md) | 中文

## 目录

- [概述](#概述)
- [系统要求](#系统要求)
- [部署方式一：主机环境部署](#部署方式一主机环境部署)
  - [步骤1：获取程序包](#步骤1获取程序包)
  - [步骤2：准备 MySQL](#步骤2准备-mysql)
  - [步骤3：配置数据库](#步骤3配置数据库)
  - [步骤4：启动应用](#步骤4启动应用)
  - [步骤5：验证部署](#步骤5验证部署)
- [部署方式二：Kubernetes 环境部署](#部署方式二kubernetes-环境部署)
  - [前置条件](#前置条件)
  - [步骤1：获取 Helm Chart 包](#步骤1获取-helm-chart-包)
  - [步骤2：配置部署参数](#步骤2配置部署参数)
  - [步骤3：部署应用](#步骤3部署应用)
  - [步骤4：验证部署](#步骤4验证部署)
  - [步骤5：访问应用](#步骤5访问应用)
- [配置说明](#配置说明)
- [高级功能：批量主机管理](#高级功能批量主机管理)
- [高级功能：部署 Redis 缓存](#高级功能部署-redis-缓存)
- [常见问题](#常见问题)
- [升级指南](#升级指南)
- [卸载](#卸载)

## 概述

ChaosBlade-Box 是一个功能丰富的混沌工程平台，支持多种场景的故障注入和演练。本文档提供了在主机环境和 Kubernetes 环境下部署 ChaosBlade-Box 的详细指南。

### 架构组件

- **ChaosBlade-Box**：主应用服务（Web UI + API）
- **MySQL**：数据库，存储实验配置和结果
- **Redis**：缓存服务（可选），用于提升性能
- **ChaosBlade-Agent**：部署在目标主机/集群的代理程序

## 系统要求

### 主机环境
- **操作系统**：Linux（CentOS 7+, Ubuntu 18.04+）
- **JDK**：1.8 或更高版本
- **内存**：建议 4GB 以上
- **磁盘**：至少 10GB 可用空间
- **网络**：能够访问目标主机（用于 Agent 通信）

### Kubernetes 环境
- **Kubernetes 版本**：1.16 或更高版本
- **Helm**：3.0 或更高版本
- **存储**：支持 hostPath 或其他存储类（用于数据持久化）
- **资源**：建议至少 2 核 CPU、4GB 内存

## 部署方式一：主机环境部署
### 步骤1：获取程序包
从[RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases)下载最新的 ChaosBlade Box jar包
也可源码编译，通过 `make help` 查看编译帮助

### 步骤2：准备 MySQL
可以使用本地数据库或云数据库，如果作为开发或测试，可以使用 Docker 部署 MySQL，操作如下：

```bash
# 创建数据目录
sudo mkdir -p /data/mysql

# 设置 MySQL 密码环境变量
export MYSQL_ROOT_PASSWORD="YourPassword123"

# 启动 MySQL 容器
docker run -d \
  --name mysql-8.0 \
  -p 3306:3306 \
  -v /data/mysql:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
  -e MYSQL_DATABASE=chaosblade \
  mysql:8.0.0 \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_unicode_ci \
  --default-time_zone='+8:00' \
  --lower_case_table_names=1

# 验证 MySQL 运行状态
docker ps | grep mysql
docker logs mysql-8.0
```

> **提示**：
> - 支持 MySQL 5.6+ 
> - ChaosBlade-Box 将自动创建 `chaosblade` 数据库并初始化表结构

### 步骤3：配置数据库

#### 3.1 设置环境变量

**推荐方式：使用环境变量配置敏感信息**

```bash
# 设置数据库相关环境变量
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword123"

# 可选：Redis 配置（如果使用 Redis）
export SPRING_REDIS_HOST="127.0.0.1"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"
export SPRING_REDIS_DATABASE="0"
```

#### 3.2 创建配置文件（可选）

如果需要使用配置文件，创建 `application-custom.yml`：

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.jdbc.Driver
```

> **注意**：
> - Redis 配置为可选项，如需启用 Redis 缓存，请参考 [高级功能：部署 Redis 缓存](#高级功能部署-redis-缓存) 章节
> - 建议使用环境变量配置密码，避免在配置文件中明文存储敏感信息

### 步骤4：启动应用

#### 4.1 基本启动命令

**确保已设置环境变量**（参考步骤3.1）

```bash
# 方式 A：使用配置文件启动（配置文件引用环境变量）
java -jar chaosblade-box-1.1.0.jar \
  --spring.config.additional-location=./application-custom.yml

# 方式 B：直接使用环境变量启动（推荐）
java -Duser.timezone=Asia/Shanghai \
  -jar chaosblade-box-1.1.0.jar \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}" \
  --spring.data.redis.host="${SPRING_REDIS_HOST:-127.0.0.1}" \
  --spring.data.redis.port="${SPRING_REDIS_PORT:-6379}" \
  --spring.data.redis.password="${SPRING_REDIS_PASSWORD:-}" \
  --chaos.cache.enable=true \
  --chaos.server.domain=192.168.1.100:7001
```

> **说明**：`${VAR:-default}` 语法表示如果环境变量未设置则使用默认值

#### 4.2 后台启动（生产环境推荐）

```bash
# 创建启动脚本
cat > start-chaosblade-box.sh << 'EOF'
#!/bin/bash

APP_NAME=chaosblade-box
JAR_FILE=chaosblade-box-1.1.0.jar
LOG_DIR=logs
PID_FILE=${APP_NAME}.pid

# 检查环境变量是否设置
if [ -z "${SPRING_DATASOURCE_PASSWORD}" ]; then
    echo "Error: SPRING_DATASOURCE_PASSWORD environment variable is not set"
    exit 1
fi

# 创建日志目录
mkdir -p ${LOG_DIR}

# 检查是否已运行
if [ -f ${PID_FILE} ]; then
    OLD_PID=$(cat ${PID_FILE})
    if ps -p ${OLD_PID} > /dev/null 2>&1; then
        echo "${APP_NAME} is already running with PID ${OLD_PID}"
        exit 1
    fi
fi

# 启动应用
nohup java -Duser.timezone=Asia/Shanghai \
  -Xms2g -Xmx2g \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=${LOG_DIR}/heap_dump.hprof \
  -jar ${JAR_FILE} \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}" \
  --spring.data.redis.host="${SPRING_REDIS_HOST:-127.0.0.1}" \
  --spring.data.redis.port="${SPRING_REDIS_PORT:-6379}" \
  --spring.data.redis.password="${SPRING_REDIS_PASSWORD:-}" \
  --chaos.cache.enable=true \
  --chaos.server.domain=$(hostname -I | awk '{print $1}'):7001 \
  > ${LOG_DIR}/chaosblade-box.log 2>&1 &

# 保存 PID
echo $! > ${PID_FILE}
echo "${APP_NAME} started with PID $(cat ${PID_FILE})"
EOF

# 添加执行权限
chmod +x start-chaosblade-box.sh

# 启动应用
./start-chaosblade-box.sh
```

#### 5.3 创建停止脚本

```bash
cat > stop-chaosblade-box.sh << 'EOF'
#!/bin/bash

APP_NAME=chaosblade-box
PID_FILE=${APP_NAME}.pid

if [ ! -f ${PID_FILE} ]; then
    echo "${APP_NAME} is not running"
    exit 1
fi

PID=$(cat ${PID_FILE})
if ps -p ${PID} > /dev/null 2>&1; then
    echo "Stopping ${APP_NAME} (PID: ${PID})..."
    kill ${PID}
    
    # 等待进程退出
    for i in {1..30}; do
        if ! ps -p ${PID} > /dev/null 2>&1; then
            echo "${APP_NAME} stopped"
            rm ${PID_FILE}
            exit 0
        fi
        sleep 1
    done
    
    # 强制终止
    echo "Force killing ${APP_NAME}..."
    kill -9 ${PID}
    rm ${PID_FILE}
else
    echo "${APP_NAME} is not running"
    rm ${PID_FILE}
fi
EOF

chmod +x stop-chaosblade-box.sh
```

#### 4.4 配置系统服务（可选）

```bash
# 创建环境变量文件
sudo cat > /etc/chaosblade-box/chaosblade-box.env << 'EOF'
SPRING_DATASOURCE_URL=jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=YourPassword123
SPRING_REDIS_HOST=127.0.0.1
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=YourRedisPassword
EOF

# 设置文件权限（仅 root 可读）
sudo chmod 600 /etc/chaosblade-box/chaosblade-box.env
sudo chown root:root /etc/chaosblade-box/chaosblade-box.env

# 创建 systemd 服务文件
sudo cat > /etc/systemd/system/chaosblade-box.service << 'EOF'
[Unit]
Description=ChaosBlade Box Service
After=network.target mysql.service redis.service

[Service]
Type=simple
User=chaosblade
WorkingDirectory=/opt/chaosblade-box
EnvironmentFile=/etc/chaosblade-box/chaosblade-box.env
ExecStart=/usr/bin/java -Duser.timezone=Asia/Shanghai \
  -Xms2g -Xmx2g \
  -jar /opt/chaosblade-box/chaosblade-box-1.1.0.jar \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}" \
  --spring.data.redis.host="${SPRING_REDIS_HOST:-127.0.0.1}" \
  --spring.data.redis.port="${SPRING_REDIS_PORT:-6379}" \
  --spring.data.redis.password="${SPRING_REDIS_PASSWORD:-}"
ExecStop=/bin/kill -15 $MAINPID
Restart=on-failure
RestartSec=10s

[Install]
WantedBy=multi-user.target
EOF

# 重新加载 systemd
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start chaosblade-box

# 设置开机自启
sudo systemctl enable chaosblade-box

# 查看状态
sudo systemctl status chaosblade-box
```

### 步骤5：验证部署

```bash
# 查看日志
tail -f logs/chaosblade-box.log

# 检查端口
netstat -tlnp | grep 7001
# 或
ss -tlnp | grep 7001

# 测试 API
curl http://localhost:7001/api/status

# 访问 Web UI
# 在浏览器打开：http://YOUR_SERVER_IP:7001
```

## 部署方式二：Kubernetes 环境部署

### 前置条件

1. **Kubernetes 集群**
   - 已配置好 kubectl 并能访问集群
   - 具有创建资源的权限

2. **Helm 安装**
```bash
# 检查 Helm 版本
helm version

# 如果未安装 Helm 3
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# 验证安装
helm version
```

### 步骤1：获取 Helm Chart 包
从[RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases)下载最新的 ChaosBlade Box helm 包
也可源码打包，通过 `make help` 查看编译帮助

### 步骤2：配置部署参数

#### 2.1 创建 Secret（推荐方式）

**使用 Kubernetes Secret 存储敏感信息**

```bash
# 创建命名空间
kubectl create namespace chaosblade

# 设置密码环境变量
export MYSQL_PASSWORD="YourMysqlPassword123"
export REDIS_PASSWORD="YourRedisPassword"

# 创建 Secret 存储数据库密码（使用环境变量）
kubectl create secret generic chaosblade-secret \
  --from-literal=mysql-password="${MYSQL_PASSWORD}" \
  --from-literal=redis-password="${REDIS_PASSWORD}" \
  -n chaosblade

# 验证 Secret
kubectl get secret chaosblade-secret -n chaosblade
```

#### 2.2 创建自定义配置文件

创建自定义配置文件 `custom-values.yaml`：

**方式 A：使用外部 MySQL 和 Redis（生产环境推荐）**

```yaml
# 禁用内置 MySQL
mysql:
  enable: false

# 禁用内置 Redis
redis:
  enable: false

# 配置外部数据库（从 Secret 读取密码）
spring:
  datasource:
    url: jdbc:mysql://external-mysql.example.com:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: chaosblade_user
    # 密码通过 Secret 注入，见下方说明
  redis:
    host: external-redis.example.com
    port: 6379
    # 密码通过 Secret 注入，见下方说明
    database: 0
```

**方式 B：使用内置 MySQL 和 Redis（开发/测试环境）**

```yaml
# 启用内置 MySQL
mysql:
  enable: true
  image:
    repository: mysql
    version: 8.0.0
    pullPolicy: IfNotPresent

# 启用内置 Redis
redis:
  enable: true
  image:
    repository: redis
    version: 7.0
    pullPolicy: IfNotPresent

# 数据库配置（从 Secret 读取密码）
spring:
  datasource:
    url: jdbc:mysql://chaosblade-box-mysql:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    # 密码通过 Secret 注入，见下方说明
  redis:
    host: chaosblade-box-redis
    port: 6379
    # 密码通过 Secret 注入，见下方说明
    database: 0

# 日志配置
logging:
  level:
    root: info
    box: info
```

> **重要提示**：
> - **推荐使用 Kubernetes Secret** 存储密码，避免在 values.yaml 中明文存储
> - 如果必须使用 values.yaml，请确保文件权限设置为 600，并不要提交到版本控制系统
> - 可以通过环境变量或 Secret 注入密码：
>
>   ```bash
>   # 方式1：通过 Helm 参数传递（从 Secret 读取）
>   helm install chaosblade-box ./chaosblade-box-1.1.0.tgz \
>     --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d)  -n chaosblade
>   
>   # 方式2：使用环境变量（在 Deployment 中配置）
>   # 需要在 Helm Chart 的 Deployment 模板中添加 envFrom 引用 Secret
>   ```

### 步骤3：部署应用

#### 3.1 创建命名空间

```bash
# 创建命名空间
kubectl create namespace chaosblade

# 验证命名空间
kubectl get namespace chaosblade
```

#### 3.2 部署应用

```bash
# 方式 A：使用自定义配置部署（推荐）
# 从 Secret 读取密码
helm install chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) \
  --set spring.redis.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.redis-password}' | base64 -d) \
  --namespace chaosblade

# 方式 B：使用命令行参数部署（从 Secret 读取）
helm install chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  --namespace chaosblade \
  --set mysql.enable=true \
  --set redis.enable=true \
  --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) \
  --set spring.redis.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.redis-password}' | base64 -d)

# 方式 C：使用环境变量（如果 Helm Chart 支持）
export MYSQL_PASSWORD=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d)
export REDIS_PASSWORD=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.redis-password}' | base64 -d)

helm install chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --set spring.datasource.password=${MYSQL_PASSWORD} \
  --set spring.redis.password=${REDIS_PASSWORD} \
  --namespace chaosblade

# 查看部署状态
helm list -n chaosblade
kubectl get pods -n chaosblade -w
```
> **说明**：如果不使用redis，则spring.redis.password可不用设置

#### 3.3 等待 Pod 就绪

```bash
# 持续监控 Pod 状态
watch kubectl get pods -n chaosblade

# 预期输出：
# NAME                                   READY   STATUS    RESTARTS   AGE
# chaosblade-box-xxxxxxxxxx-xxxxx       1/1     Running   0          2m
# chaosblade-box-mysql-xxxxxxxxx-xxxxx  1/1     Running   0          2m
# chaosblade-box-redis-xxxxxxxxx-xxxxx  1/1     Running   0          2m
```
> **说明**：如果使用内置的 mysql、redis，则会显示上述两个 POD，开启方式详见上述 2.2 中的方式B

### 步骤4：验证部署

```bash
# 查看所有资源
kubectl get all -n chaosblade

# 查看 Pod 日志
kubectl logs -f deployment/chaosblade-box -n chaosblade

# 查看 Service
kubectl get svc -n chaosblade

```

### 步骤5：访问应用

#### 5.1 通过 LoadBalancer（如果云环境支持）

```bash
# 查看 External IP
kubectl get svc chaosblade-box -n chaosblade

# 输出示例：
# NAME              TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)          AGE
# chaosblade-box    LoadBalancer   192.168.255.1    10.10.10.100     7001:32250/TCP   5m

# 访问地址
# http://10.10.10.100:7001
```

#### 5.2 通过 NodePort

修改 Service 类型为 NodePort：
```bash
kubectl patch svc chaosblade-box -n chaosblade -p '{"spec":{"type":"NodePort"}}'

# 查看 NodePort
kubectl get svc chaosblade-box -n chaosblade

# 输出示例：
# NAME              TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
# chaosblade-box    NodePort   192.168.255.1    <none>        7001:30123/TCP   5m

# 访问地址（使用任意节点 IP）
# http://NODE_IP:30123
```

#### 5.3 通过 Port Forward（开发调试）

```bash
# 端口转发到本地
kubectl port-forward -n chaosblade svc/chaosblade-box 7001:7001

# 访问地址
# http://localhost:7001
```

#### 5.4 通过 Ingress（生产环境推荐）

创建 Ingress 资源：
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: chaosblade-box-ingress
  namespace: chaosblade
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  ingressClassName: nginx
  rules:
  - host: chaosblade.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: chaosblade-box
            port:
              number: 7001
```

应用 Ingress：
```bash
kubectl apply -f ingress.yaml

# 配置 DNS 或 hosts
echo "INGRESS_IP chaosblade.example.com" | sudo tee -a /etc/hosts

# 访问地址
# http://chaosblade.example.com
```

### ChaosBlade 工具安装
详见 https://github.com/chaosblade-io/chaosblade/releases/latest

## 配置说明

### 环境变量配置（推荐）

**为了安全起见，强烈建议使用环境变量配置所有敏感信息（密码等）**：

```bash
# 数据库配置
export SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword"  # 必填

# Redis 配置（可选）
export SPRING_REDIS_HOST="redis-host"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"  # 可选
export SPRING_REDIS_DATABASE="0"

# MySQL Docker 容器密码
export MYSQL_ROOT_PASSWORD="YourPassword123"

# Redis Docker 容器密码（可选）
export REDIS_PASSWORD="YourRedisPassword"
```

**持久化环境变量**（推荐）：

```bash
# 创建环境变量配置文件
cat > ~/.chaosblade-box-env << 'EOF'
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword"
export SPRING_REDIS_HOST="127.0.0.1"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"
export SPRING_REDIS_DATABASE="0"
EOF

# 设置文件权限（仅所有者可读）
chmod 600 ~/.chaosblade-box-env

# 每次使用前加载环境变量
source ~/.chaosblade-box-env
```

### 核心配置参数

#### 数据库配置

配置文件示例（使用环境变量）：
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.jdbc.Driver
```

#### Redis 配置

配置文件示例（使用环境变量）：
```yaml
spring:
  data:
    redis:
      host: ${SPRING_REDIS_HOST}
      port: ${SPRING_REDIS_PORT}
      password: ${SPRING_REDIS_PASSWORD}  # 可选
      database: ${SPRING_REDIS_DATABASE}
      timeout: 2000

chaos:
  cache:
    enable: true  # 是否启用缓存
```

#### Agent 配置
```yaml
chaos:
  agent:
    version: 1.1.0  # Agent 版本
    repository: ghcr.io/chaosblade-io/chaosblade-box-agent  # Agent 镜像仓库
    url: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosagent-1.1.0-linux_amd64.tar.gz
    url_arm64: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosagent-1.1.0-linux_arm64.tar.gz
    helm: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosblade-box-agent-1.1.0-helm_amd64.tgz
    helm_arm64: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosblade-box-agent-1.1.0-helm_arm64.tgz
  function:
    sync:
      type: ALL  # 数据同步类型：ALL, ChaosBlade, UserApp, None, LITMUS_CHAOS
  server:
    domain: ''  # ChaosBlade Box 访问地址，如：192.168.1.100:7001
```

#### 功能配置
```yaml
chaos:
  function:
    sync:
      type: ALL  # 首次启动建议使用 ALL，后续使用 None
      # ALL: 初始化所有混沌实验数据
      # ChaosBlade: 只初始化 ChaosBlade 相关数据
      # UserApp: 只初始化用户应用数据
      # None: 不初始化数据
      # LITMUS_CHAOS: 只初始化 Litmus Chaos 数据
  prometheus:
    api: http://prometheus-server:9090  # Prometheus API 地址（可选）
```

#### 日志配置
```yaml
logging:
  level:
    root: info  # 全局日志级别
    box: info   # ChaosBlade Box 日志级别
    # 可选值：trace, debug, info, warn, error
```

### 环境变量（主机部署）

也可以通过环境变量配置：
```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword123"
export SPRING_REDIS_HOST="127.0.0.1"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"
export SPRING_REDIS_DATABASE="0"

java -jar chaosblade-box-1.1.0.jar
```

## 高级功能：批量主机管理

> **适用场景**：当需要在大量主机上批量部署和管理 ChaosBlade Agent 时，可以使用 Ansible 实现自动化。
> 
> **不需要此功能的场景**：
> - Kubernetes 环境部署（Agent 通过 DaemonSet 自动部署）
> - 单台或少量主机（可手动部署 Agent）
> - 已有其他自动化部署工具

### 什么是批量主机管理？

ChaosBlade-Box 提供了基于 Ansible 的批量主机管理功能，可以：

- 🚀 批量部署 Agent 到多台主机
- 🔄 统一管理主机配置
- 📦 自动化安装/卸载 Agent
- 🔐 自动配置 SSH 密钥认证
- 📊 批量测试主机连接

### 前置条件

#### 1. 安装 Ansible

```bash
# 检查是否已安装
ansible --version

# 安装 Ansible（CentOS/RHEL）
sudo yum install ansible -y

# 安装 Ansible（Ubuntu/Debian）
sudo apt-get update
sudo apt-get install ansible -y

# 验证安装
ansible --version
```

#### 2. 安装 Expect

Expect 用于自动化 SSH 密钥分发：

```bash
# 检查是否已安装
expect -v

# 安装 Expect（CentOS/RHEL）
sudo yum install expect -y

# 安装 Expect（Ubuntu/Debian）
sudo apt-get install expect -y

# 验证安装
expect -v
```

#### 3. 配置 SSH 密钥

```bash
# 检查现有密钥
ls ~/.ssh

# 如需重新生成，先备份旧密钥
mkdir -p ~/.ssh/backup
mv ~/.ssh/id_rsa* ~/.ssh/backup/ 2>/dev/null || true

# 生成新的 SSH 密钥对（无密码）
ssh-keygen -t rsa -b 4096 -C "chaosblade-box" -f ~/.ssh/id_rsa -N ""

# 验证密钥生成
ls -la ~/.ssh/id_rsa*
```

#### 4. 准备 SSH 密钥分发脚本

下载 [sshKey.sh](https://github.com/chaosblade-io/chaosblade-box/blob/main/ssh/sshKey.sh) 并放在与 chaosblade-box jar 包同一目录：

```bash
# 下载脚本
wget https://raw.githubusercontent.com/chaosblade-io/chaosblade-box/main/ssh/sshKey.sh

# 添加执行权限
chmod +x sshKey.sh

# 查看脚本
cat sshKey.sh
```

### 使用 Ansible 批量部署 Agent

#### 1. 配置主机清单

编辑 `/etc/ansible/hosts` 文件，添加目标主机：

**推荐方式：使用 SSH 密钥认证（更安全）**

```ini
# 编辑主机清单
sudo vim /etc/ansible/hosts

# 添加主机信息（使用密钥认证）
[chaosblade_agents]
192.168.1.101 ansible_user=root
192.168.1.102 ansible_user=root
192.168.1.103 ansible_user=root
```

**备选方式：使用密码认证（不推荐，仅用于测试）**

如果必须使用密码，建议通过环境变量或 Ansible Vault 加密：

```bash
# 方式1：使用环境变量（在 ansible.cfg 中配置）
export ANSIBLE_SSH_PASS="YourPassword"

# 方式2：使用 Ansible Vault 加密密码文件（推荐）
# 创建加密的密码文件
ansible-vault create vars/passwords.yml
# 在文件中定义：
# ssh_pass: YourPassword
```

然后在 hosts 文件中引用：
```ini
[chaosblade_agents]
192.168.1.101 ansible_user=root ansible_ssh_pass={{ ssh_pass }}
192.168.1.102 ansible_user=root ansible_ssh_pass={{ ssh_pass }}
192.168.1.103 ansible_user=root ansible_ssh_pass={{ ssh_pass }}
```

#### 2. 分发 SSH 密钥

使用 `sshKey.sh` 脚本自动分发密钥：

```bash
# 执行脚本分发密钥到所有主机
./sshKey.sh

# 或手动分发到指定主机
ssh-copy-id root@192.168.1.101
ssh-copy-id root@192.168.1.102
ssh-copy-id root@192.168.1.103
```

#### 3. 测试连接

```bash
# 测试所有主机连接
ansible chaosblade_agents -m ping

# 预期输出（成功）
192.168.1.101 | SUCCESS => {
    "changed": false,
    "ping": "pong"
}
192.168.1.102 | SUCCESS => {
    "changed": false,
    "ping": "pong"
}
...
```

#### 4. 通过 Web UI 批量部署

1. **登录 ChaosBlade-Box Web UI**
   - 访问 http://YOUR_BOX_IP:7001

2. **添加主机**
   - 进入 "主机管理" 页面
   - 点击 "添加主机"
   - 输入主机信息（IP、端口、用户名等）

3. **批量部署 Agent**
   - 选择要部署的主机
   - 点击 "批量部署 Agent"
   - 系统会自动通过 Ansible 完成部署

4. **验证部署**
   - 查看主机状态
   - 确认 Agent 连接正常

### Ansible 命令行操作

#### 批量执行命令

```bash
# 在所有主机上执行命令
ansible chaosblade_agents -m shell -a "uptime"

# 检查主机系统信息
ansible chaosblade_agents -m setup

# 批量安装软件包
ansible chaosblade_agents -m yum -a "name=wget state=present"
```

#### 批量文件分发

```bash
# 分发文件到所有主机
ansible chaosblade_agents -m copy -a "src=/path/to/file dest=/tmp/"

# 分发 Agent 安装包
ansible chaosblade_agents -m copy -a "src=./chaosagent.tar.gz dest=/opt/"
```

#### 批量管理 Agent

```bash
# 批量启动 Agent
ansible chaosblade_agents -m shell -a "systemctl start chaosagent"

# 批量查看 Agent 状态
ansible chaosblade_agents -m shell -a "systemctl status chaosagent"

# 批量停止 Agent
ansible chaosblade_agents -m shell -a "systemctl stop chaosagent"
```

### 故障排查

#### 1. Ansible 连接失败

```bash
# 检查主机连通性
ping 192.168.1.101

# 测试 SSH 连接
ssh root@192.168.1.101

# 查看 Ansible 详细日志
ansible chaosblade_agents -m ping -vvv

# 检查 SSH 密钥
ls -la ~/.ssh/
cat ~/.ssh/id_rsa.pub
```

#### 2. SSH 密钥认证失败

```bash
# 手动复制密钥到目标主机
ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.1.101

# 测试密钥登录
ssh -i ~/.ssh/id_rsa root@192.168.1.101

# 检查目标主机 authorized_keys
ssh root@192.168.1.101 "cat ~/.ssh/authorized_keys"
```

#### 3. Expect 脚本执行失败

```bash
# 检查 Expect 安装
which expect

# 手动执行脚本查看错误
./sshKey.sh

# 检查脚本权限
ls -la sshKey.sh
chmod +x sshKey.sh
```

### 最佳实践

1. **使用密钥认证而非密码**
   - 更安全
   - 避免密码明文存储

2. **配置 Ansible 超时时间**
```ini
# /etc/ansible/ansible.cfg
[defaults]
timeout = 30
```

3. **分组管理主机**
```ini
# /etc/ansible/hosts
[production]
prod-host-1
prod-host-2

[testing]
test-host-1
test-host-2
```

4. **使用 Ansible Vault 加密敏感信息**
```bash
# 加密密码文件
ansible-vault encrypt vars/passwords.yml

# 使用加密文件
ansible-playbook -i hosts playbook.yml --ask-vault-pass
```

5. **定期更新主机清单**
   - 删除已下线的主机
   - 添加新部署的主机
   - 保持清单文件最新

### 参考资源

- [Ansible 官方文档](https://docs.ansible.com/)
- [sshKey.sh 脚本源码](https://github.com/chaosblade-io/chaosblade-box/blob/main/ssh/sshKey.sh)

## 高级功能：部署 Redis 缓存

> **适用场景**：Redis 缓存可以显著提升 ChaosBlade-Box 的性能，但不是必需的。
> 
> **不需要 Redis 的场景**：
> - 小规模部署（实验数量少、用户少）
> - 测试和开发环境
> - 资源受限的环境

### 部署 Redis

#### 方式 A：使用 Docker 部署（推荐）

```bash
# 创建数据目录
sudo mkdir -p /data/redis

# 启动 Redis 容器（无密码）
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server --appendonly yes

# 设置 Redis 密码环境变量
export REDIS_PASSWORD="YourRedisPassword"

# 启动 Redis 容器（带密码，生产环境推荐）
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server --appendonly yes --requirepass ${REDIS_PASSWORD}

# 验证 Redis 运行状态
docker ps | grep redis

# 测试连接
redis-cli ping  # 应返回 PONG

# 如果设置了密码
redis-cli -a ${REDIS_PASSWORD} ping
```

#### 方式 B：使用已有 Redis 或云 Redis

如果您已有 Redis 实例（本地安装、阿里云 Redis 等），可以直接使用。只需准备好以下信息：
- Redis 地址和端口
- Redis 密码（如果有）
- 数据库编号（默认为 0）

### 配置 ChaosBlade-Box 使用 Redis

#### 主机环境配置

编辑 `application-custom.yml` 或在启动命令中添加以下配置（使用环境变量）：

```yaml
spring:
  data:
    redis:
      host: ${SPRING_REDIS_HOST:127.0.0.1}  # Redis 地址
      port: ${SPRING_REDIS_PORT:6379}
      password: ${SPRING_REDIS_PASSWORD:}  # 如果设置了密码
      database: ${SPRING_REDIS_DATABASE:0}
      timeout: 2000

chaos:
  cache:
    enable: true  # 启用缓存
```

或者在启动命令中添加参数（使用环境变量）：

```bash
# 确保已设置环境变量
export SPRING_DATASOURCE_URL="jdbc:mysql://..."
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword"
export SPRING_REDIS_HOST="127.0.0.1"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"

java -jar chaosblade-box-1.1.0.jar \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}" \
  --spring.data.redis.host="${SPRING_REDIS_HOST}" \
  --spring.data.redis.port="${SPRING_REDIS_PORT}" \
  --spring.data.redis.password="${SPRING_REDIS_PASSWORD}" \
  --chaos.cache.enable=true
```

#### Kubernetes 环境配置

使用内置 Redis（在 `values.yaml` 中）：

```yaml
redis:
  enable: true  # 启用内置 Redis
  image:
    repository: redis
    version: 7.0
    pullPolicy: IfNotPresent

spring:
  redis:
    host: chaosblade-box-redis  # 使用内置 Redis 的服务名
    port: 6379
    password: ${SPRING_REDIS_PASSWORD}  # 使用环境变量，可选
    database: 0
```

使用外部 Redis（推荐使用 Secret 存储密码）：

```yaml
redis:
  enable: false  # 禁用内置 Redis

spring:
  redis:
    host: external-redis.example.com  # 外部 Redis 地址
    port: 6379
    password: ${SPRING_REDIS_PASSWORD}  # 从环境变量或 Secret 读取
    database: 0
```

**通过 Helm 设置环境变量**：

```bash
# 设置环境变量
export SPRING_REDIS_PASSWORD="YourRedisPassword"

# 部署时传入
helm install chaosblade-box ./chaosblade-box-1.1.0.tgz \
  --namespace chaosblade \
  --set spring.redis.password="${SPRING_REDIS_PASSWORD}"
```

### 验证 Redis 配置

#### 1. 检查 Redis 服务状态

```bash
# Docker 环境
docker ps | grep redis
docker logs redis

# 检查端口监听
netstat -tlnp | grep 6379
```

#### 2. 测试 Redis 连接

```bash
# 无密码
redis-cli -h 127.0.0.1 -p 6379 ping

# 有密码（使用环境变量）
export REDIS_PASSWORD="YourPassword"
redis-cli -h 127.0.0.1 -p 6379 -a ${REDIS_PASSWORD} ping

# 进入 Redis CLI
redis-cli -h 127.0.0.1 -p 6379 -a ${REDIS_PASSWORD}

# 查看信息
INFO
KEYS *
```

#### 3. 检查 ChaosBlade-Box 日志

启动 ChaosBlade-Box 后，查看日志确认 Redis 连接成功：

```bash
# 查看日志
tail -f logs/chaosblade-box.log | grep -i redis

# 应看到类似信息
# Successfully connected to Redis at 127.0.0.1:6379
```

### Redis 性能优化

#### 1. 配置持久化策略

```bash
# 启动时配置 AOF 和 RDB
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server \
  --appendonly yes \
  --appendfsync everysec \
  --save 900 1 \
  --save 300 10 \
  --save 60 10000
```

#### 2. 设置内存限制

```bash
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  --memory=2g \
  redis:7.0 \
  redis-server \
  --maxmemory 1.5gb \
  --maxmemory-policy allkeys-lru
```

#### 3. 启用密码认证（生产环境必需）

```bash
# 设置 Redis 密码环境变量
export REDIS_PASSWORD="StrongPassword123"

docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server \
  --requirepass ${REDIS_PASSWORD} \
  --appendonly yes
```

### 故障排查

#### 1. Redis 连接失败

```bash
# 检查 Redis 服务状态
docker ps | grep redis

# 查看 Redis 日志
docker logs redis

# 测试网络连通性
telnet 127.0.0.1 6379

# 检查防火墙
firewall-cmd --list-ports
firewall-cmd --add-port=6379/tcp --permanent
firewall-cmd --reload
```

#### 2. 密码认证失败

```bash
# 使用环境变量中的密码
export REDIS_PASSWORD="YourPassword"

# 检查密码是否正确
redis-cli -h 127.0.0.1 -p 6379 -a ${REDIS_PASSWORD} ping

# 查看 Redis 配置
docker exec -it redis redis-cli CONFIG GET requirepass
```

#### 3. 内存不足

```bash
# 使用环境变量中的密码
export REDIS_PASSWORD="YourPassword"

# 查看 Redis 内存使用
redis-cli -a ${REDIS_PASSWORD} INFO memory

# 查看所有 key
redis-cli -a ${REDIS_PASSWORD} DBSIZE

# 清理缓存（谨慎操作）
redis-cli -a ${REDIS_PASSWORD} FLUSHDB
```

### 最佳实践

1. **生产环境必须设置密码**
   - 使用强密码（至少 16 位，包含字母、数字、特殊字符）
   - 定期更换密码

2. **启用持久化**
   - AOF：适合对数据安全要求高的场景
   - RDB：适合对性能要求高的场景
   - 两者结合：最佳实践

3. **配置资源限制**
   - 设置合理的最大内存
   - 配置内存淘汰策略
   - 监控内存使用情况

4. **网络安全**
   - 绑定特定 IP 地址
   - 使用防火墙限制访问
   - 启用 TLS/SSL（如果支持）

5. **监控和告警**
   - 监控内存使用率
   - 监控连接数
   - 监控命令响应时间
   - 设置告警阈值

### 参考资源

- [Redis 官方文档](https://redis.io/documentation)
- [Redis Docker 镜像](https://hub.docker.com/_/redis)

## 常见问题

### 1. 数据库连接失败

**问题现象**：
```
Error connecting to database: Connection refused
```

**解决方案**：
```bash
# 检查 MySQL 服务状态
systemctl status mysqld  # 或 docker ps | grep mysql

# 检查端口是否监听
netstat -tlnp | grep 3306

# 测试数据库连接
mysql -h 127.0.0.1 -u root -p

# 检查防火墙
firewall-cmd --list-ports
firewall-cmd --add-port=3306/tcp --permanent
firewall-cmd --reload
```

### 2. Redis 连接失败

**问题现象**：
```
Error connecting to Redis: Connection refused
```

**解决方案**：
```bash
# 检查 Redis 服务状态
docker ps | grep redis

# 测试 Redis 连接
redis-cli -h 127.0.0.1 -p 6379 ping

# 如果设置了密码（使用环境变量）
export REDIS_PASSWORD="YourPassword"
redis-cli -h 127.0.0.1 -p 6379 -a ${REDIS_PASSWORD} ping

# 检查 Redis 配置
docker exec -it redis cat /etc/redis/redis.conf | grep bind
```

### 3. 应用启动失败

**问题现象**：
```
Application failed to start
```

**解决方案**：
```bash
# 查看详细日志
tail -f logs/chaosblade-box.log

# 检查 JVM 内存
java -Xms2g -Xmx2g -XX:+PrintFlagsFinal -version | grep HeapSize

# 检查端口占用
lsof -i :7001
# 如果端口被占用，杀死进程或更改端口
kill -9 <PID>
```

### 4. Kubernetes Pod 无法启动

**问题现象**：
```
CrashLoopBackOff or ImagePullBackOff
```

**解决方案**：
```bash
# 查看 Pod 详情
kubectl describe pod <pod-name> -n chaosblade

# 查看日志
kubectl logs <pod-name> -n chaosblade

# 常见问题：
# 1. 镜像拉取失败 - 检查镜像仓库访问权限
kubectl get events -n chaosblade

# 2. 存储挂载失败 - 检查 hostPath 目录权限
ssh <node> 'sudo mkdir -p /data/mysql /data/redis && sudo chmod 777 /data/mysql /data/redis'

# 3. 数据库初始化失败 - 检查 MySQL Pod 日志
kubectl logs chaosblade-box-mysql-xxx -n chaosblade
```

### 5. Web UI 无法访问

**问题现象**：
```
ERR_CONNECTION_REFUSED
```

**解决方案**：
```bash
# 检查 Service
kubectl get svc -n chaosblade

# 检查 Pod 状态
kubectl get pods -n chaosblade

# 端口转发测试
kubectl port-forward -n chaosblade svc/chaosblade-box 7001:7001

# 检查防火墙和安全组
# 确保 7001 端口开放
```

### 6. Agent 连接失败

**问题现象**：
```
Agent connection timeout
```

**解决方案**：
```bash
# 检查 chaos.server.domain 配置
# 确保 Agent 可以访问 ChaosBlade Box 的地址

# 主机部署：使用实际 IP
--chaos.server.domain=192.168.1.100:7001

# Kubernetes 部署：使用 LoadBalancer 或 NodePort 地址

# 检查网络连通性
telnet CHAOSBLADE_BOX_IP 7001
```

### 7. 数据初始化重复

**问题现象**：
```
Duplicate entry error
```

**解决方案**：
```bash
# 首次启动使用 chaos.function.sync.type=ALL
# 后续启动改为 None

# Kubernetes 环境修改 values.yaml
chaos:
  function:
    sync:
      type: None

# 升级部署
helm upgrade chaosblade-box ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  -n chaosblade
```

### 8. 内存不足

**问题现象**：
```
OutOfMemoryError: Java heap space
```

**解决方案**：
```bash
# 调整 JVM 参数
java -Xms4g -Xmx4g -jar chaosblade-box-1.1.0.jar

# Kubernetes 环境增加资源限制
# 修改 deployment.yaml
resources:
  requests:
    memory: "2Gi"
    cpu: "1000m"
  limits:
    memory: "4Gi"
    cpu: "2000m"
```

## 升级指南

### 主机环境升级

```bash
# 1. 备份数据库
mysqldump -u root -p chaosblade > chaosblade_backup_$(date +%Y%m%d).sql

# 2. 停止应用
./stop-chaosblade-box.sh

# 3. 备份旧版本
mv chaosblade-box-1.0.0.jar chaosblade-box-1.0.0.jar.bak

# 4. 部署新版本
参考上述部署文档部署
```

### Kubernetes 环境升级

```bash
# 1. 备份数据库（进入 MySQL Pod）
# 设置 MySQL 密码环境变量
export MYSQL_ROOT_PASSWORD="YourPassword"

kubectl exec -it chaosblade-box-mysql-xxx -n chaosblade -- \
  mysqldump -u root -p${MYSQL_ROOT_PASSWORD} chaosblade > chaosblade_backup_$(date +%Y%m%d).sql

# 2. 更新 Chart
helm upgrade chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --namespace chaosblade

# 3. 监控升级过程
kubectl rollout status deployment/chaosblade-box -n chaosblade

# 4. 验证升级
kubectl get pods -n chaosblade
kubectl logs -f deployment/chaosblade-box -n chaosblade

# 5. 回滚（如果需要）
helm rollback chaosblade-box -n chaosblade
```

## 卸载

### Kubernetes 环境卸载

```bash
# 1. 备份数据（可选）
# 设置 MySQL 密码环境变量
export MYSQL_ROOT_PASSWORD="YourPassword"

kubectl exec -it chaosblade-box-mysql-xxx -n chaosblade -- \
  mysqldump -u root -p${MYSQL_ROOT_PASSWORD} chaosblade > chaosblade_final_backup.sql

# 2. 卸载 Helm Release
helm uninstall chaosblade-box -n chaosblade

# 3. 删除命名空间（将删除所有资源）
kubectl delete namespace chaosblade

# 4. 验证卸载
kubectl get all -n chaosblade
# 应返回：No resources found
```

## 获取帮助

- **GitHub Issues**: https://github.com/chaosblade-io/chaosblade-box/issues
- **钉钉群**: 23177705

## 许可证

ChaosBlade-Box 使用 Apache License 2.0 许可证。详见 [LICENSE](LICENSE) 文件。

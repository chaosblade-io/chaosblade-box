# ChaosBlade-Box Deployment Guide

English | [中文](./DEPLOYMENT_zh.md)

## Table of Contents

- [Overview](#overview)
- [System Requirements](#system-requirements)
- [Deployment Method 1: Host Environment](#deployment-method-1-host-environment)
  - [Step 1: Get Package](#step-1-get-package)
  - [Step 2: Prepare MySQL](#step-2-prepare-mysql)
  - [Step 3: Configure Database](#step-3-configure-database)
  - [Step 4: Start Application](#step-4-start-application)
  - [Step 5: Verify Deployment](#step-5-verify-deployment)
- [Deployment Method 2: Kubernetes Environment](#deployment-method-2-kubernetes-environment)
  - [Prerequisites](#prerequisites)
  - [Step 1: Get Helm Chart Package](#step-1-get-helm-chart-package)
  - [Step 2: Configure Deployment Parameters](#step-2-configure-deployment-parameters)
  - [Step 3: Deploy Application](#step-3-deploy-application)
  - [Step 4: Verify Deployment](#step-4-verify-deployment)
  - [Step 5: Access Application](#step-5-access-application)
- [Configuration Reference](#configuration-reference)
- [Advanced Features: Batch Host Management](#advanced-features-batch-host-management)
- [Advanced Features: Deploy Redis Cache](#advanced-features-deploy-redis-cache)
- [Troubleshooting](#troubleshooting)
- [Upgrade Guide](#upgrade-guide)
- [Uninstallation](#uninstallation)

## Overview

ChaosBlade-Box is a feature-rich chaos engineering platform that supports fault injection and drills in various scenarios. This document provides detailed instructions for deploying ChaosBlade-Box in both host and Kubernetes environments.

### Architecture Components

- **ChaosBlade-Box**: Main application service (Web UI + API)
- **MySQL**: Database for storing experiment configurations and results
- **Redis**: Cache service (optional) for performance enhancement
- **ChaosBlade-Agent**: Agent program deployed on target hosts/clusters

## System Requirements

### Host Environment
- **Operating System**: Linux (CentOS 7+, Ubuntu 18.04+)
- **JDK**: 1.8 or higher
- **Memory**: Minimum 4GB recommended
- **Disk**: At least 10GB available space
- **Network**: Access to target hosts (for Agent communication)

### Kubernetes Environment
- **Kubernetes Version**: 1.16 or higher
- **Helm**: 3.0 or higher
- **Storage**: Support for hostPath or other storage classes (for data persistence)
- **Resources**: Minimum 2 CPU cores, 4GB memory recommended

## Deployment Method 1: Host Environment

### Step 1: Get Package

Download the latest ChaosBlade Box jar package from [RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases)

You can also compile from source, check compilation help with `make help`

### Step 2: Prepare MySQL

You can use a local database or cloud database. For development or testing, you can deploy MySQL using Docker as follows:

```bash
# Create data directory
sudo mkdir -p /data/mysql

# Set MySQL password environment variable
export MYSQL_ROOT_PASSWORD="YourPassword123"

# Start MySQL container
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

# Verify MySQL status
docker ps | grep mysql
docker logs mysql-8.0
```

> **Tips**:
> - Supports MySQL 5.6+
> - ChaosBlade-Box will automatically create the `chaosblade` database and initialize table structures

### Step 3: Configure Database

#### 3.1 Set Environment Variables

**Recommended: Use environment variables to configure sensitive information**

```bash
# Set database-related environment variables
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword123"

# Optional: Redis configuration (if using Redis)
export SPRING_REDIS_HOST="127.0.0.1"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"
export SPRING_REDIS_DATABASE="0"
```

#### 3.2 Create Configuration File (Optional)

If you need to use a configuration file, create `application-custom.yml`:

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.jdbc.Driver
```

> **Note**:
> - Redis configuration is optional. If you need to enable Redis cache, please refer to [Advanced Features: Deploy Redis Cache](#advanced-features-deploy-redis-cache) section
> - It is recommended to use environment variables to configure passwords to avoid storing sensitive information in plaintext in configuration files

### Step 4: Start Application

#### 4.1 Basic Startup Command

**Ensure environment variables are set** (refer to Step 3.1)

```bash
# Option A: Start with configuration file (configuration file references environment variables)
java -jar chaosblade-box-1.1.0.jar \
  --spring.config.additional-location=./application-custom.yml

# Option B: Start directly with environment variables (Recommended)
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

> **Note**: `${VAR:-default}` syntax means use the default value if the environment variable is not set

#### 4.2 Background Startup (Recommended for Production)

```bash
# Create startup script
cat > start-chaosblade-box.sh << 'EOF'
#!/bin/bash

APP_NAME=chaosblade-box
JAR_FILE=chaosblade-box-1.1.0.jar
LOG_DIR=logs
PID_FILE=${APP_NAME}.pid

# Check if environment variables are set
if [ -z "${SPRING_DATASOURCE_PASSWORD}" ]; then
    echo "Error: SPRING_DATASOURCE_PASSWORD environment variable is not set"
    exit 1
fi

# Create log directory
mkdir -p ${LOG_DIR}

# Check if already running
if [ -f ${PID_FILE} ]; then
    OLD_PID=$(cat ${PID_FILE})
    if ps -p ${OLD_PID} > /dev/null 2>&1; then
        echo "${APP_NAME} is already running with PID ${OLD_PID}"
        exit 1
    fi
fi

# Start application
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

# Save PID
echo $! > ${PID_FILE}
echo "${APP_NAME} started with PID $(cat ${PID_FILE})"
EOF

# Add execute permission
chmod +x start-chaosblade-box.sh

# Start application
./start-chaosblade-box.sh
```

#### 5.3 Create Stop Script

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
    
    # Wait for process to exit
    for i in {1..30}; do
        if ! ps -p ${PID} > /dev/null 2>&1; then
            echo "${APP_NAME} stopped"
            rm ${PID_FILE}
            exit 0
        fi
        sleep 1
    done
    
    # Force kill
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

#### 4.4 Configure System Service (Optional)

```bash
# Create environment variable file
sudo cat > /etc/chaosblade-box/chaosblade-box.env << 'EOF'
SPRING_DATASOURCE_URL=jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=YourPassword123
SPRING_REDIS_HOST=127.0.0.1
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=YourRedisPassword
EOF

# Set file permissions (readable only by root)
sudo chmod 600 /etc/chaosblade-box/chaosblade-box.env
sudo chown root:root /etc/chaosblade-box/chaosblade-box.env

# Create systemd service file
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

# Reload systemd
sudo systemctl daemon-reload

# Start service
sudo systemctl start chaosblade-box

# Enable auto-start on boot
sudo systemctl enable chaosblade-box

# Check status
sudo systemctl status chaosblade-box
```

### Step 5: Verify Deployment

```bash
# View logs
tail -f logs/chaosblade-box.log

# Check port
netstat -tlnp | grep 7001
# or
ss -tlnp | grep 7001

# Test API
curl http://localhost:7001/api/status

# Access Web UI
# Open in browser: http://YOUR_SERVER_IP:7001
```

## Deployment Method 2: Kubernetes Environment

### Prerequisites

1. **Kubernetes Cluster**
   - kubectl configured and able to access cluster
   - Permission to create resources

2. **Helm Installation**
```bash
# Check Helm version
helm version

# If Helm 3 not installed
curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# Verify installation
helm version
```

### Step 1: Get Helm Chart Package

Download the latest ChaosBlade Box helm package from [RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases)

You can also package from source, check compilation help with `make help`

### Step 2: Configure Deployment Parameters

#### 2.1 Create Secret (Recommended)

**Use Kubernetes Secret to store sensitive information**

```bash
# Create namespace
kubectl create namespace chaosblade

# Set password environment variables
export MYSQL_PASSWORD="YourMysqlPassword123"
export REDIS_PASSWORD="YourRedisPassword"

# Create Secret to store database passwords (using environment variables)
kubectl create secret generic chaosblade-secret \
  --from-literal=mysql-password="${MYSQL_PASSWORD}" \
  --from-literal=redis-password="${REDIS_PASSWORD}" \
  -n chaosblade

# Verify Secret
kubectl get secret chaosblade-secret -n chaosblade
```

#### 2.2 Create Custom Configuration File

Create custom configuration file `custom-values.yaml`:

**Option A: Use External MySQL and Redis (Production Recommended)**

```yaml
# Disable built-in MySQL
mysql:
  enable: false

# Disable built-in Redis
redis:
  enable: false

# Configure external database (read password from Secret, see instructions below)
spring:
  datasource:
    url: jdbc:mysql://external-mysql.example.com:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: chaosblade_user
    # Password injected via Secret, see instructions below
  redis:
    host: external-redis.example.com
    port: 6379
    # Password injected via Secret, see instructions below
    database: 0
```

**Option B: Use Built-in MySQL and Redis (Dev/Test Environment)**

```yaml
# Enable built-in MySQL
mysql:
  enable: true
  image:
    repository: mysql
    version: 8.0.0
    pullPolicy: IfNotPresent

# Enable built-in Redis
redis:
  enable: true
  image:
    repository: redis
    version: 7.0
    pullPolicy: IfNotPresent

# Database configuration (read password from Secret)
spring:
  datasource:
    url: jdbc:mysql://chaosblade-box-mysql:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    # Password injected via Secret, see instructions below
  redis:
    host: chaosblade-box-redis
    port: 6379
    # Password injected via Secret, see instructions below
    database: 0

# Logging configuration
logging:
  level:
    root: info
    box: info
```

> **Important Notes**:
> - **It is recommended to use Kubernetes Secret** to store passwords, avoid storing them in plaintext in values.yaml
> - If you must use values.yaml, ensure file permissions are set to 600 and do not commit to version control
> - Passwords can be injected via environment variables or Secret:
>   ```bash
>   # Method 1: Pass via Helm parameters (read from Secret)
>   helm install chaosblade-box ./chaosblade-box-1.1.0.tgz \
>     --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) -n chaosblade
>   
>   # Method 2: Use environment variables (configure in Deployment)
>   # Need to add envFrom reference to Secret in Helm Chart Deployment template
>   ```

### Step 3: Deploy Application

#### 3.1 Create Namespace

```bash
# Create namespace
kubectl create namespace chaosblade

# Verify namespace
kubectl get namespace chaosblade
```

#### 3.2 Deploy Application

```bash
# Option A: Deploy with custom configuration (Recommended)
# Read password from Secret
helm install chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) \
  --set spring.redis.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.redis-password}' | base64 -d) \
  --namespace chaosblade

# Option B: Deploy with command line parameters (read from Secret)
helm install chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  --namespace chaosblade \
  --set mysql.enable=true \
  --set redis.enable=true \
  --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) \
  --set spring.redis.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.redis-password}' | base64 -d)

# Option C: Use environment variables (if Helm Chart supports)
export MYSQL_PASSWORD=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d)
export REDIS_PASSWORD=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.redis-password}' | base64 -d)

helm install chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --set spring.datasource.password=${MYSQL_PASSWORD} \
  --set spring.redis.password=${REDIS_PASSWORD} \
  --namespace chaosblade

# Check deployment status
helm list -n chaosblade
kubectl get pods -n chaosblade -w
```
> **Note**: If Redis is not used, spring.redis.password does not need to be set

#### 3.3 Wait for Pods Ready

```bash
# Continuously monitor Pod status
watch kubectl get pods -n chaosblade

# Expected output:
# NAME                                   READY   STATUS    RESTARTS   AGE
# chaosblade-box-xxxxxxxxxx-xxxxx       1/1     Running   0          2m
# chaosblade-box-mysql-xxxxxxxxx-xxxxx  1/1     Running   0          2m
# chaosblade-box-redis-xxxxxxxxx-xxxxx  1/1     Running   0          2m
```
> **Note**: If using built-in mysql and redis, the above two PODs will be displayed. For enabling method, see Option B in section 2.2 above

### Step 4: Verify Deployment

```bash
# View all resources
kubectl get all -n chaosblade

# View Pod logs
kubectl logs -f deployment/chaosblade-box -n chaosblade

# View Services
kubectl get svc -n chaosblade

# Check database connection (enter Pod)
kubectl exec -it deployment/chaosblade-box -n chaosblade -- sh
# Execute in Pod
nc -zv chaosblade-box-mysql 3306
nc -zv chaosblade-box-redis 6379
```

### Step 5: Access Application

#### 5.1 Via LoadBalancer (if cloud environment supports)

```bash
# View External IP
kubectl get svc chaosblade-box -n chaosblade

# Example output:
# NAME              TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)          AGE
# chaosblade-box    LoadBalancer   192.168.255.1    10.10.10.100     7001:32250/TCP   5m

# Access URL
# http://10.10.10.100:7001
```

#### 5.2 Via NodePort

Modify Service type to NodePort:
```bash
kubectl patch svc chaosblade-box -n chaosblade -p '{"spec":{"type":"NodePort"}}'

# View NodePort
kubectl get svc chaosblade-box -n chaosblade

# Example output:
# NAME              TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
# chaosblade-box    NodePort   192.168.255.1    <none>        7001:30123/TCP   5m

# Access URL (use any node IP)
# http://NODE_IP:30123
```

#### 5.3 Via Port Forward (Development/Debugging)

```bash
# Forward port to local
kubectl port-forward -n chaosblade svc/chaosblade-box 7001:7001

# Access URL
# http://localhost:7001
```

#### 5.4 Via Ingress (Production Recommended)

Create Ingress resource:
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

Apply Ingress:
```bash
kubectl apply -f ingress.yaml

# Configure DNS or hosts
echo "INGRESS_IP chaosblade.example.com" | sudo tee -a /etc/hosts

# Access URL
# http://chaosblade.example.com
```

## Configuration Reference

### Environment Variable Configuration (Recommended)

**For security reasons, it is strongly recommended to use environment variables to configure all sensitive information (passwords, etc.)**:

```bash
# Database configuration
export SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword"  # Required

# Redis configuration (optional)
export SPRING_REDIS_HOST="redis-host"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"  # Optional
export SPRING_REDIS_DATABASE="0"

# MySQL Docker container password
export MYSQL_ROOT_PASSWORD="YourPassword123"

# Redis Docker container password (optional)
export REDIS_PASSWORD="YourRedisPassword"
```

**Persist environment variables** (recommended):

```bash
# Create environment variable configuration file
cat > ~/.chaosblade-box-env << 'EOF'
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword"
export SPRING_REDIS_HOST="127.0.0.1"
export SPRING_REDIS_PORT="6379"
export SPRING_REDIS_PASSWORD="YourRedisPassword"
export SPRING_REDIS_DATABASE="0"
EOF

# Set file permissions (readable only by owner)
chmod 600 ~/.chaosblade-box-env

# Load environment variables before each use
source ~/.chaosblade-box-env
```

### Core Configuration Parameters

#### Database Configuration

Configuration file example (using environment variables):
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
    driver-class-name: com.mysql.jdbc.Driver
```

#### Redis Configuration

Configuration file example (using environment variables):
```yaml
spring:
  data:
    redis:
      host: ${SPRING_REDIS_HOST}
      port: ${SPRING_REDIS_PORT}
      password: ${SPRING_REDIS_PASSWORD}  # Optional
      database: ${SPRING_REDIS_DATABASE}
      timeout: 2000

chaos:
  cache:
    enable: true  # Enable cache
```

#### Agent Configuration
```yaml
chaos:
  agent:
    version: 1.1.0  # Agent version
    repository: ghcr.io/chaosblade-io/chaosblade-box-agent  # Agent image repository
    url: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosagent-1.1.0-linux_amd64.tar.gz
    url_arm64: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosagent-1.1.0-linux_arm64.tar.gz
    helm: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosblade-box-agent-1.1.0-helm_amd64.tgz
    helm_arm64: https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.1.0/chaosblade-box-agent-1.1.0-helm_arm64.tgz
  function:
    sync:
      type: ALL  # Data sync type: ALL, ChaosBlade, UserApp, None, LITMUS_CHAOS
  server:
    domain: ''  # ChaosBlade Box access address, e.g., 192.168.1.100:7001
```

#### Feature Configuration
```yaml
chaos:
  function:
    sync:
      type: ALL  # Use ALL for first startup, None for subsequent startups
      # ALL: Initialize all chaos experiment data
      # ChaosBlade: Initialize only ChaosBlade related data
      # UserApp: Initialize only user application data
      # None: No data initialization
      # LITMUS_CHAOS: Initialize only Litmus Chaos data
  prometheus:
    api: http://prometheus-server:9090  # Prometheus API address (optional)
```

#### Logging Configuration
```yaml
logging:
  level:
    root: info  # Global log level
    box: info   # ChaosBlade Box log level
    # Options: trace, debug, info, warn, error
```

### Environment Variables (Host Deployment)

Can also configure via environment variables:
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

## Advanced Features: Batch Host Management

> **Applicable Scenarios**: Use Ansible for automation when you need to batch deploy and manage ChaosBlade Agent across many hosts.
> 
> **Scenarios where this is NOT needed**:
> - Kubernetes deployment (Agents deployed automatically via DaemonSet)
> - Single or few hosts (manual Agent deployment is sufficient)
> - Existing automation tools in place

### What is Batch Host Management?

ChaosBlade-Box provides Ansible-based batch host management capabilities:

- 🚀 Batch deploy Agents to multiple hosts
- 🔄 Centralized host configuration management
- 📦 Automated Agent installation/uninstallation
- 🔐 Automatic SSH key authentication setup
- 📊 Batch connectivity testing

### Prerequisites

#### 1. Install Ansible

```bash
# Check if installed
ansible --version

# Install Ansible (CentOS/RHEL)
sudo yum install ansible -y

# Install Ansible (Ubuntu/Debian)
sudo apt-get update
sudo apt-get install ansible -y

# Verify installation
ansible --version
```

#### 2. Install Expect

Expect is used for automated SSH key distribution:

```bash
# Check if installed
expect -v

# Install Expect (CentOS/RHEL)
sudo yum install expect -y

# Install Expect (Ubuntu/Debian)
sudo apt-get install expect -y

# Verify installation
expect -v
```

#### 3. Configure SSH Keys

```bash
# Check existing keys
ls ~/.ssh

# If regenerating, backup old keys first
mkdir -p ~/.ssh/backup
mv ~/.ssh/id_rsa* ~/.ssh/backup/ 2>/dev/null || true

# Generate new SSH key pair (no passphrase)
ssh-keygen -t rsa -b 4096 -C "chaosblade-box" -f ~/.ssh/id_rsa -N ""

# Verify key generation
ls -la ~/.ssh/id_rsa*
```

#### 4. Prepare SSH Key Distribution Script

Download [sshKey.sh](https://github.com/chaosblade-io/chaosblade-box/blob/main/ssh/sshKey.sh) and place it in the same directory as the chaosblade-box jar:

```bash
# Download script
wget https://raw.githubusercontent.com/chaosblade-io/chaosblade-box/main/ssh/sshKey.sh

# Add execute permission
chmod +x sshKey.sh

# View script
cat sshKey.sh
```

### Batch Deploy Agents with Ansible

#### 1. Configure Host Inventory

Edit `/etc/ansible/hosts` file to add target hosts:

```ini
# Edit host inventory
sudo vim /etc/ansible/hosts

# Add host information
[chaosblade_agents]
192.168.1.101 ansible_user=root ansible_ssh_pass=YourPassword
192.168.1.102 ansible_user=root ansible_ssh_pass=YourPassword
192.168.1.103 ansible_user=root ansible_ssh_pass=YourPassword

# Or use key-based authentication (recommended)
[chaosblade_agents]
192.168.1.101 ansible_user=root
192.168.1.102 ansible_user=root
192.168.1.103 ansible_user=root
```

#### 2. Distribute SSH Keys

Use `sshKey.sh` script to automatically distribute keys:

```bash
# Execute script to distribute keys to all hosts
./sshKey.sh

# Or manually distribute to specific hosts
ssh-copy-id root@192.168.1.101
ssh-copy-id root@192.168.1.102
ssh-copy-id root@192.168.1.103
```

#### 3. Test Connection

```bash
# Test connection to all hosts
ansible chaosblade_agents -m ping

# Expected output (success)
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

#### 4. Batch Deploy via Web UI

1. **Login to ChaosBlade-Box Web UI**
   - Visit http://YOUR_BOX_IP:7001

2. **Add Hosts**
   - Go to "Host Management" page
   - Click "Add Host"
   - Enter host information (IP, port, username, etc.)

3. **Batch Deploy Agents**
   - Select hosts to deploy
   - Click "Batch Deploy Agent"
   - System will automatically deploy via Ansible

4. **Verify Deployment**
   - Check host status
   - Confirm Agent connections are normal

### Ansible Command Line Operations

#### Batch Execute Commands

```bash
# Execute commands on all hosts
ansible chaosblade_agents -m shell -a "uptime"

# Check host system information
ansible chaosblade_agents -m setup

# Batch install packages
ansible chaosblade_agents -m yum -a "name=wget state=present"
```

#### Batch File Distribution

```bash
# Distribute files to all hosts
ansible chaosblade_agents -m copy -a "src=/path/to/file dest=/tmp/"

# Distribute Agent package
ansible chaosblade_agents -m copy -a "src=./chaosagent.tar.gz dest=/opt/"
```

#### Batch Agent Management

```bash
# Batch start Agents
ansible chaosblade_agents -m shell -a "systemctl start chaosagent"

# Batch check Agent status
ansible chaosblade_agents -m shell -a "systemctl status chaosagent"

# Batch stop Agents
ansible chaosblade_agents -m shell -a "systemctl stop chaosagent"
```

### Troubleshooting

#### 1. Ansible Connection Failed

```bash
# Check host connectivity
ping 192.168.1.101

# Test SSH connection
ssh root@192.168.1.101

# View Ansible verbose logs
ansible chaosblade_agents -m ping -vvv

# Check SSH keys
ls -la ~/.ssh/
cat ~/.ssh/id_rsa.pub
```

#### 2. SSH Key Authentication Failed

```bash
# Manually copy key to target host
ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.1.101

# Test key-based login
ssh -i ~/.ssh/id_rsa root@192.168.1.101

# Check target host authorized_keys
ssh root@192.168.1.101 "cat ~/.ssh/authorized_keys"
```

#### 3. Expect Script Execution Failed

```bash
# Check Expect installation
which expect

# Manually execute script to see errors
./sshKey.sh

# Check script permissions
ls -la sshKey.sh
chmod +x sshKey.sh
```

### Best Practices

1. **Use Key-Based Authentication Instead of Passwords**
   - More secure
   - Avoids storing passwords in plaintext

2. **Configure Ansible Timeout**
```ini
# /etc/ansible/ansible.cfg
[defaults]
timeout = 30
```

3. **Group Hosts by Environment**
```ini
# /etc/ansible/hosts
[production]
prod-host-1
prod-host-2

[testing]
test-host-1
test-host-2
```

4. **Use Ansible Vault for Sensitive Data**
```bash
# Encrypt password file
ansible-vault encrypt vars/passwords.yml

# Use encrypted file
ansible-playbook -i hosts playbook.yml --ask-vault-pass
```

5. **Keep Host Inventory Updated**
   - Remove decommissioned hosts
   - Add newly deployed hosts
   - Maintain current inventory

### References

- [Ansible Official Documentation](https://docs.ansible.com/)
- [sshKey.sh Script Source](https://github.com/chaosblade-io/chaosblade-box/blob/main/ssh/sshKey.sh)
- [ChaosBlade Agent Deployment Guide](https://github.com/chaosblade-io/chaosblade-box)

## Advanced Features: Deploy Redis Cache

> **Applicable Scenarios**: Redis cache can significantly improve ChaosBlade-Box performance, but it's not required.
> 
> **Scenarios where Redis is NOT needed**:
> - Small-scale deployments (few experiments, few users)
> - Testing and development environments
> - Resource-constrained environments

### What is Redis Cache?

ChaosBlade-Box supports using Redis as a distributed cache to:

- ⚡ Improve system response speed
- 📊 Reduce database pressure
- 🔄 Support distributed deployments
- 💾 Cache experiment configurations and results

### Deploy Redis

#### Option A: Deploy using Docker (Recommended)

```bash
# Create data directory
sudo mkdir -p /data/redis

# Start Redis container (without password)
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server --appendonly yes

# Set Redis password environment variable
export REDIS_PASSWORD="YourRedisPassword"

# Start Redis container (with password, recommended for production)
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server --appendonly yes --requirepass ${REDIS_PASSWORD}

# Verify Redis status
docker ps | grep redis

# Test connection
redis-cli ping  # Should return PONG

# If password is set
redis-cli -a ${REDIS_PASSWORD} ping
```

#### Option B: Use Existing Redis or Cloud Redis

If you have an existing Redis instance (local installation, Alibaba Cloud Redis, etc.), you can use it directly. Just prepare the following information:
- Redis host and port
- Redis password (if any)
- Database number (default is 0)

### Configure ChaosBlade-Box to Use Redis

#### Host Environment Configuration

Edit `application-custom.yml` or add the following configuration in the startup command:

```yaml
spring:
  data:
    redis:
      host: 127.0.0.1  # Redis address
      port: 6379
      password: YourRedisPassword  # If password is set
      database: 0
      timeout: 2000

chaos:
  cache:
    enable: true  # Enable cache
```

Or add parameters in the startup command:

```bash
java -jar chaosblade-box-1.1.0.jar \
  --spring.datasource.url="jdbc:mysql://..." \
  --spring.datasource.username=root \
  --spring.datasource.password=YourPassword \
  --spring.data.redis.host=127.0.0.1 \
  --spring.data.redis.port=6379 \
  --spring.data.redis.password=YourRedisPassword \
  --chaos.cache.enable=true
```

#### Kubernetes Environment Configuration

Use built-in Redis (in `values.yaml`):

```yaml
redis:
  enable: true  # Enable built-in Redis
  image:
    repository: redis
    version: 7.0
    pullPolicy: IfNotPresent

spring:
  redis:
    host: chaosblade-box-redis  # Use built-in Redis service name
    port: 6379
    password: ""  # Optional: Set password
    database: 0
```

Use external Redis:

```yaml
redis:
  enable: false  # Disable built-in Redis

spring:
  redis:
    host: external-redis.example.com  # External Redis address
    port: 6379
    password: ExternalRedisPassword
    database: 0
```

### Verify Redis Configuration

#### 1. Check Redis Service Status

```bash
# Docker environment
docker ps | grep redis
docker logs redis

# Check port listening
netstat -tlnp | grep 6379
```

#### 2. Test Redis Connection

```bash
# Without password
redis-cli -h 127.0.0.1 -p 6379 ping

# With password
redis-cli -h 127.0.0.1 -p 6379 -a YourPassword ping

# Enter Redis CLI
redis-cli -h 127.0.0.1 -p 6379 -a YourPassword

# View information
INFO
KEYS *
```

#### 3. Check ChaosBlade-Box Logs

After starting ChaosBlade-Box, check logs to confirm Redis connection:

```bash
# View logs
tail -f logs/chaosblade-box.log | grep -i redis

# Should see similar information
# Successfully connected to Redis at 127.0.0.1:6379
```

### Redis Performance Optimization

#### 1. Configure Persistence Strategy

```bash
# Configure AOF and RDB at startup
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

#### 2. Set Memory Limits

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

#### 3. Enable Password Authentication (Required for Production)

```bash
docker run -d \
  --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7.0 \
  redis-server \
  --requirepass StrongPassword123 \
  --appendonly yes
```

### Troubleshooting

#### 1. Redis Connection Failed

```bash
# Check Redis service status
docker ps | grep redis

# View Redis logs
docker logs redis

# Test network connectivity
telnet 127.0.0.1 6379

# Check firewall
firewall-cmd --list-ports
firewall-cmd --add-port=6379/tcp --permanent
firewall-cmd --reload
```

#### 2. Password Authentication Failed

```bash
# Check if password is correct
redis-cli -h 127.0.0.1 -p 6379 -a YourPassword ping

# View Redis configuration
docker exec -it redis redis-cli CONFIG GET requirepass
```

#### 3. Out of Memory

```bash
# View Redis memory usage
redis-cli -a YourPassword INFO memory

# View all keys
redis-cli -a YourPassword DBSIZE

# Clear cache (use with caution)
redis-cli -a YourPassword FLUSHDB
```

### Best Practices

1. **Password is Required for Production**
   - Use strong passwords (at least 16 characters with letters, numbers, special characters)
   - Rotate passwords regularly

2. **Enable Persistence**
   - AOF: Suitable for scenarios requiring high data safety
   - RDB: Suitable for scenarios requiring high performance
   - Combination: Best practice

3. **Configure Resource Limits**
   - Set reasonable maximum memory
   - Configure memory eviction policy
   - Monitor memory usage

4. **Network Security**
   - Bind to specific IP address
   - Use firewall to restrict access
   - Enable TLS/SSL (if supported)

5. **Monitoring and Alerting**
   - Monitor memory usage
   - Monitor connection count
   - Monitor command response time
   - Set alert thresholds

### References

- [Redis Official Documentation](https://redis.io/documentation)
- [Redis Docker Image](https://hub.docker.com/_/redis)
- [Alibaba Cloud Redis Documentation](https://help.aliyun.com/product/26340.html)

## Troubleshooting

### 1. Database Connection Failed

**Issue**:
```
Error connecting to database: Connection refused
```

**Solution**:
```bash
# Check MySQL service status
systemctl status mysqld  # or docker ps | grep mysql

# Check if port is listening
netstat -tlnp | grep 3306

# Test database connection
mysql -h 127.0.0.1 -u root -p

# Check firewall
firewall-cmd --list-ports
firewall-cmd --add-port=3306/tcp --permanent
firewall-cmd --reload
```

### 2. Redis Connection Failed

**Issue**:
```
Error connecting to Redis: Connection refused
```

**Solution**:
```bash
# Check Redis service status
docker ps | grep redis

# Test Redis connection
redis-cli -h 127.0.0.1 -p 6379 ping

# If password is set
redis-cli -h 127.0.0.1 -p 6379 -a YourPassword ping

# Check Redis configuration
docker exec -it redis cat /etc/redis/redis.conf | grep bind
```

### 3. Application Start Failed

**Issue**:
```
Application failed to start
```

**Solution**:
```bash
# View detailed logs
tail -f logs/chaosblade-box.log

# Check JVM memory
java -Xms2g -Xmx2g -XX:+PrintFlagsFinal -version | grep HeapSize

# Check port usage
lsof -i :7001
# If port is occupied, kill process or change port
kill -9 <PID>
```

### 4. Kubernetes Pod Cannot Start

**Issue**:
```
CrashLoopBackOff or ImagePullBackOff
```

**Solution**:
```bash
# View Pod details
kubectl describe pod <pod-name> -n chaosblade

# View logs
kubectl logs <pod-name> -n chaosblade

# Common issues:
# 1. Image pull failed - Check image repository access
kubectl get events -n chaosblade

# 2. Storage mount failed - Check hostPath directory permissions
ssh <node> 'sudo mkdir -p /data/mysql /data/redis && sudo chmod 777 /data/mysql /data/redis'

# 3. Database initialization failed - Check MySQL Pod logs
kubectl logs chaosblade-box-mysql-xxx -n chaosblade
```

### 5. Web UI Cannot Access

**Issue**:
```
ERR_CONNECTION_REFUSED
```

**Solution**:
```bash
# Check Service
kubectl get svc -n chaosblade

# Check Pod status
kubectl get pods -n chaosblade

# Port forward test
kubectl port-forward -n chaosblade svc/chaosblade-box 7001:7001

# Check firewall and security groups
# Ensure port 7001 is open
```

### 6. Agent Connection Failed

**Issue**:
```
Agent connection timeout
```

**Solution**:
```bash
# Check chaos.server.domain configuration
# Ensure Agent can access ChaosBlade Box address

# Host deployment: Use actual IP
--chaos.server.domain=192.168.1.100:7001

# Kubernetes deployment: Use LoadBalancer or NodePort address

# Check network connectivity
telnet CHAOSBLADE_BOX_IP 7001
```

### 7. Data Initialization Duplicated

**Issue**:
```
Duplicate entry error
```

**Solution**:
```bash
# First startup use chaos.function.sync.type=ALL
# Subsequent startups change to None

# Kubernetes environment modify values.yaml
chaos:
  function:
    sync:
      type: None

# Upgrade deployment
helm upgrade chaosblade-box ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  -n chaosblade
```

### 8. Out of Memory

**Issue**:
```
OutOfMemoryError: Java heap space
```

**Solution**:
```bash
# Adjust JVM parameters
java -Xms4g -Xmx4g -jar chaosblade-box-1.1.0.jar

# Kubernetes environment increase resource limits
# Modify deployment.yaml
resources:
  requests:
    memory: "2Gi"
    cpu: "1000m"
  limits:
    memory: "4Gi"
    cpu: "2000m"
```

## Upgrade Guide

### Host Environment Upgrade

```bash
# 1. Backup database
mysqldump -u root -p chaosblade > chaosblade_backup_$(date +%Y%m%d).sql

# 2. Stop application
./stop-chaosblade-box.sh

# 3. Backup old version
mv chaosblade-box-1.0.0.jar chaosblade-box-1.0.0.jar.bak

# 4. Deploy new version
Refer to the deployment documentation above
```

### Kubernetes Environment Upgrade

```bash
# 1. Backup database (enter MySQL Pod)
# Set MySQL password environment variable
export MYSQL_ROOT_PASSWORD="YourPassword"

kubectl exec -it chaosblade-box-mysql-xxx -n chaosblade -- \
  mysqldump -u root -p${MYSQL_ROOT_PASSWORD} chaosblade > chaosblade_backup_$(date +%Y%m%d).sql

# 2. Update Chart
helm upgrade chaosblade-box \
  ./chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --namespace chaosblade

# 3. Monitor upgrade process
kubectl rollout status deployment/chaosblade-box -n chaosblade

# 4. Verify upgrade
kubectl get pods -n chaosblade
kubectl logs -f deployment/chaosblade-box -n chaosblade

# 5. Rollback (if needed)
helm rollback chaosblade-box -n chaosblade
```

## Uninstallation

### Kubernetes Environment Uninstallation

```bash
# 1. Backup data (optional)
# Set MySQL password environment variable
export MYSQL_ROOT_PASSWORD="YourPassword"

kubectl exec -it chaosblade-box-mysql-xxx -n chaosblade -- \
  mysqldump -u root -p${MYSQL_ROOT_PASSWORD} chaosblade > chaosblade_final_backup.sql

# 2. Uninstall Helm Release
helm uninstall chaosblade-box -n chaosblade

# 3. Delete namespace (will delete all resources)
kubectl delete namespace chaosblade

# 4. Verify uninstallation
kubectl get all -n chaosblade
# Should return: No resources found
```

## Get Help

- **GitHub Issues**: https://github.com/chaosblade-io/chaosblade-box/issues
- **DingTalk Group**: 23177705

## License

ChaosBlade-Box is licensed under the Apache License 2.0. See [LICENSE](LICENSE) file for details.

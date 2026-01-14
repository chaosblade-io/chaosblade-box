![logo](https://chaosblade.oss-cn-hangzhou.aliyuncs.com/doc/image/chaosblade-logo.png)

# ChaosBlade-Box: A Chaos Engineering Platform with Rich Scenarios

English | [中文](./README_zh.md)

![license](https://img.shields.io/github/license/chaosblade-io/chaosblade.svg)

> **Note**: For more information about the resilience testing platform, see the [main2](https://github.com/chaosblade-io/chaosblade-box/tree/main2) branch.

## Introduction

ChaosBlade-Box is a comprehensive chaos engineering platform with rich fault injection scenarios. Currently supported scenarios include:
* [chaosblade-exec-os](https://github.com/chaosblade-io/chaosblade-exec-os): Implementation of basic resource experimental scenarios.
* [chaosblade-exec-docker](https://github.com/chaosblade-io/chaosblade-exec-docker): Docker container experimental scenario implementation, standardized by calling the Docker API.
* [chaosblade-operator](https://github.com/chaosblade-io/chaosblade-operator): Kubernetes platform experimental scenario is implemented, chaos experiments are defined by Kubernetes standard CRD method, it is very convenient to use Kubernetes resource operation method To create, update, and delete experimental scenarios, including using kubectl, client-go, etc., and also using the chaosblade cli tool described above.
* [chaosblade-exec-jvm](https://github.com/chaosblade-io/chaosblade-exec-jvm): Java application experimental scenario implementation, using Java Agent technology to mount dynamically, without any access, zero-cost use. It also supports uninstallation and completely recycles various resources created by the Agent.
* [chaosblade-exec-cplus](https://github.com/chaosblade-io/chaosblade-exec-cplus): C++ application experimental scenario implementation, using GDB technology to implement method and code line level experimental scenario injection.
* [litmus-chaos](https://github.com/litmuschaos/litmus): A toolset to do cloud-native chaos engineering

## Quick Start

### 📖 Deployment Documentation

We provide comprehensive deployment guides for both host and Kubernetes environments:

- **[English Deployment Guide](./DEPLOYMENT_en.md)** - Detailed step-by-step deployment instructions
- **[中文部署文档](./DEPLOYMENT_zh.md)** - 详细的分步部署指南

The deployment documentation includes:
- ✅ System requirements and prerequisites
- ✅ Host environment deployment (Docker, direct deployment)
- ✅ Kubernetes environment deployment (Helm Chart)
- ✅ Configuration reference and best practices
- ✅ Troubleshooting guide
- ✅ Upgrade and uninstallation instructions

## Compile

Go to the project root directory which you cloned and execute compile:

### Build Project

```bash
# Build the entire project (including frontend and Java backend)
make build
```

### Build Docker Image

```bash
# Build Docker image (optional parameters: IMAGE_NAME, IMAGE_REGISTRY, VERSION)
make docker-build

# Example: specify image registry
make docker-build IMAGE_REGISTRY=registry.example.com/
```

### Build Helm Package

```bash
# Package Helm chart
make helm-package
```

### Clean Build Artifacts

```bash
# Clean build artifacts
make clean
```

### View All Available Commands

```bash
# Show help information, view all available commands
make help
```

## Deployment Options

### Option 1: Host Environment Deployment

#### Quick Start

1. **Get Package**: Download the latest jar package from [RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases)

2. **Prepare MySQL**: You can use a local database or cloud database. For dev/test environments, you can deploy MySQL using Docker

3. **Configure Environment Variables** (Recommended):
```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://127.0.0.1:3306/chaosblade?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="YourPassword123"
```

4. **Start Application**:
```bash
java -jar chaosblade-box-1.1.0.jar \
  --spring.datasource.url="${SPRING_DATASOURCE_URL}" \
  --spring.datasource.username="${SPRING_DATASOURCE_USERNAME}" \
  --spring.datasource.password="${SPRING_DATASOURCE_PASSWORD}"
```

> **Tip**: For detailed deployment steps, please refer to the [Deployment Guide](./DEPLOYMENT_en.md)

### Option 2: Kubernetes Environment Deployment

#### Quick Start

1. **Get Helm Chart Package**: Download the latest helm package from [RELEASES](https://github.com/chaosblade-io/chaosblade-box/releases)

2. **Create Secret** (Recommended):
```bash
kubectl create secret generic chaosblade-secret \
  --from-literal=mysql-password="YourPassword" \
  -n chaosblade
```

3. **Deploy Application**:
```bash
helm install chaosblade-box chaosblade-box-1.1.0.tgz \
  --set spring.datasource.password=$(kubectl get secret chaosblade-secret -n chaosblade -o jsonpath='{.data.mysql-password}' | base64 -d) \
  --namespace chaosblade
```

> **Tip**: For detailed deployment steps, please refer to the [Deployment Guide](./DEPLOYMENT_en.md)

## Configuration Parameters

| Parameter | Description | Default Value | Required |
|-----------|-------------|---------------|----------|
| `spring.datasource.url` | MySQL connection URL. Not required if using Helm | - | Helm: No<br>Host: Yes |
| `spring.datasource.username` | MySQL username. Not required if using Helm | root | Helm: No<br>Host: Yes |
| `spring.datasource.password` | MySQL password | - | **Yes** |
| `spring.data.redis.host` | Redis host address | localhost | No |
| `spring.data.redis.port` | Redis port | 6379 | No |
| `spring.data.redis.password` | Redis password | - | No |
| `chaos.cache.enable` | Enable cache | false | No |
| `chaos.function.sync.type` | Init chaos data type. Use `ALL` for first start | ALL | No |
| `chaos.agent.version` | chaosblade-box-agent version | 1.1.0 | No |
| `chaos.agent.repository` | chaosblade-box-agent image repository | ghcr.io/chaosblade-io/chaosblade-box-agent | No |
| `chaos.agent.url` | chaosblade-box-agent binary package URL | [OSS Link] | No |
| `chaos.agent.helm` | chaosblade-box-agent helm package URL | [OSS Link] | No |
| `chaos.server.domain` | ChaosBlade Box access address (for Agent connection) | - | Required for host deployment |

### Available chaos.function.sync.type Values

- `ALL`: Initialize all chaos experiment data (recommended for first deployment)
- `ChaosBlade`: Initialize only ChaosBlade related data
- `UserApp`: Initialize only user application data
- `None`: No data initialization (recommended for production subsequent startups)
- `LITMUS_CHAOS`: Initialize only Litmus Chaos data

## Common Issues

### 1. Database Connection Failed
```bash
# Check MySQL service status
docker ps | grep mysql

# Test connection
mysql -h DATASOURCE_HOST -u DATASOURCE_USERNAME -p
```

### 2. Redis Connection Failed
```bash
# Check Redis service status
docker ps | grep redis

# Test connection
redis-cli -h REDIS_HOST -p 6379 ping
```

### 3. Kubernetes Pod Cannot Start
```bash
# View Pod logs
kubectl logs <pod-name> -n chaosblade

# Describe Pod
kubectl describe pod <pod-name> -n chaosblade

# Check events
kubectl get events -n chaosblade
```

### 4. More Issues

Please refer to the detailed [Deployment Documentation](./DEPLOYMENT_en.md) for complete troubleshooting guide.

## Architecture

### Component Architecture

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

### Supported Experiment Scenarios

- **Basic Resources**: CPU, memory, disk, network, etc.
- **Container Scenarios**: Docker container fault injection
- **Kubernetes**: Pod, Node, Service fault injection
- **Application Layer**: Java, C++ application-level fault injection
- **Cloud Native**: Cloud-native scenarios integrated with Litmus Chaos

## Features

- ✅ Rich fault injection scenarios
- ✅ User-friendly Web UI
- ✅ Support for host and Kubernetes environments
- ✅ Experiment orchestration and automation
- ✅ Real-time monitoring and reporting
- ✅ Secure permission management
- ✅ Extensible plugin architecture
- ✅ Prometheus integration
- ✅ Multi-tenancy support

## Upgrade Guide

### Host Environment Upgrade
```bash
# 1. Backup database
mysqldump -u root -p chaosblade > backup.sql

# 2. Stop old version
kill $(cat chaosblade-box.pid)

# 3. Start new version
java -jar chaosblade-box-1.1.0.jar ...
```

### Kubernetes Environment Upgrade
```bash
# Upgrade using Helm
helm upgrade chaosblade-box chaosblade-box-1.1.0.tgz \
  -f custom-values.yaml \
  --namespace chaosblade
```

## Uninstallation

### Host Environment
```bash
# Stop application
kill $(cat chaosblade-box.pid)

# Clean up data (optional)
docker stop mysql-8.0 redis
docker rm mysql-8.0 redis
```

### Kubernetes Environment
```bash
# Uninstall Helm Release
helm uninstall chaosblade-box -n chaosblade

# Delete namespace
kubectl delete namespace chaosblade
```


## Bugs and Feedback

For bug reports, questions, and discussions, please submit [GitHub Issues](https://github.com/chaosblade-io/chaosblade-box/issues).

You can also contact us via:
* **DingTalk Group** (recommended for Chinese users): 23177705
* **Slack**: [chaosblade-io](https://join.slack.com/t/chaosblade-io/shared_invite/zt-f0d3r3f4-TDK13Wr3QRUrAhems28p1w)
* **Gitter**: [chaosblade community](https://gitter.im/chaosblade-io/community)
* **Email**: chaosblade.io.01@gmail.com
* **Twitter**: [chaosblade.io](https://twitter.com/ChaosbladeI)

## Contributing

We welcome every contribution, even if it is just punctuation. See details in [CONTRIBUTING](CONTRIBUTING.md)

## Enterprise Registration

The original intention of our open source project is to lower the threshold for implementing chaos engineering in enterprises, so we highly value the use of the project in enterprises. Welcome to register [here](https://github.com/chaosblade-io/chaosblade/issues/32). After registration, you will be invited to join the enterprise mailing group to discuss challenges encountered during chaos engineering implementation and share experiences.

## License

ChaosBlade-Box is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for the full license text.

## Related Projects

- [ChaosBlade](https://github.com/chaosblade-io/chaosblade) - Chaos engineering experiment execution tool
- [ChaosBlade Operator](https://github.com/chaosblade-io/chaosblade-operator) - Kubernetes chaos engineering operator
- [Litmus](https://github.com/litmuschaos/litmus) - Cloud-native chaos engineering framework

---

**Star our project to get the latest updates!** ⭐

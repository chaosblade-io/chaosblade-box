![logo](https://chaosblade.oss-cn-hangzhou.aliyuncs.com/doc/image/chaosblade-logo.png)  


# ChaosBlade-Box: 一个面向云原生系统的韧性测试平台
🔥🔥🔥 **ChaosBlade-Box 能力升级，从混沌工程平台增强到韧性测试平台，具备基于全链路的故障空间韧性评测能力！** 
<img width="1804" height="948" alt="image" src="https://github.com/user-attachments/assets/8f7c45ca-8fad-4996-a572-0378aaede88d" />

## 特性
* 具备 OpenTelemetry 可观测数据标准化接入能力，支持动态构建服务依赖拓扑。
* 兼容 JMeter 压测引擎，具备压测管理能力，支持故障注入中自动化实施压测。
* 具备故障空间探测，实时计算故障传播路径，精准控制故障影响范围。
* 具备基于 SLO 稳态断言和基于 LLM 的健康度评估能力。

## 贡献者
此能力由中国科学院软件研究所主导和社区一起共建，感谢项目中的各位贡献者

<img width="310" height="48" alt="image" src="https://github.com/user-attachments/assets/f16647d6-c17b-43d7-9e78-209f76516d2e" /> 
X
<img width="492" height="56" alt="image" src="https://github.com/user-attachments/assets/e667c545-f794-47c8-b0ba-726e19cb21dd" />

## 里程碑

- [x] v2.0.0（2025年9月）：Tool模式：基于可观测和压测流量的半自动的故障探测能力
- [ ] v2.x.0（2026年3月）：Copilot模式：基于自然语言对话的交互式演练
- [ ] v3.0.0（2026年9月）：Agent模式：基于知识库和AI推理的韧性评测能力

## 部署
涉及到的项目如下：
* 控制台：https://github.com/chaosblade-io/chaosblade-box
* 后端服务：https://github.com/chaosblade-io/chaosblade-space-exploration

### chaosblade-space-exploration 
使用 Helm Chart 可以一键部署整个微服务架构，包括依赖的 MySQL 和 Redis。

```bash
# 1. 添加 Helm 依赖
cd helm/chaosblade-space-exploration
helm dependency update

# 2. 基本安装（使用内置 MySQL 和 Redis）
helm install chaosblade-space-exploration . -n chaosblade --create-namespace

# 3. 生产环境安装（使用外部数据库）
helm install chaosblade-space-exploration . \
  -f values-production.yaml \
  -n chaosblade \
  --create-namespace

# 4. 检查部署状态
helm status chaosblade-space-exploration -n chaosblade
kubectl get pods -n chaosblade
```

主要特性：
- 自动创建所有必要的 Kubernetes 资源（Deployment、Service、ConfigMap、Secret、RBAC）
- 支持内置或外部 MySQL/Redis
- 完整的 RBAC 权限配置
- 支持 Ingress 和负载均衡
- 健康检查和监控集成
- 支持水平扩缩容

详细配置说明请参考：[Helm Chart 文档](https://github.com/chaosblade-io/chaosblade-space-exploration/blob/main/helm/README.md)

### chaosblade-box
使用 Helm Chart 可以一键部署

```bash
helm install chaosblade-box chaosblade-box-2.0.0-alpha.tgz --namespace chaosblade --set spring.datasource.password=DATASOURCE_PASSWORD
```

您可以通过服务获取BOX-HOST，使用浏览器访问http://10.10.10.03:7001网站即可使用该平台。

```bash
➜  shell kubectl get services -n chaosblade
NAME                        TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)           AGE
chaosblade-box              LoadBalancer   192.168.255.01   10.10.10.03     7001:32250/TCP    12h
chaosblade-box-mysql        ClusterIP      192.168.168.02    <none>           3306/TCP          12h
```

## Bugs and Feedback
如需报告 Bug、提出问题或进行讨论，请提交 [GitHub Issues](https://github.com/chaosblade-io/chaosblade-box/issues)。

您也可以通过以下方式联系我们：
* 钉钉群（建议中文用户加入）：23177705

## 贡献
我们欢迎您的每一份贡献，哪怕只是一个标点符号。查看 [CONTRIBUTING](CONTRIBUTING.md) 详情

## 企业注册
我们开源项目的初衷是降低混沌工程在企业落地的门槛，因此我们非常重视该项目在企业中的应用。欢迎大家关注 [ISSUE](https://github.com/chaosblade-io/chaosblade/issues/32)。注册后，您将受邀加入企业邮件群，共同探讨混沌工程在企业落地中遇到的问题，分享落地经验。

## 许可证
Chaosblade-box 采用 Apache 许可证 2.0 版。

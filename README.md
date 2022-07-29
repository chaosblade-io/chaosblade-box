![logo](https://chaosblade.oss-cn-hangzhou.aliyuncs.com/doc/image/chaosblade-logo.png)  

# Chaosblade-box: An chaos engineering platform with rich scenes
[![Build Status](https://api.travis-ci.org/chaosblade-io/chaosblade-box.svg?branch=main)](https://travis-ci.org/chaosblade-io/chaosblade-box)
![license](https://img.shields.io/github/license/chaosblade-io/chaosblade-box.svg)

## Introduction
Chaosblade-box is an chaos engineering platform with rich scenes, the scenes currently included are:
* [chaosblade-exec-os](https://github.com/chaosblade-io/chaosblade-exec-os): Implementation of basic resource experimental scenarios.
* [chaosblade-exec-docker](https://github.com/chaosblade-io/chaosblade-exec-docker): Docker container experimental scenario implementation, standardized by calling the Docker API.
* [chaosblade-operator](https://github.com/chaosblade-io/chaosblade-operator): Kubernetes platform experimental scenario is implemented, chaos experiments are defined by Kubernetes standard CRD method, it is very convenient to use Kubernetes resource operation method To create, update, and delete experimental scenarios, including using kubectl, client-go, etc., and also using the chaosblade cli tool described above.
* [chaosblade-exec-jvm](https://github.com/chaosblade-io/chaosblade-exec-jvm): Java application experimental scenario implementation, using Java Agent technology to mount dynamically, without any access, zero-cost use It also supports uninstallation and completely recycles various resources created by the Agent.
* [chaosblade-exec-cplus](https://github.com/chaosblade-io/chaosblade-exec-cplus): C ++ application experimental scenario implementation, using GDB technology to implement method and code line level experimental scenario injection.
* [limus-chaos-generic](https://github.com/litmuschaos/litmus): An toolset to do cloud-native chaos engineering

## Compile
Go to the project root directory which you cloned and execute compile:
```bash
mvn clean package -Dmaven.test.skip=true
```

If you compile the chaosblade-box image, you can do:
```bash
make build_image
```

clean compilation:
```bash
mvn clean
```

helm package:
```bash
helm package deploy/chaosblade-box
```

## Pre Run Application For Host
1. If don't have ansible installed, you need install `ansible`
```bash
# Check if there is already installed
ansible --version

# install ansible, eg: Fedora || RedHat 
yum install ansible -y
```
2. If don't have expect installed, you need install `expect`, put [sshKey.sh](https://github.com/chaosblade-io/chaosblade-box/blob/main/ssh/sshKey.sh) and chaosblade-box-1.0.2.jar in a directory
```bash
# Check if there is already installed
expect -v

# install expect, eg: Fedora || RedHat 
yum install expect -y
```
3. Generate public key
```bash
# Check if there is already a key, if there is, delete the previous backup
ls ~/.ssh
rm -rf ~/.ssh/*

# generate public key
ssh-keygen -t rsa
```

## Run Application
If you already have MySQL installed, you need to create a schema named `chaosblade`, if you don't have MySQL installed, you can run it via Docker, run method is as follows:
```bash
docker run -d -it -p 3306:3306 \
            -e MYSQL_DATABASE=chaosblade \
            -e MYSQL_ROOT_PASSWORD=[DATASOURCE_PASSWORD] \
            --name mysql-5.6 mysql:5.6 \
            --character-set-server=utf8mb4 \
            --collation-server=utf8mb4_unicode_ci \
            --default-time_zone='+8:00' \
            --lower_case_table_names=1
```
Notes: You must replace the follow parameters: DATASOURCE_HOST, DATASOURCE_USERNAME, DATASOURCE_PASSWORD, BOX-HOST(localHostIP:port, eg:*.*.*.*:7001)

Then run the application, run method is as follows:

```bash
nohup java -Duser.timezone=Asia/Shanghai -jar chaosblade-box-1.0.0.jar --spring.datasource.url="jdbc:mysql://DATASOURCE_HOST:3306/chaosblade?characterEncoding=utf8&useSSL=false" --spring.datasource.username=DATASOURCE_USERNAME --spring.datasource.password=DATASOURCE_PASSWORD --chaos.server.domain=BOX-HOST> chaosblade-box.log 2>&1 &
```

You can use a browser to access the http://127.0.0.1:7001 website to use the platform.

If you're deployed on kubernetes, the usage method is as follows:

```bash
helm install chaosblade-box chaosblade-box-1.0.0.tgz --namespace chaosblade --set spring.datasource.password=DATASOURCE_PASSWORD
```

You can get BOX-HOST by services. You can use a browser to access the http://10.10.10.03:7001 website to use the platform.

```bash
➜  shell kubectl get services -n chaosblade
NAME                        TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)           AGE
chaosblade-box              LoadBalancer   192.168.255.01   10.10.10.03     7001:32250/TCP    12h
chaosblade-box-mysql        ClusterIP      192.168.168.02    <none>           3306/TCP          12h
```

## Parameter Parsing
* spring.datasource.url: Mysql url.If helm starts, no assignment is required.
* spring.datasource.username: Mysql username.If helm starts, no assignment is required.
* spring.datasource.password: Mysql password.Required.
* chaos.function.sync.type: Init chaos data.If the first start,can use `ALL`.Available values `ALL`,`ChaosBlade`,`UserApp`,`None`,`LITMUS_CHAOS`. Default is `ALL`.
* chaos.agent.version: chaosblade-box-agent version.Default is `1.0.0`.
* chaos.agent.repository: chaosblade-box-agent image repository.Default is `chaosbladeio/chaosblade-agent`.
* chaos.agent.url: chaosblade-box-agent binary package url.Default is `https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.0.0/chaosagent.tar.gz`.
* chaos.agent.helm: chaosblade-box-agent helm package url.Default is `https://chaosblade.oss-cn-hangzhou.aliyuncs.com/platform/release/1.0.0/chaosblade-box-agent-1.0.0.tgz`.


## Bugs and Feedback
For bug report, questions and discussions please submit [GitHub Issues](https://github.com/chaosblade-io/chaosblade-box/issues). 

You can also contact us via:
* Dingding group (recommended for chinese): 23177705
* Slack group: [chaosblade-io](https://join.slack.com/t/chaosblade-io/shared_invite/zt-f0d3r3f4-TDK13Wr3QRUrAhems28p1w)
* Gitter room: [chaosblade community](https://gitter.im/chaosblade-io/community)
* Email: chaosblade.io.01@gmail.com
* Twitter: [chaosblade.io](https://twitter.com/ChaosbladeI)

## Contributing
We welcome every contribution, even if it is just punctuation. See details of [CONTRIBUTING](CONTRIBUTING.md)

## Business Registration
The original intention of our open source project is to lower the threshold for chaos engineering to be implemented in enterprises, so we highly value the use of the project in enterprises. Welcome everyone here [ISSUE](https://github.com/chaosblade-io/chaosblade/issues/32). After registration, you will be invited to join the corporate mail group to discuss the problems encountered by Chaos Engineering in the landing of the company and share the landing experience.

## License
Chaosblade-box is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for the full license text.

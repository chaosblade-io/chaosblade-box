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

## Run Application
If you already have MySQL installed, you need to create a schema named `chaosblade`, if you don't have MySQL installed, you can run it via Docker, run method is as follows:
```bash
docker run -d -it -p 3306:3306 \
			-e MYSQL_DATABASE=chaosblade \
			-e MYSQL_ROOT_PASSWORD=DATASOURCE_PASSWORD \
			--name mysql-5.6 mysql:5.6 \
            --character-set-server=utf8mb4 \
            --collation-server=utf8mb4_unicode_ci \
            --default-time_zone='+8:00'
```
Notes: You must replace the follow parameters: DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD

Then run the application, run method is as follows:

```bash
nohup java -Duser.timezone=Asia/Shanghai -jar chaosblade-box-web-0.4.2.jar --spring.datasource.url=DATASOURCE_URL --spring.datasource.username=DATASOURCE_USERNAME --spring.datasource.password=DATASOURCE_PASSWORD > chaosblade-box.log 2>&1 &
```

You can use a browser to access the http://127.0.0.1:8080 website to use the platform.

If you're deployed on kubernetes, the usage method is as follows:

```bash
helm install chaosblade-box chaosblade-box-0.4.2.tgz --set spring.datasource.password=DATASOURCE_PASSWORD --namespace chaosblade
```

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

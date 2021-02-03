![logo](https://chaosblade.oss-cn-hangzhou.aliyuncs.com/doc/image/chaosblade-logo.png)  

## Introduction
chaos-platform a chaos engineering platform with rich scenes

## Compile
### mysql
````shell script
make mysql mysql.password=${mysql.password}
````

### build with docker
````shell script
make build mysql.url=${mysql.url} mysql.username=${mysql.username} mysql.password=${mysql.password}
````

### build
````shell script
mvn clean package -Dmaven.test.skip=true -Dmysql.url=${mysql.url} -Dmysql.username=${mysql.username} -Dmysql.password=${mysql.password}
````

## run
````shell script
nohup java -Xdebug -Duser.timezone=Asia/Shanghai -jar ./chaos-platform-web/target/chaos-platform-web-0.0.1-SNAPSHOT.jar > chaos-platform.log 2>&1 &
````

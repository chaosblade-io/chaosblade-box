![logo](https://chaosblade.oss-cn-hangzhou.aliyuncs.com/doc/image/chaosblade-logo.png)  

## Introduction
chaosbox a chaos engineering platform with rich scenes

## Compile
### mysql
````shell script
make mysql mysql.password=${mysql.password}
````

### build frontend
````shell script
make build_fe
````

### build with docker
````shell script
make build mysql.url=${mysql.url} mysql.username=${mysql.username} mysql.password=${mysql.password}
````

### build
````shell script
mvn clean package -Dmaven.test.skip=true -Dmysql.url=${mysql.url} -Dmysql.username=${mysql.username} -Dmysql.password=${mysql.password}
````
````shell script
mvn clean package -Dmaven.test.skip=true -Dmysql.url= -Dmysql.username= -Dmysql.password=
````

## run
````shell script
nohup java -Xdebug -Duser.timezone=Asia/Shanghai -jar chaosbox-web-0.3.0.jar > chaosbox.log 2>&1 &
````
````shell script
nohup java -Xdebug -Duser.timezone=Asia/Shanghai -jar chaosbox-web-0.3.0.jar --spring.datasource.url= --spring.datasource.username= ---spring.datasource.password= > chaosbox.log 2>&1 &
````



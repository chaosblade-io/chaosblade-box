.PHONY: build clean

mysql:
	docker run --rm -d -it \
			-p 3306:3306 \
			-e MYSQL_ROOT_PASSWORD=$(mysql.password) \
			-v $(shell pwd)/sql/:/docker-entrypoint-initdb.d/ \
			--name mysql-5.6 mysql:5.6 \
            --character-set-server=utf8mb4 \
            --collation-server=utf8mb4_unicode_ci \
            --default-time_zone='+8:00'

build:
	docker run -it \
   			-v $(shell pwd):/root/ \
   			-w /root/ \
   			-v $(shell echo -n ${HOME})/.m2:/root/.m2 \
    		maven:3.6.3-openjdk-8 mvn clean package -Dmaven.test.skip=true -Dmysql.url=$(mysql.url) -Dmysql.username=$(mysql.username) -Dmysql.password=$(mysql.password)

app:
	docker run -it -d --rm \
    		--name chaos-platform \
    		-v $(shell pwd)/chaos-platform-web/target:/root/ \
    		-v /etc/localtime:/etc/localtime:ro \
    		-p 8080:8080 \
    		-p 8000:8000 \
    		openjdk:8 \
    		java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000,suspend=n -Duser.timezone=Asia/Shanghai -jar /root/chaos-platform-web-0.0.1-SNAPSHOT.jar



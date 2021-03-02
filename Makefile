.PHONY: build clean

export CHAOS_PLATFORM_VERSION=0.2.0
SRC_ROOT=$(shell pwd)

# chaos-platform-fe
CHAOS_PLATFORM_FE=https://github.com/chaosblade-io/chaos-platform-fe.git
CHAOS_PLATFORM_FE_BRANCH=main

mysql:
	docker run --rm -d -it \
			-p 3306:3306 \
			-e MYSQL_ROOT_PASSWORD=$(mysql.password) \
			-v $(shell pwd)/sql/:/docker-entrypoint-initdb.d/ \
			--name mysql-5.6 mysql:5.6 \
            --character-set-server=utf8mb4 \
            --collation-server=utf8mb4_unicode_ci \
            --default-time_zone='+8:00'

build_fe:
	rm -rf ./cache
	rm -rf chaos-platform-web/src/main/resources/web/build/*
	mkdir -p ./cache/chaos-platform-fe
	git clone -b $(CHAOS_PLATFORM_FE_BRANCH) $(CHAOS_PLATFORM_FE) ./cache/chaos-platform-fe
	cd cache/chaos-platform-fe && yarn add react-app-rewired && yarn build
	cp -r cache/chaos-platform-fe/build/* $(SRC_ROOT)/chaos-platform-web/src/main/resources/web/build

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
    		java -Duser.timezone=Asia/Shanghai -jar /root/chaos-platform-web-${CHAOS_PLATFORM_VERSION}.jar

build_image:
	docker build --rm -t chaos-platform:${CHAOS_PLATFORM_VERSION} .



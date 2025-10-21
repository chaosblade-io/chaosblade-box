# Copyright 2025 The ChaosBlade Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

.PHONY: build clean

export CHAOS_PLATFORM_VERSION=1.0.5
SRC_ROOT=$(shell pwd)

# chaosblade-box-fe
CHAOS_PLATFORM_FE=git@github.com:chaosblade-io/chaosblade-box-fe.git
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
	rm -rf chaosblade-box-starter/src/main/resources/build/*
	mkdir -p ./cache/chaosblade-box-fe
	git clone -b $(CHAOS_PLATFORM_FE_BRANCH) $(CHAOS_PLATFORM_FE) ./cache/chaosblade-box-fe
	cd cache/chaosblade-box-fe && npm install && npm run build
	cp -r cache/chaosblade-box-fe/dist/* $(SRC_ROOT)/chaosblade-box-starter/src/main/resources/build

build:
	docker run -it \
   			-v $(shell pwd):/root/ \
   			-w /root/ \
   			-v $(shell echo -n ${HOME})/.m2:/root/.m2 \
    		maven:3.6.3-openjdk-8 mvn clean package -Dmaven.test.skip=true -Dmysql.url=$(mysql.url) -Dmysql.username=$(mysql.username) -Dmysql.password=$(mysql.password)

app:
	docker run -it -d --rm \
    		--name chaosblade-box \
    		-v $(shell pwd)/chaosblade-box-starter/target:/root/ \
    		-v /etc/localtime:/etc/localtime:ro \
    		-p 8080:8080 \
    		-p 8000:8000 \
    		openjdk:8 \
    		java -Duser.timezone=Asia/Shanghai -jar /root/chaosblade-box-${CHAOS_PLATFORM_VERSION}.jar

build_image:
	docker build --rm -t ghcr.io/chaosblade-io/chaosblade-box:${CHAOS_PLATFORM_VERSION} .

.PHONY: license-check
license-check:
	@echo "Checking license headers..."
	docker run -it --rm -v $(shell pwd):/github/workspace ghcr.io/korandoru/hawkeye check

.PHONY: license-format
license-format:
	@echo "Formatting license headers..."
	docker run -it --rm -v $(shell pwd):/github/workspace ghcr.io/korandoru/hawkeye format

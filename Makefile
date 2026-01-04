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

export CHAOS_PLATFORM_VERSION=1.1.0
SRC_ROOT=$(shell pwd)
CACHE_DIR=$(SRC_ROOT)/cache
FE_CACHE_DIR=$(CACHE_DIR)/chaosblade-box-fe
BUILD_DIR=$(SRC_ROOT)/chaosblade-box-starter/src/main/resources/build

# chaosblade-box-fe
CHAOS_PLATFORM_FE=git@github.com:chaosblade-io/chaosblade-box-fe.git
CHAOS_PLATFORM_FE_BRANCH=fix-box-196

build_fe:
	rm -rf $(CACHE_DIR)
	rm -rf $(BUILD_DIR)/*
	mkdir -p $(FE_CACHE_DIR)
	git clone -b $(CHAOS_PLATFORM_FE_BRANCH) $(CHAOS_PLATFORM_FE) $(FE_CACHE_DIR)
	cd $(FE_CACHE_DIR) && npm install && npm run build
	cp -r $(FE_CACHE_DIR)/dist/* $(BUILD_DIR)

build: build_fe
	mvn clean package -Dmaven.test.skip=true

clean:
	@echo "Cleaning build artifacts..."
	rm -rf $(CACHE_DIR)
	rm -rf $(BUILD_DIR)/*
	find $(SRC_ROOT) -type d -name "target" -exec rm -rf {} + 2>/dev/null || true
	@echo "Clean completed."

.PHONY: license-check
license-check:
	@echo "Checking license headers..."
	docker run -it --rm -v $(SRC_ROOT):/github/workspace ghcr.io/korandoru/hawkeye check

.PHONY: license-format
license-format:
	@echo "Formatting license headers..."
	docker run -it --rm -v $(SRC_ROOT):/github/workspace ghcr.io/korandoru/hawkeye format

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
CHAOS_PLATFORM_FE_BRANCH ?= fix-box-196

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
	rm -rf $(HELM_PACKAGE_DIR)
	@echo "Clean completed."

.PHONY: license-check
license-check:
	@echo "Checking license headers..."
	docker run -it --rm -v $(SRC_ROOT):/github/workspace ghcr.io/korandoru/hawkeye check

.PHONY: license-format
license-format:
	@echo "Formatting license headers..."
	docker run -it --rm -v $(SRC_ROOT):/github/workspace ghcr.io/korandoru/hawkeye format

# Docker image build variables
IMAGE_NAME ?= chaosblade-box
IMAGE_REGISTRY ?= 
# Get version from pom.xml if VERSION is not set
# Extract version from the root project's version tag in pom.xml (first <version> tag after <packaging>pom</packaging>)
VERSION ?= $(shell sed -n 's/.*<version>\([^<]*\)<\/version>.*/\1/p' $(SRC_ROOT)/pom.xml | head -1 | tr -d ' ')

# Helm chart variables
HELM_CHART_DIR=$(SRC_ROOT)/deploy/chaosblade-box
HELM_CHART_NAME=chaosblade-box
HELM_PACKAGE_DIR=$(SRC_ROOT)/dist

.PHONY: docker-build
docker-build:
	@echo "Building Docker image..."
	@echo "Image: $(IMAGE_REGISTRY)$(IMAGE_NAME):$(VERSION)"
	@if [ ! -f "$(SRC_ROOT)/chaosblade-box-starter/target/chaosblade-box-$(VERSION).jar" ]; then \
		echo "Error: JAR file not found: chaosblade-box-starter/target/chaosblade-box-$(VERSION).jar"; \
		echo "Please run 'make build' first to build the JAR file."; \
		exit 1; \
	fi
	docker build \
		--build-arg VERSION=$(VERSION) \
		--build-arg JAR_FILE=chaosblade-box-$(VERSION).jar \
		-t $(IMAGE_NAME):$(VERSION) \
		-t $(IMAGE_NAME):latest \
		-f $(SRC_ROOT)/Dockerfile \
		$(SRC_ROOT)
	@if [ -n "$(IMAGE_REGISTRY)" ]; then \
		echo "Tagging image with registry prefix..."; \
		docker tag $(IMAGE_NAME):$(VERSION) $(IMAGE_REGISTRY)$(IMAGE_NAME):$(VERSION); \
		docker tag $(IMAGE_NAME):latest $(IMAGE_REGISTRY)$(IMAGE_NAME):latest; \
	fi
	@echo "Docker image built successfully: $(IMAGE_NAME):$(VERSION)"

.PHONY: docker-push
docker-push:
	@if [ -z "$(IMAGE_REGISTRY)" ]; then \
		echo "Error: IMAGE_REGISTRY is not set. Please set it to push the image."; \
		echo "Example: make docker-push IMAGE_REGISTRY=registry.example.com/"; \
		exit 1; \
	fi
	@echo "Pushing Docker image to $(IMAGE_REGISTRY)$(IMAGE_NAME):$(VERSION)..."
	docker push $(IMAGE_REGISTRY)$(IMAGE_NAME):$(VERSION)
	docker push $(IMAGE_REGISTRY)$(IMAGE_NAME):latest
	@echo "Docker image pushed successfully"

.PHONY: helm-update-version
helm-update-version:
	@echo "Updating Helm chart version to $(VERSION)..."
	@if [ ! -f "$(HELM_CHART_DIR)/Chart.yaml" ]; then \
		echo "Error: Chart.yaml not found at $(HELM_CHART_DIR)/Chart.yaml"; \
		exit 1; \
	fi
	@sed -i.bak 's/^version:.*/version: $(VERSION)/' $(HELM_CHART_DIR)/Chart.yaml
	@sed -i.bak 's/^appVersion:.*/appVersion: "$(VERSION)"/' $(HELM_CHART_DIR)/Chart.yaml
	@if [ -f "$(HELM_CHART_DIR)/Chart.yaml.bak" ]; then rm $(HELM_CHART_DIR)/Chart.yaml.bak; fi
	@if [ -f "$(HELM_CHART_DIR)/values.yaml" ]; then \
		awk -v version="$(VERSION)" '/^box:/ { in_box=1 } /^[a-z]/ && !/^box:/ { in_box=0 } in_box && /^[[:space:]]+version:/ { gsub(/[0-9]+\.[0-9]+\.[0-9]+/, version); } { print }' $(HELM_CHART_DIR)/values.yaml > $(HELM_CHART_DIR)/values.yaml.tmp && \
		mv $(HELM_CHART_DIR)/values.yaml.tmp $(HELM_CHART_DIR)/values.yaml; \
	fi
	@echo "Helm chart version updated successfully"

.PHONY: helm-package
helm-package: helm-update-version
	@echo "Packaging Helm chart..."
	@if ! command -v helm >/dev/null 2>&1; then \
		echo "Error: helm command not found. Please install Helm first."; \
		echo "Visit https://helm.sh/docs/intro/install/ for installation instructions."; \
		exit 1; \
	fi
	@if [ ! -d "$(HELM_CHART_DIR)" ]; then \
		echo "Error: Helm chart directory not found: $(HELM_CHART_DIR)"; \
		exit 1; \
	fi
	@mkdir -p $(HELM_PACKAGE_DIR)
	@helm package $(HELM_CHART_DIR) --destination $(HELM_PACKAGE_DIR) --version $(VERSION) --app-version $(VERSION)
	@echo "Helm chart packaged successfully: $(HELM_PACKAGE_DIR)/$(HELM_CHART_NAME)-$(VERSION).tgz"

.PHONY: helm-lint
helm-lint:
	@echo "Linting Helm chart..."
	@if ! command -v helm >/dev/null 2>&1; then \
		echo "Error: helm command not found. Please install Helm first."; \
		exit 1; \
	fi
	@helm lint $(HELM_CHART_DIR)
	@echo "Helm chart lint completed"

.PHONY: helm-clean
helm-clean:
	@echo "Cleaning Helm package artifacts..."
	@rm -rf $(HELM_PACKAGE_DIR)
	@echo "Helm clean completed"

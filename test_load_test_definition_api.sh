#!/bin/bash

# 压测定义 API 测试脚本
# 用于验证压测定义管理功能的基本 API 调用

BASE_URL="http://localhost:8080/chaos"

echo "=== 压测定义 API 测试 ==="

# 1. 创建压测定义
echo "1. 创建压测定义..."
CREATE_RESPONSE=$(curl -s -X POST "${BASE_URL}/CreateLoadTestDefinition" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试压测定义",
    "engineType": "JMETER",
    "endpoint": "http://example.com/api",
    "entry": "URL",
    "namespace": "default",
    "urlCase": {
      "method": "GET",
      "path": "/api/test",
      "headers": {
        "Content-Type": "application/json"
      }
    }
  }')

echo "创建响应: $CREATE_RESPONSE"

# 提取定义ID（假设返回格式为 {"success":true,"result":"definition_id"}）
DEFINITION_ID=$(echo $CREATE_RESPONSE | grep -o '"result":"[^"]*"' | cut -d'"' -f4)
echo "定义ID: $DEFINITION_ID"

if [ -z "$DEFINITION_ID" ]; then
  echo "创建失败，无法获取定义ID"
  exit 1
fi

# 2. 查询压测定义详情
echo -e "\n2. 查询压测定义详情..."
GET_RESPONSE=$(curl -s -X POST "${BASE_URL}/GetLoadTestDefinition" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"$DEFINITION_ID\",\"namespace\":\"default\"}")

echo "查询响应: $GET_RESPONSE"

# 3. 分页查询压测定义列表
echo -e "\n3. 分页查询压测定义列表..."
QUERY_RESPONSE=$(curl -s -X POST "${BASE_URL}/QueryLoadTestDefinitions" \
  -H "Content-Type: application/json" \
  -d '{
    "pageNum": 1,
    "pageSize": 10,
    "namespace": "default"
  }')

echo "分页查询响应: $QUERY_RESPONSE"

# 4. 查询所有压测定义
echo -e "\n4. 查询所有压测定义..."
LIST_RESPONSE=$(curl -s -X POST "${BASE_URL}/ListAllLoadTestDefinitions" \
  -H "Content-Type: application/json" \
  -d '{"namespace":"default"}')

echo "列表查询响应: $LIST_RESPONSE"

# 5. 更新压测定义
echo -e "\n5. 更新压测定义..."
UPDATE_RESPONSE=$(curl -s -X POST "${BASE_URL}/UpdateLoadTestDefinition" \
  -H "Content-Type: application/json" \
  -d "{
    \"id\": \"$DEFINITION_ID\",
    \"name\": \"更新后的压测定义\",
    \"endpoint\": \"http://updated-example.com/api\",
    \"namespace\": \"default\"
  }")

echo "更新响应: $UPDATE_RESPONSE"

# 6. 删除压测定义
echo -e "\n6. 删除压测定义..."
DELETE_RESPONSE=$(curl -s -X POST "${BASE_URL}/DeleteLoadTestDefinition" \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"$DEFINITION_ID\",\"namespace\":\"default\"}")

echo "删除响应: $DELETE_RESPONSE"

echo -e "\n=== 测试完成 ==="

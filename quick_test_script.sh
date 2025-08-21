#!/bin/bash

# 压测策略管理功能快速测试脚本
# 使用curl命令测试所有API接口

BASE_URL="http://localhost:7001"
NAMESPACE="default"

echo "=== 压测策略管理功能测试 ==="
echo "基础URL: $BASE_URL"
echo "命名空间: $NAMESPACE"
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试结果统计
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# 测试函数
test_api() {
    local test_name="$1"
    local method="$2"
    local url="$3"
    local data="$4"
    local expected_success="$5"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -e "${YELLOW}测试 $TOTAL_TESTS: $test_name${NC}"
    
    if [ "$method" = "POST" ] && [ -n "$data" ]; then
        response=$(curl -s -X POST "$url" \
            -H "Content-Type: application/json" \
            -d "$data")
    else
        response=$(curl -s -X POST "$url")
    fi
    
    echo "请求: $method $url"
    if [ -n "$data" ]; then
        echo "数据: $data"
    fi
    echo "响应: $response"
    
    # 检查响应是否包含success字段
    success=$(echo "$response" | grep -o '"success":[^,}]*' | cut -d':' -f2 | tr -d ' ')
    
    if [ "$success" = "$expected_success" ]; then
        echo -e "${GREEN}✓ 测试通过${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        
        # 如果是创建策略的测试，提取策略ID
        if [[ "$test_name" == *"创建"* ]]; then
            STRATEGY_ID=$(echo "$response" | grep -o '"result":"[^"]*"' | cut -d'"' -f4)
            echo "策略ID: $STRATEGY_ID"
        fi
    else
        echo -e "${RED}✗ 测试失败 (期望success=$expected_success, 实际success=$success)${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
    echo ""
}

# 检查服务是否启动
echo "检查服务状态..."
health_response=$(curl -s "$BASE_URL/actuator/health" 2>/dev/null || echo "")
if [[ "$health_response" == *"UP"* ]] || [[ "$health_response" == *"status"* ]]; then
    echo -e "${GREEN}✓ 服务正在运行${NC}"
else
    echo -e "${RED}✗ 服务未启动或无法访问${NC}"
    echo "请确保应用已启动并监听端口7001"
    exit 1
fi
echo ""

# 注意：以下测试需要先手动创建压测定义和实验
echo -e "${YELLOW}注意：以下测试需要先创建压测定义和实验${NC}"
echo "请在Postman或其他工具中先创建："
echo "1. 压测定义 (CreateLoadTestDefinition)"
echo "2. 实验 (CreateExperiment)"
echo "然后将返回的ID填入下面的变量中"
echo ""

# 这里需要替换为实际的ID
DEFINITION_ID="ldef_test123456789012345678901234"  # 替换为实际的压测定义ID
EXPERIMENT_ID="1234567890123456789"               # 替换为实际的实验ID

if [ "$DEFINITION_ID" = "ldef_test123456789012345678901234" ]; then
    echo -e "${RED}警告: 请先设置实际的DEFINITION_ID和EXPERIMENT_ID${NC}"
    echo "您可以通过以下方式获取："
    echo "1. 创建压测定义："
    echo "   curl -X POST $BASE_URL/CreateLoadTestDefinition \\"
    echo "        -H 'Content-Type: application/json' \\"
    echo "        -d '{\"name\":\"测试定义\",\"engineType\":\"JMETER\",\"endpoint\":\"http://test.com\",\"entry\":\"URL\",\"namespace\":\"default\"}'"
    echo ""
    echo "2. 创建实验："
    echo "   curl -X POST $BASE_URL/CreateExperiment \\"
    echo "        -H 'Content-Type: application/json' \\"
    echo "        -d '{\"name\":\"测试实验\",\"description\":\"测试\",\"namespace\":\"default\"}'"
    echo ""
    exit 1
fi

echo "使用的测试数据："
echo "压测定义ID: $DEFINITION_ID"
echo "实验ID: $EXPERIMENT_ID"
echo ""

# 开始测试
echo "=== 开始API测试 ==="

# 测试1: 创建压测策略
test_api "创建压测策略" "POST" "$BASE_URL/CreateLoadTestStrategy" \
    "{\"enable\":true,\"definitionId\":\"$DEFINITION_ID\",\"experimentId\":\"$EXPERIMENT_ID\",\"startBeforeFaultSec\":45,\"trafficDurationSec\":600,\"abortOnLoadFailure\":true,\"namespace\":\"$NAMESPACE\"}" \
    "true"

# 如果创建成功，继续其他测试
if [ -n "$STRATEGY_ID" ]; then
    # 测试2: 查询策略详情
    test_api "查询策略详情" "POST" "$BASE_URL/GetLoadTestStrategy?id=$STRATEGY_ID&namespace=$NAMESPACE" \
        "" "true"
    
    # 测试3: 更新策略
    test_api "更新策略" "POST" "$BASE_URL/UpdateLoadTestStrategy" \
        "{\"id\":\"$STRATEGY_ID\",\"enable\":false,\"startBeforeFaultSec\":60,\"trafficDurationSec\":300,\"namespace\":\"$NAMESPACE\"}" \
        "true"
    
    # 测试4: 根据实验ID查询
    test_api "根据实验ID查询" "POST" "$BASE_URL/GetLoadTestStrategyByExperimentId?experimentId=$EXPERIMENT_ID&namespace=$NAMESPACE" \
        "" "true"
    
    # 测试5: 根据定义ID查询
    test_api "根据定义ID查询" "POST" "$BASE_URL/GetLoadTestStrategiesByDefinitionId?definitionId=$DEFINITION_ID&namespace=$NAMESPACE" \
        "" "true"
    
    # 测试6: 分页查询
    test_api "分页查询" "POST" "$BASE_URL/QueryLoadTestStrategies" \
        "{\"pageNum\":1,\"pageSize\":10,\"namespace\":\"$NAMESPACE\"}" \
        "true"
    
    # 测试7: 查询所有策略
    test_api "查询所有策略" "POST" "$BASE_URL/ListAllLoadTestStrategies?namespace=$NAMESPACE" \
        "" "true"
    
    # 测试8: 删除策略
    test_api "删除策略" "POST" "$BASE_URL/DeleteLoadTestStrategy?id=$STRATEGY_ID&namespace=$NAMESPACE" \
        "" "true"
    
    # 测试9: 查询已删除的策略（应该失败）
    test_api "查询已删除的策略" "POST" "$BASE_URL/GetLoadTestStrategy?id=$STRATEGY_ID&namespace=$NAMESPACE" \
        "" "false"
else
    echo -e "${RED}创建策略失败，跳过后续测试${NC}"
fi

# 异常场景测试
echo "=== 异常场景测试 ==="

# 测试10: 查询不存在的策略
test_api "查询不存在的策略" "POST" "$BASE_URL/GetLoadTestStrategy?id=lstrategy_nonexistent&namespace=$NAMESPACE" \
    "" "false"

# 测试11: 使用不存在的定义ID创建策略
test_api "使用不存在的定义ID" "POST" "$BASE_URL/CreateLoadTestStrategy" \
    "{\"enable\":true,\"definitionId\":\"ldef_nonexistent\",\"experimentId\":\"9999999999999999999\",\"startBeforeFaultSec\":45,\"trafficDurationSec\":600,\"abortOnLoadFailure\":true,\"namespace\":\"$NAMESPACE\"}" \
    "false"

# 输出测试结果统计
echo "=== 测试结果统计 ==="
echo -e "总测试数: $TOTAL_TESTS"
echo -e "${GREEN}通过: $PASSED_TESTS${NC}"
echo -e "${RED}失败: $FAILED_TESTS${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}🎉 所有测试通过！${NC}"
    exit 0
else
    echo -e "${RED}❌ 有测试失败，请检查日志${NC}"
    exit 1
fi

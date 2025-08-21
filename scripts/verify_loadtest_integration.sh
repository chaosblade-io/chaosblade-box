#!/bin/bash

# 压测集成功能验证脚本
# 用于验证压测集成功能的编译和基本功能

set -e

echo "=== 压测集成功能验证脚本 ==="
echo "开始时间: $(date)"
echo ""

# 1. 编译验证
echo "1. 编译验证..."
mvn clean compile -q -DskipTests=true
if [ $? -eq 0 ]; then
    echo "✅ 编译成功"
else
    echo "❌ 编译失败"
    exit 1
fi
echo ""

# 2. 检查关键文件是否存在
echo "2. 检查关键文件..."

# 数据库相关文件
files=(
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/model/LoadTestTaskDO.java"
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/mapper/LoadTestTaskMapper.java"
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/repository/LoadTestTaskRepository.java"
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/repository/impl/LoadTestTaskRepositoryImpl.java"
    
    # 压测引擎客户端
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/infrastructure/loadtest/LoadTestEngineClient.java"
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/infrastructure/loadtest/impl/LoadTestEngineClientImpl.java"
    
    # 压测任务管理器
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/infrastructure/loadtest/LoadTestTaskManager.java"
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/infrastructure/loadtest/impl/LoadTestTaskManagerImpl.java"
    
    # 拦截器
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/infrastructure/experiment/interceptor/LoadTestExperimentTaskInterceptor.java"
    "chaosblade-box-dao/src/main/java/com/alibaba/chaosblade/box/dao/infrastructure/experiment/interceptor/LoadTestExperimentTaskFinishedInterceptor.java"
    
    # 服务层
    "chaosblade-box-service/src/main/java/com/alibaba/chaosblade/box/service/LoadTestTaskService.java"
    "chaosblade-box-service/src/main/java/com/alibaba/chaosblade/box/service/impl/LoadTestTaskServiceImpl.java"
    "chaosblade-box-service/src/main/java/com/alibaba/chaosblade/box/service/LoadTestMonitorService.java"
    "chaosblade-box-service/src/main/java/com/alibaba/chaosblade/box/service/impl/LoadTestMonitorServiceImpl.java"
    
    # 控制器
    "chaosblade-box-starter/src/main/java/com/alibaba/chaosblade/box/controller/LoadTestTaskController.java"
    
    # 配置
    "chaosblade-box-starter/src/main/java/com/alibaba/chaosblade/box/config/LoadTestConfig.java"
    
    # 数据库脚本
    "chaosblade-box-starter/src/main/resources/sql/schema/chaosblade-box-ddl.sql"
)

missing_files=0
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ $file (缺失)"
        missing_files=$((missing_files + 1))
    fi
done

if [ $missing_files -eq 0 ]; then
    echo "✅ 所有关键文件都存在"
else
    echo "❌ 有 $missing_files 个文件缺失"
fi
echo ""

# 3. 检查数据库表定义
echo "3. 检查数据库表定义..."
if grep -q "t_chaos_load_test_task" chaosblade-box-starter/src/main/resources/sql/schema/chaosblade-box-ddl.sql; then
    echo "✅ 压测任务表定义存在"
else
    echo "❌ 压测任务表定义缺失"
fi
echo ""

# 4. 检查配置文件
echo "4. 检查配置文件..."
if grep -q "loadtest:" chaosblade-box-starter/src/main/resources/application.yml; then
    echo "✅ 压测引擎配置存在"
else
    echo "❌ 压测引擎配置缺失"
fi
echo ""

# 5. 检查API接口
echo "5. 检查API接口..."
api_methods=(
    "GetLoadTestTask"
    "GetLoadTestTaskByExperimentTaskId"
    "QueryLoadTestTasks"
    "StopLoadTestTask"
    "GetLoadTestResults"
    "GetLoadTestEvents"
    "SyncLoadTestTaskStatus"
)

missing_apis=0
for method in "${api_methods[@]}"; do
    if grep -q "$method" chaosblade-box-starter/src/main/java/com/alibaba/chaosblade/box/controller/LoadTestTaskController.java; then
        echo "✅ API接口 $method 存在"
    else
        echo "❌ API接口 $method 缺失"
        missing_apis=$((missing_apis + 1))
    fi
done

if [ $missing_apis -eq 0 ]; then
    echo "✅ 所有API接口都存在"
else
    echo "❌ 有 $missing_apis 个API接口缺失"
fi
echo ""

# 6. 检查打包
echo "6. 检查打包..."
if mvn package -q -DskipTests=true > /dev/null 2>&1; then
    echo "✅ 项目打包成功"
    package_success=0
else
    echo "❌ 项目打包失败"
    package_success=1
fi
echo ""

# 7. 检查文档
echo "7. 检查文档..."
doc_files=(
    "docs/LOAD_TEST_INTEGRATION.md"
    "docs/LOAD_TEST_INTEGRATION_TEST_CASES.md"
)

missing_docs=0
for doc_file in "${doc_files[@]}"; do
    if [ -f "$doc_file" ]; then
        echo "✅ $doc_file"
    else
        echo "❌ $doc_file (缺失)"
        missing_docs=$((missing_docs + 1))
    fi
done

if [ $missing_docs -eq 0 ]; then
    echo "✅ 所有文档都存在"
else
    echo "❌ 有 $missing_docs 个文档缺失"
fi
echo ""

# 8. 总结
echo "=== 验证总结 ==="
total_issues=$((missing_files + missing_apis + package_success + missing_docs))

if [ $total_issues -eq 0 ]; then
    echo "🎉 压测集成功能验证通过！"
    echo "✅ 编译成功"
    echo "✅ 所有关键文件存在"
    echo "✅ 数据库表定义正确"
    echo "✅ 配置文件正确"
    echo "✅ API接口完整"
    echo "✅ 项目打包成功"
    echo "✅ 文档完整"
else
    echo "⚠️  压测集成功能验证发现 $total_issues 个问题"
    echo "请检查上述错误信息并修复"
fi

echo ""
echo "结束时间: $(date)"
echo "=== 验证完成 ==="

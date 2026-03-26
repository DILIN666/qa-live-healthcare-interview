#!/bin/bash

# API测试脚本 - 医生列表API
API_URL="http://localhost:8080/api/doctors"

# 测试结果统计
passed=0
failed=0

echo "===================================="
echo "开始测试医生列表API接口"
echo "===================================="

# 函数：执行测试用例
run_test() {
    local test_name="$1"
    local curl_command="$2"
    local expected_status="$3"
    local expected_content="$4"
    
    echo -n "测试: $test_name... "
    
    # 记录开始时间
    start_time=$(date +%s%3N)
    
    # 执行curl命令
    response=$(eval "$curl_command")
    status_code=$(echo "$response" | head -n 1 | cut -d ' ' -f 2)
    response_body=$(echo "$response" | tail -n +2)
    
    # 计算响应时间
    end_time=$(date +%s%3N)
    response_time=$((end_time - start_time))
    
    # 检查状态码
    if [ "$status_code" != "$expected_status" ]; then
        echo "失败"
        echo "  预期状态码: $expected_status, 实际状态码: $status_code"
        echo "  响应内容: $response_body"
        failed=$((failed + 1))
        return
    fi
    
    # 检查响应内容
    if [ -n "$expected_content" ]; then
        if echo "$response_body" | grep -q "$expected_content"; then
            echo -n "通过"
        else
            echo "失败"
            echo "  响应内容不包含预期字符串: $expected_content"
            echo "  响应内容: $response_body"
            failed=$((failed + 1))
            return
        fi
    else
        echo -n "通过"
    fi
    
    # 检查响应时间
    if [ $response_time -gt 500 ]; then
        echo " (响应时间: ${response_time}ms - 超过500ms)"
    else
        echo " (响应时间: ${response_time}ms)"
    fi
    
    passed=$((passed + 1))
}

# 测试场景1: 成功获取医生列表数据的正常请求
run_test "正常请求 - 获取医生列表" "curl -s -w '\n%{http_code}' $API_URL" "200" "success":true

# 测试场景2: 处理不同分页参数的请求
run_test "分页测试 - 第1页，每页10条" "curl -s -w '\n%{http_code}' '$API_URL?page=0&size=10'" "200" "success":true
run_test "分页测试 - 第2页，每页5条" "curl -s -w '\n%{http_code}' '$API_URL?page=1&size=5'" "200" "success":true
run_test "分页测试 - 第3页，每页20条" "curl -s -w '\n%{http_code}' '$API_URL?page=2&size=20'" "200" "success":true

# 测试场景3: 验证排序功能
run_test "排序测试 - 按ID升序" "curl -s -w '\n%{http_code}' '$API_URL?sortBy=id&sortDirection=asc'" "200" "success":true
run_test "排序测试 - 按ID降序" "curl -s -w '\n%{http_code}' '$API_URL?sortBy=id&sortDirection=desc'" "200" "success":true

# 测试场景4: 测试筛选条件
run_test "筛选测试 - 按科室筛选(心内科)" "curl -s -w '\n%{http_code}' '$API_URL?department=心内科'" "200" "心内科"
run_test "筛选测试 - 按活跃状态筛选(在线)" "curl -s -w '\n%{http_code}' '$API_URL?isActive=true'" "200" "isActive":true
run_test "筛选测试 - 组合筛选(心内科+在线)" "curl -s -w '\n%{http_code}' '$API_URL?department=心内科&isActive=true'" "200" "心内科"

# 测试场景5: 处理无效参数的错误情况
run_test "错误测试 - 无效的页码" "curl -s -w '\n%{http_code}' '$API_URL?page=-1'" "200" "success":true
run_test "错误测试 - 无效的每页条数" "curl -s -w '\n%{http_code}' '$API_URL?size=0'" "200" "success":true
run_test "错误测试 - 无效的排序字段" "curl -s -w '\n%{http_code}' '$API_URL?sortBy=invalid'" "200" "success":true

# 测试场景6: 测试单个医生详情
run_test "详情测试 - 存在的医生" "curl -s -w '\n%{http_code}' '$API_URL/dr-zhang-wei'" "200" "张伟医生"
run_test "详情测试 - 不存在的医生" "curl -s -w '\n%{http_code}' '$API_URL/invalid-doctor'" "404" "医生不存在"

echo "===================================="
echo "测试完成"
echo "通过: $passed"
echo "失败: $failed"
echo "===================================="

if [ $failed -eq 0 ]; then
    echo "所有测试用例通过！"
    exit 0
else
    echo "有 $failed 个测试用例失败！"
    exit 1
fi

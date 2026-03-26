# API测试脚本 - 医生列表API
$API_URL = "http://localhost:8080/api/doctors"

# 测试结果统计
$passed = 0
$failed = 0

echo "===================================="
echo "开始测试医生列表API接口"
echo "===================================="

# 函数：执行测试用例
function Run-Test {
    param (
        [string]$testName,
        [string]$url,
        [int]$expectedStatus,
        [string]$expectedContent
    )
    
    Write-Host -NoNewline "测试: $testName... "
    
    # 记录开始时间
    $startTime = Get-Date
    
    try {
        # 执行请求
        $response = Invoke-RestMethod -Uri $url -Method GET -ErrorAction Stop
        $statusCode = 200
        $responseBody = $response | ConvertTo-Json -Depth 10
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $responseBody = $_.ErrorDetails.Message
    }
    
    # 计算响应时间
    $endTime = Get-Date
    $responseTime = ($endTime - $startTime).TotalMilliseconds
    
    # 检查状态码
    if ($statusCode -ne $expectedStatus) {
        Write-Host "失败"
        Write-Host "  预期状态码: $expectedStatus, 实际状态码: $statusCode"
        Write-Host "  响应内容: $responseBody"
        global:$failed++
        return
    }
    
    # 检查响应内容
    if ($expectedContent) {
        if ($responseBody -match $expectedContent) {
            Write-Host -NoNewline "通过"
        } else {
            Write-Host "失败"
            Write-Host "  响应内容不包含预期字符串: $expectedContent"
            Write-Host "  响应内容: $responseBody"
            global:$failed++
            return
        }
    } else {
        Write-Host -NoNewline "通过"
    }
    
    # 检查响应时间
    if ($responseTime -gt 500) {
        Write-Host " (响应时间: $([math]::Round($responseTime, 2))ms - 超过500ms)"
    } else {
        Write-Host " (响应时间: $([math]::Round($responseTime, 2))ms)"
    }
    
    global:$passed++
}

# 测试场景1: 成功获取医生列表数据的正常请求
Run-Test "正常请求 - 获取医生列表" $API_URL 200 '"success":true'

# 测试场景2: 处理不同分页参数的请求
Run-Test "分页测试 - 第1页，每页10条" "$API_URL?page=0&size=10" 200 '"success":true'
Run-Test "分页测试 - 第2页，每页5条" "$API_URL?page=1&size=5" 200 '"success":true'
Run-Test "分页测试 - 第3页，每页20条" "$API_URL?page=2&size=20" 200 '"success":true'

# 测试场景3: 验证排序功能
Run-Test "排序测试 - 按ID升序" "$API_URL?sortBy=id&sortDirection=asc" 200 '"success":true'
Run-Test "排序测试 - 按ID降序" "$API_URL?sortBy=id&sortDirection=desc" 200 '"success":true'

# 测试场景4: 测试筛选条件
Run-Test "筛选测试 - 按科室筛选(心内科)" "$API_URL?department=心内科" 200 '心内科'
Run-Test "筛选测试 - 按活跃状态筛选(在线)" "$API_URL?isActive=true" 200 '"isActive":true'
Run-Test "筛选测试 - 组合筛选(心内科+在线)" "$API_URL?department=心内科&isActive=true" 200 '心内科'

# 测试场景5: 处理无效参数的错误情况
Run-Test "错误测试 - 无效的页码" "$API_URL?page=-1" 200 '"success":true'
Run-Test "错误测试 - 无效的每页条数" "$API_URL?size=0" 200 '"success":true'
Run-Test "错误测试 - 无效的排序字段" "$API_URL?sortBy=invalid" 200 '"success":true'

# 测试场景6: 测试单个医生详情
Run-Test "详情测试 - 存在的医生" "$API_URL/dr-zhang-wei" 200 '张伟医生'
Run-Test "详情测试 - 不存在的医生" "$API_URL/invalid-doctor" 404 '医生不存在'

echo "===================================="
echo "测试完成"
echo "通过: $passed"
echo "失败: $failed"
echo "===================================="

if ($failed -eq 0) {
    echo "所有测试用例通过！"
    exit 0
} else {
    echo "有 $failed 个测试用例失败！"
    exit 1
}

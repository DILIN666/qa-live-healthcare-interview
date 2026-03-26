# API 文档

## 概述

本文档描述了 `qa-service-user` 服务中提供的所有 API 接口，主要用于医生数据的管理和查询。

## 基础信息

- **基础 URL**: `http://localhost:8080/api`
- **数据格式**: JSON
- **字符编码**: UTF-8

## API 接口列表

### 1. 获取医生列表

**接口描述**: 获取医生列表数据，支持分页、排序和筛选。

**请求方式**: `GET`

**请求路径**: `/doctors`

**请求参数**:

| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | int | 否 | 0 | 页码，从0开始 |
| size | int | 否 | 10 | 每页显示数量 |
| sortBy | string | 否 | id | 排序字段 |
| sortDirection | string | 否 | asc | 排序方向，可选值：asc(升序)、desc(降序) |
| department | string | 否 | - | 按科室筛选 |
| isActive | boolean | 否 | - | 按在线状态筛选 |

**响应示例**:

```json
{
  "success": true,
  "data": [
    {
      "id": "doc001",
      "username": "dr-zhang-wei",
      "name": "张伟医生",
      "title": "主任医师",
      "department": "心内科",
      "avatar": "https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg",
      "experience": "15年临床经验",
      "specialties": ["心血管疾病", "高血压", "冠心病"],
      "isActive": true
    }
  ],
  "total": 5,
  "message": "获取医生列表成功"
}
```

---

### 2. 获取单个医生详情

**接口描述**: 根据用户名获取单个医生的详细信息。

**请求方式**: `GET`

**请求路径**: `/doctors/{username}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 医生用户名 |

**响应示例 (成功)**:

```json
{
  "success": true,
  "data": {
    "id": "doc001",
    "username": "dr-zhang-wei",
    "name": "张伟医生",
    "title": "主任医师",
    "department": "心内科",
    "avatar": "https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg",
    "experience": "15年临床经验",
    "specialties": ["心血管疾病", "高血压", "冠心病"],
    "isActive": true
  },
  "message": "获取医生信息成功"
}
```

**响应示例 (失败 - 医生不存在)**:

```json
{
  "success": false,
  "message": "医生不存在"
}
```

---

### 3. 健康检查接口

**接口描述**: 测试服务是否正常运行。

**请求方式**: `GET`

**请求路径**: `/test`

**响应示例**:

```
Hello from qa-service-user!
```

## 错误码说明

| HTTP 状态码 | 说明 |
|-------------|------|
| 200 | 请求成功 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 数据模型

### Doctor (医生)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | string | 医生ID |
| username | string | 用户名 |
| password | string | 密码 (仅存储在数据库中) |
| name | string | 医生姓名 |
| title | string | 职称 |
| department | string | 科室 |
| avatar | string | 头像URL |
| experience | string | 经验描述 |
| isActive | boolean | 是否在线 |

### DoctorSpecialty (医生专业领域)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | int | 主键ID |
| doctorId | string | 医生ID |
| specialty | string | 专业领域名称 |

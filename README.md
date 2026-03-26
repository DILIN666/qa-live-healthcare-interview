# QA Live Healthcare - 在线医疗咨询平台

## 项目概述

QA Live Healthcare 是一个现代化的在线医疗咨询平台，提供医生团队展示、在线咨询等功能。本项目已完成以下核心功能：

- **首页国际化**：支持中英文语言切换
- **医生数据迁移**：从JSON文件迁移到MySQL数据库
- **RESTful API**：提供完整的医生数据管理接口
- **Docker容器化**：快速部署MySQL数据库和phpMyAdmin
- **API测试**：完整的测试脚本覆盖所有接口

## 技术栈

### 前端
- Vue 3
- TypeScript
- Ant Design Vue
- Vue Router
- Vue I18n（国际化）
- Vite

### 后端
- Spring Boot 3
- Spring Data JPA
- MySQL 8.0
- Maven

### DevOps
- Docker
- Docker Compose

## 项目结构

```
qa-live-healthcare-interview/
├── docker-compose.yml          # Docker服务配置
├── mysql-init/                 # 数据库初始化脚本
│   └── init.sql
├── server/                     # 后端服务
│   ├── qa-service-user/        # 用户服务
│   ├── qa-service-question/    # 问题服务
│   └── qa-service-statistic/   # 统计服务
├── web/                        # 前端应用
│   └── qa-web/                 # Web应用
├── docs/                       # 文档
│   └── api.md                  # API文档
├── API_TEST.md                 # API测试说明
├── test-doctor-api.ps1         # PowerShell测试脚本
└── test-doctor-api.sh          # Bash测试脚本
```

## 快速开始

### 环境要求

- **Node.js**: 18.x 或更高版本
- **JDK**: 17 或更高版本
- **Docker**: 20.x 或更高版本
- **Docker Compose**: 2.x 或更高版本
- **Maven**: 3.8.x 或更高版本

### 安装步骤

#### 1. 克隆项目

```bash
git clone https://github.com/your-username/qa-live-healthcare-interview.git
cd qa-live-healthcare-interview
```

#### 2. 启动Docker服务

使用Docker Compose启动MySQL数据库和phpMyAdmin：

```bash
docker-compose up -d
```

服务说明：
- **MySQL**: 监听端口 3306
- **phpMyAdmin**: 访问地址 http://localhost:8080
  - 用户名: root
  - 密码: root
  - 数据库: qa_live_healthcare

#### 3. 启动后端服务

```bash
cd server/qa-service-user
./mvnw spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

#### 4. 启动前端服务

```bash
cd web/qa-web
npm install
npm run dev
```

前端应用将在 `http://localhost:5173` 启动。

## 功能说明

### 1. 首页国际化

- 在页面右上角添加了语言切换下拉菜单
- 支持中文和英文两种语言
- 语言设置保存在localStorage中，刷新页面后保持不变
- 支持平滑过渡效果

### 2. 医生数据管理

- 医生数据已从JSON文件迁移到MySQL数据库
- 支持分页、排序和筛选功能
- 提供完整的RESTful API接口
- 支持按科室、在线状态等条件筛选

### 3. API接口

详细的API文档请参考 [docs/api.md](docs/api.md)。

主要接口：
- `GET /api/doctors` - 获取医生列表（支持分页、排序、筛选）
- `GET /api/doctors/{username}` - 获取单个医生详情
- `GET /api/test` - 健康检查

## 使用方法

### 访问应用

1. **前端应用**: http://localhost:5173
2. **医生页面**: http://localhost:5173/doctors
3. **phpMyAdmin**: http://localhost:8080
4. **后端API**: http://localhost:8080/api

### 运行API测试

详细的测试说明请参考 [API_TEST.md](API_TEST.md)。

**Windows环境**:
```powershell
powershell -ExecutionPolicy Bypass -File test-doctor-api.ps1
```

**Linux/Mac环境**:
```bash
chmod +x test-doctor-api.sh
./test-doctor-api.sh
```

## 配置说明

### 后端配置

后端配置文件位于 `server/qa-service-user/src/main/resources/application.properties`:

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/qa_live_healthcare
spring.datasource.username=root
spring.datasource.password=root

# JPA配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 前端环境变量

可以在 `web/qa-web/.env` 文件中配置API地址：

```env
VITE_API_URL=http://localhost:8080/api/doctors
```

## 常见问题解答

### Q: npm命令无法识别？

A: 请确保已安装Node.js，并将其添加到系统环境变量中。可以通过以下命令验证：
```bash
node --version
npm --version
```

### Q: Docker容器无法启动？

A: 请确保Docker Desktop已启动，并且端口3306和8080没有被占用。

### Q: 后端启动失败，提示数据库连接错误？

A: 请确保：
1. Docker容器已正常启动
2. MySQL数据库已初始化
3. 数据库配置信息正确

### Q: 前端页面无法加载医生数据？

A: 请确保：
1. 后端服务已正常启动
2. 前端API地址配置正确
3. 浏览器控制台没有跨域错误

### Q: 如何重置数据库？

A: 可以通过以下步骤重置数据库：
```bash
docker-compose down -v
docker-compose up -d
```

## 故障排除指南

### 问题1：端口被占用

**症状**: Docker容器启动失败，提示端口已被占用。

**解决方案**:
1. 查找占用端口的进程：
   ```bash
   # Windows
   netstat -ano | findstr :3306
   
   # Linux/Mac
   lsof -i :3306
   ```
2. 停止占用端口的进程，或修改 `docker-compose.yml` 中的端口映射。

### 问题2：数据库连接失败

**症状**: 后端启动时提示 "Communications link failure"。

**解决方案**:
1. 确认MySQL容器正在运行：`docker ps`
2. 检查数据库连接配置
3. 尝试重启Docker容器：`docker-compose restart`

### 问题3：前端跨域问题

**症状**: 浏览器控制台提示 CORS 错误。

**解决方案**:
后端已配置CORS支持，确保 `CorsConfig.java` 正确配置了允许的来源。

### 问题4：测试脚本执行失败

**症状**: API测试脚本无法连接到服务器。

**解决方案**:
1. 确认后端服务已启动
2. 检查测试脚本中的API URL配置
3. 确认防火墙没有阻止请求

## 开发指南

### 添加新的API接口

1. 在 `entity` 包中创建实体类
2. 在 `repository` 包中创建Repository接口
3. 在 `service` 包中创建Service类
4. 在 `controller` 包中创建Controller类

### 添加新的语言

1. 在 `web/qa-web/src/locales` 目录下创建新的语言文件
2. 在 `index.ts` 中注册新语言
3. 在 `AppHeader.vue` 中添加语言切换选项

## 贡献指南

1. Fork本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送 Pull Request

---

**注意**: 本项目为面试演示项目，请勿在生产环境中直接使用。

# 现金缴存系统 (Cash System)

> 基于 Spring Boot 3 的现金缴存业务管理平台，支持设备管理、存款记录、清机任务、日结结算及区块链存证等核心功能。

---

## 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 接口](#api-接口)
- [角色权限](#角色权限)
- [区块链存证](#区块链存证)

---

## 项目简介

现金缴存系统是一套面向银行/金融机构的现金业务管理后端服务，主要功能包括：

- **用户与权限管理**：多角色体系，支持管理员、管理者、终端操作员、清机操作员
- **设备管理**：现金缴存设备的注册、状态维护与查询
- **存款记录**：存款流水的创建、审核与统计分析
- **清机任务**：清机工单的派发、执行与状态跟踪
- **日结结算**：每日资金结算与对账
- **区块链存证**：关键业务数据自动上链，保障数据不可篡改
- **审计日志**：完整的操作记录追踪

---

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Spring Boot | 3.4.12 |
| 编程语言 | Java | 17 |
| 构建工具 | Maven | 3.x |
| ORM 框架 | MyBatis | 3.0.5 |
| 主数据库 | MySQL | 8.x |
| 缓存 | Redis (Lettuce) | — |
| 安全认证 | Spring Security + JWT (JJWT 0.11.5) | — |
| 切面编程 | Spring AOP + AspectJ | — |
| 代码简化 | Lombok | — |
| JSON 处理 | Jackson + JSR310 | — |
| 日志框架 | SLF4J + Logback | — |
| 测试数据库 | H2 | — |

---

## 项目结构

```
src/main/java/com/cashsystem/
├── CashSystemApplication.java      # 启动类
├── config/                         # 配置类
│   ├── SecurityConfig.java         # Spring Security 配置
│   ├── JwtAuthenticationFilter.java# JWT 认证过滤器
│   ├── BlockchainAspect.java       # 区块链存证切面
│   ├── RoleAspect.java             # 角色权限切面
│   ├── RequiresRole.java           # 自定义角色注解
│   └── GlobalExceptionHandler.java # 全局异常处理
├── controller/                     # REST 控制层
│   ├── AuthController.java
│   ├── UserController.java
│   ├── DeviceController.java
│   ├── DepositRecordController.java
│   ├── CleaningTaskController.java
│   ├── BlockchainController.java
│   └── TestController.java
├── service/                        # 业务逻辑层
│   └── impl/                       # 服务实现
├── mapper/                         # 数据访问层（MyBatis）
├── entity/                         # 数据实体类
├── dto/                            # 请求数据传输对象
├── vo/                             # 响应视图对象
└── util/                           # 工具类（JwtUtil、PasswordUtil）

src/main/resources/
├── application.yml                 # 主配置文件
└── mapper/                         # MyBatis XML 映射文件
```

---

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 1. 克隆项目

```bash
git clone <repository-url>
cd cash-system
```

### 2. 初始化数据库

创建数据库并导入初始化脚本：

```sql
CREATE DATABASE cash_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 修改配置

编辑 `src/main/resources/application.yml`，按实际环境修改数据库和 Redis 连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cash_system?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码

  data:
    redis:
      host: localhost
      port: 6379
      password: 你的Redis密码（无密码则留空）
```

### 4. 启动项目

```bash
# 使用 Maven Wrapper
./mvnw spring-boot:run

# 或使用本地 Maven
mvn spring-boot:run
```

服务启动后访问：`http://localhost:8080`

### 5. 验证服务

```bash
curl http://localhost:8080/api/auth/test
```

---

## 配置说明

### JWT 配置

```yaml
jwt:
  secret: "cash-system-secret-key-2025-spring-boot-jwt-token"  # 生产环境请替换为强密钥
  expiration: 86400000  # Token 有效期，单位毫秒（默认 24 小时）
  header: "Authorization"
```

### 数据库连接池（HikariCP）

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 30000   # 连接超时 30s
      maximum-pool-size: 20       # 最大连接数
      minimum-idle: 5             # 最小空闲连接数
```

### 日志

日志文件输出至 `logs/cash-system.log`，支持按日期滚动归档。

---

## API 接口

所有接口统一响应格式：

```json
{
  "success": true,
  "message": "操作成功",
  "data": {},
  "timestamp": 1700000000000
}
```

### 认证

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/auth/login` | 用户登录，返回 JWT Token | 公开 |
| GET  | `/api/auth/test`  | 服务健康检查 | 公开 |

**登录请求示例：**

```json
POST /api/auth/login
{
  "account": "admin",
  "password": "123456"
}
```

**登录响应示例：**

```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "admin",
    "role": "ADMIN"
  }
}
```

后续请求在 Header 中携带 Token：

```
Authorization: Bearer <token>
```

---

### 用户管理

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET    | `/api/users/me`           | 获取当前登录用户信息 | 所有角色 |
| GET    | `/api/users/{id}`         | 根据 ID 查询用户 | ADMIN / MANAGER |
| POST   | `/api/users`              | 创建用户 | ADMIN |
| PUT    | `/api/users/{id}`         | 更新用户信息 | ADMIN / MANAGER |
| PUT    | `/api/users/{id}/password`| 修改密码 | 所有角色 |

---

### 设备管理

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET  | `/api/devices`              | 获取设备列表 | ADMIN / MANAGER |
| GET  | `/api/devices/{id}`         | 根据 ID 查询设备 | ADMIN / MANAGER |
| GET  | `/api/devices/code/{code}`  | 根据设备编号查询 | ADMIN / MANAGER |
| POST | `/api/devices`              | 创建设备 | ADMIN / MANAGER |
| PUT  | `/api/devices/{id}`         | 更新设备信息 | ADMIN / MANAGER |
| PUT  | `/api/devices/{id}/status`  | 更新设备状态 | ADMIN / MANAGER |

---

### 存款记录

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/deposit-records`                        | 创建存款记录 | TERMINAL_OPERATOR / ADMIN / MANAGER |
| GET  | `/api/deposit-records/{id}`                   | 根据 ID 查询 | TERMINAL_OPERATOR / ADMIN / MANAGER |
| GET  | `/api/deposit-records/no/{depositNo}`         | 根据流水号查询 | TERMINAL_OPERATOR / ADMIN / MANAGER |
| GET  | `/api/deposit-records/operator/{operatorId}`  | 查询操作员记录 | TERMINAL_OPERATOR / ADMIN / MANAGER |
| GET  | `/api/deposit-records/statistics`             | 存款统计分析 | TERMINAL_OPERATOR / ADMIN / MANAGER |
| PUT  | `/api/deposit-records/{id}/audit-status`      | 更新审核状态 | TERMINAL_OPERATOR / ADMIN / MANAGER |

---

### 清机任务

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/cleaning-tasks`           | 创建清机任务 | CLEANING_OPERATOR / ADMIN / MANAGER |
| GET  | `/api/cleaning-tasks/{id}`      | 根据 ID 查询 | CLEANING_OPERATOR / ADMIN / MANAGER |
| GET  | `/api/cleaning-tasks/pending`   | 查询待处理任务 | CLEANING_OPERATOR / ADMIN / MANAGER |
| PUT  | `/api/cleaning-tasks/{id}/status` | 更新任务状态 | CLEANING_OPERATOR / ADMIN / MANAGER |

---

### 区块链存证

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/blockchain/store`        | 手动触发存证 | 需认证 |
| POST | `/api/blockchain/verify`       | 验证数据完整性 | 需认证 |
| GET  | `/api/blockchain/record`       | 查询存证记录 | 需认证 |
| GET  | `/api/blockchain/stats`        | 获取存证统计 | 需认证 |
| POST | `/api/blockchain/retry/{id}`   | 重试失败存证 | 需认证 |
| GET  | `/api/blockchain/test`         | 测试区块链连接 | 需认证 |

---

## 角色权限

系统内置四种角色，通过 JWT Token 中的 `role` 字段标识：

| 角色 | 标识 | 说明 |
|------|------|------|
| 管理员 | `ADMIN` | 最高权限，可访问所有接口 |
| 管理者 | `MANAGER` | 可管理设备、用户及查看所有记录 |
| 终端操作员 | `TERMINAL_OPERATOR` | 负责存款记录的创建与查询 |
| 清机操作员 | `CLEANING_OPERATOR` | 负责清机任务的执行与更新 |

权限控制通过两种方式实现：
1. **Spring Security** — 在 `SecurityConfig` 中配置路由级别的角色限制
2. **自定义 `@RequiresRole` 注解** — 在方法级别通过 AOP 切面进行细粒度控制

---

## 区块链存证

系统通过 AOP 切面（`BlockchainAspect`）在以下业务操作完成后**自动触发存证**，无需手动调用：

| 触发时机 | 存证类型 |
|----------|----------|
| 存款记录创建成功 | `DEPOSIT` |
| 清机任务创建成功 | `CLEANING` |
| 日结记录创建成功 | `SETTLEMENT` |

存证数据包含业务数据的哈希值与交易哈希（`txHash`），可通过 `/api/blockchain/verify` 接口随时验证数据完整性。

> 当前版本使用模拟区块链实现（`SimulationBlockchainServiceImpl`），可替换为真实区块链网络对接。

---

## 开发说明

### 运行测试

```bash
./mvnw test
```

### 打包构建

```bash
./mvnw clean package -DskipTests
java -jar target/cash-system-0.0.1-SNAPSHOT.jar
```

### 日志级别调整

修改 `application.yml` 中的日志配置：

```yaml
logging:
  level:
    com.cashsystem: debug   # 开发环境建议 debug，生产环境改为 info
```

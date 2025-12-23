# 一体化民宿管理系统

## 项目简介

这是一个基于Spring Boot的一体化民宿管理系统，包含宾客官网和员工管理端两个部分。系统实现了房间管理、预订管理、支付处理、统计报表等核心功能，并预留了OTA平台、公安部门、支付网关等外部接口（模拟实现）。

## 技术栈

- **后端**: Spring Boot 3.5.6, Spring Security, JPA, MySQL, JWT
- **前端**: HTML, CSS, JavaScript (原生)
- **数据库**: MySQL 8.0+

## 功能特性

### 宾客端功能
- 房间浏览和搜索
- 在线预订
- 订单管理
- 在线支付（模拟）
- 个人中心

### 管理端功能
- 员工登录（支持多种角色：管理员、经理、前台、房务）
- 预订管理（查看、办理入住/退房）
- 房间管理（增删改查）
- 宾客管理
- 统计报表（今日统计、日期范围统计）
- 员工管理

### 外部接口（模拟实现）
- OTA平台集成接口
- 公安部门接口（身份证验证、住宿登记上报）
- 支付网关接口

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE hotel_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `HotelSystem/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hotel_system?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password  # 修改为你的数据库密码
```

### 运行项目

1. 进入项目目录：
```bash
cd HotelSystem
```

2. 使用Maven运行：
```bash
mvn spring-boot:run
```

或者打包后运行：
```bash
mvn clean package
java -jar target/HotelSystem-0.0.1-SNAPSHOT.jar
```

3. 访问系统：
- 首页: http://localhost:8080/
- 宾客端: http://localhost:8080/guest/rooms.html
- 管理端: http://localhost:8080/admin/login.html

### 默认账号

系统启动时会自动创建管理员账号：
- 用户名: `admin`
- 密码: `admin123`
- 角色: `ADMIN`

## 项目结构

```
HotelSystem/
├── src/
│   ├── main/
│   │   ├── java/com/hotelsystem/
│   │   │   ├── controller/          # 控制器层
│   │   │   ├── service/            # 服务层
│   │   │   ├── repository/         # 数据访问层
│   │   │   ├── entity/             # 实体类
│   │   │   ├── dto/                # 数据传输对象
│   │   │   ├── security/           # 安全配置
│   │   │   ├── config/             # 配置类
│   │   │   └── service/external/   # 外部接口服务
│   │   └── resources/
│   │       ├── static/             # 静态资源（前端页面）
│   │       │   ├── guest/          # 宾客端页面
│   │       │   ├── admin/          # 管理端页面
│   │       │   ├── css/            # 样式文件
│   │       │   └── js/             # JavaScript文件
│   │       └── application.yml     # 配置文件
│   └── test/                       # 测试文件
└── pom.xml                         # Maven配置
```

## API接口

### 认证接口
- `POST /auth/login` - 登录
- `POST /guests` - 宾客注册

### 房间接口
- `GET /rooms` - 获取所有房间
- `GET /rooms/{id}` - 获取房间详情
- `POST /rooms` - 创建房间（需管理员权限）
- `PUT /rooms/{id}` - 更新房间（需管理员权限）
- `DELETE /rooms/{id}` - 删除房间（需管理员权限）

### 预订接口
- `GET /reservations` - 获取所有预订（需管理员权限）
- `GET /reservations/me` - 获取我的预订（宾客）
- `POST /reservations` - 创建预订
- `POST /reservations/{id}/cancel` - 取消预订

### 前台操作接口
- `POST /frontdesk/checkin/{reservationId}` - 办理入住（需前台权限）
- `POST /frontdesk/checkout/{reservationId}` - 办理退房（需前台权限）

### 统计接口
- `GET /api/statistics/today` - 今日统计（需管理员权限）
- `GET /api/statistics/date-range` - 日期范围统计（需管理员权限）

### 外部接口（模拟）
- `POST /api/external/ota/sync-reservation` - 同步预订到OTA平台
- `POST /api/external/security/verify-id-card` - 验证身份证
- `POST /api/external/payment/create-order` - 创建支付订单

## 角色权限

- **ADMIN**: 管理员，拥有所有权限
- **MANAGER**: 经理，可以管理预订、房间、查看报表
- **RECEPTIONIST**: 前台，可以办理入住/退房、查看预订
- **HOUSEKEEPING**: 房务，可以查看房间状态
- **GUEST**: 宾客，可以浏览房间、预订、查看自己的订单

## 注意事项

1. **外部接口**: 所有外部接口（OTA平台、公安部门、支付网关）都是模拟实现，不会真实调用外部API。

2. **支付流程**: 支付功能为模拟实现，实际使用时需要集成真实的支付网关。

3. **安全**: 生产环境请修改JWT密钥和数据库密码，并启用HTTPS。

4. **数据初始化**: 系统启动时会自动创建管理员账号，但不会初始化房间数据，需要手动添加。

## 开发说明

### 添加新功能

1. 在 `entity` 包中创建实体类
2. 在 `repository` 包中创建Repository接口
3. 在 `service` 包中创建Service类
4. 在 `controller` 包中创建Controller类
5. 如需前端页面，在 `static` 目录下创建HTML文件

### 数据库迁移

系统使用JPA自动创建表结构（`ddl-auto: update`），首次运行时会自动创建所有表。

## 许可证

本项目仅供学习和参考使用。

## 联系方式

如有问题或建议，请联系开发团队。


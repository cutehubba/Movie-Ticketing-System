# 电影票务系统

## 项目简介
这是一个全栈电影票务系统，包含前端和后端。后端基于 Spring Boot 和 MySQL，提供 RESTful API 接口；前端使用 Vue.js 构建，支持用户注册、登录、查询电影、影院、影厅信息，以及在线购票和支付功能。系统为管理员和普通用户提供了不同的界面和功能。

## 功能模块
### 后端
- **用户管理**：用户注册、登录、信息更新、删除用户。
- **电影管理**：添加、查询、更新、删除电影信息。
- **影院管理**：添加、查询、更新、删除影院信息。
- **影厅管理**：添加、查询、更新、删除影厅信息，以及座位管理。
- **排片管理**：添加、查询、更新、删除排片信息。
- **订单管理**：创建订单、查询订单、更新订单状态、删除订单。
- **支付管理**：创建支付订单、更新支付状态、处理支付回调。

### 前端
- **用户界面**：普通用户可以浏览电影、影院信息，进行购票和查看订单。
- **管理员界面**：管理员可以管理电影、影院、影厅、排片信息，查看所有订单。
- **响应式设计**：支持多种设备，提供良好的用户体验。

## 技术栈
### 后端
- **框架**：Spring Boot、Spring MVC、Spring Security、Spring Data JPA、MyBatis
- **数据库**：MySQL
- **其他**：JWT、Lombok、Swagger

### 前端
- **框架**：Vue.js
- **路由**：Vue Router
- **状态管理**：Vuex（可选，根据实际使用情况添加）
- **UI 组件库**：Element UI / Vuetify（根据实际使用的库填写）
- **构建工具**：Vue CLI

## 数据库设计
数据库设计文件 `data.sql` 包含了所有表的创建语句和初始数据插入。主要表结构如下：
- **用户表**：存储用户信息。
- **电影表**：存储电影信息。
- **影院表**：存储影院信息。
- **影厅表**：存储影厅信息。
- **座位表**：存储座位信息。
- **排片表**：存储排片信息。
- **订单表**：存储订单信息。
- **支付表**：存储支付信息。

## 项目结构
```
movie_ticketing_system/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── com.movie.ticketing.userservice/
│   │   │   │   │   ├── controller/          # 控制器层
│   │   │   │   │   ├── service/             # 服务层
│   │   │   │   │   ├── repository/          # 数据访问层
│   │   │   │   │   ├── entity/              # 实体类
│   │   │   │   │   ├── util/                # 工具类
│   │   │   │   │   └── config/              # 配置类
│   │   │   ├── resources/
│   │   │   │   ├── application.properties   # 配置文件
│   │   │   │   └── data.sql                 # 数据库初始化脚本
│   │   └── test/                            # 测试代码
│   ├── pom.xml                              # Maven配置文件
│   └── README.md                            # 后端项目说明文档
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/
│   │   ├── views/
│   │   │   ├── HomeView.vue
│   │   │   ├── UserHomeView.vue
│   │   │   ├── LoginView.vue
│   │   │   ├── MovieList.vue
│   │   │   ├── CinemaList.vue
│   │   │   ├── OrderList.vue
│   │   │   ├── ScheduleList.vue
│   │   │   └── ...                          # 其他页面组件
│   │   ├── router/
│   │   │   └── index.js                     # 路由配置
│   │   ├── store/
│   │   │   └── index.js                     # 状态管理（如有）
│   │   ├── App.vue                          # 根组件
│   │   └── main.js                          # 入口文件
│   ├── package.json                         # 依赖管理
│   └── README.md                            # 前端项目说明文档
├── README.md                                # 项目总说明文档
```

## 安装与运行
### 前提条件
- Java 1.8 或更高版本
- Maven 3.6 或更高版本
- MySQL 5.7 或更高版本
- Node.js 14.x 或更高版本
- npm 6.x 或更高版本

### 数据库配置
1. 创建数据库 `MovieTicket`。
2. 将 `backend/data.sql` 文件中的 SQL 语句导入到数据库中。

### 后端运行
1. 用IDE打开后端代码文件夹`movie_backend`。
2. 根据文件目录`src/main/java/.../MovieBackendApplication`找到启动器。
3. 直接运行启动器`MovieBackendApplication`。

### 前端运行
1. 打开新的终端，切换到前端项目根目录 `frontend/`。
2. 安装前端依赖：
   ```bash
   npm install
   ```
3. 启动前端开发服务器：
   ```bash
   npm run dev
   ```
4. 前端项目默认运行在 `http://localhost:8080`（或根据配置的端口）。


## 贡献指南
欢迎贡献代码！请遵循以下步骤：
1. Fork 本项目。
2. 创建一个新的分支：`git checkout -b feature/your-feature-name`。
3. 提交你的更改：`git commit -m "Add some feature"`。
4. 推送到你的分支：`git push origin feature/your-feature-name`。
5. 创建一个新的 Pull Request。


## 许可证
本项目采用 [MIT License](LICENSE) 许可证。

### 注意事项
1. **代码格式化**：在提交代码前，请确保代码格式化一致，可以使用 IDE 的格式化工具。
2. **代码注释**：关键代码段应添加注释，方便其他开发者理解。
3. **测试**：确保所有功能都有对应的测试用例，并且测试通过。

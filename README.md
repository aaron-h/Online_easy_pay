 <p align="center">
<img src="./image/cover.png">
</p>


<h3 align="center">🚀 OnlinePay 服务端</h3>

<p align="center">
 <img src="https://img.shields.io/static/v1?label=license&message=Apache-2.0&color=brightgreen&style=for-the-badge"/>
 <img src="https://img.shields.io/static/v1?label=version&message=5.0.0&color=9cf&style=for-the-badge"/>
 <img src="https://img.shields.io/static/v1?label=backend&message=Java%2021&color=007396&style=for-the-badge"/>
 <img src="https://img.shields.io/static/v1?label=frontend&message=Vue%203&color=4FC08D&style=for-the-badge"/>

</p>

### ️⚠️ 警告 

- 手机二维码监听收款是异地收款，收款频率高可能导致风控；
- 云端监听收款风险同上；

## 分支说明

- `master`分支：主分支，包含最新稳定版本
- `main`分支：主分支，与master分支保持同步
- `dev`分支：开发分支，包含最新开发中的功能
- 旧版PHP版本已迁移至[Vpay-Collection/VPay](https://github.com/Vpay-Collection/VPay)仓库

## 简介

OnlinePay ——一款现代化的个人收款解决方案，基于Vue 3 + Java 21 + Spring Boot + PostgreSQL技术栈，使个人开发者能够安全高效地处理在线交易。


## 安装指南

### 技术栈

- **后端**：Java 21 + Spring Boot 3.x + PostgreSQL
- **前端**：Vue 3.x + Vite + Element Plus
- **部署**：Docker Compose

### 环境要求

- JDK 21+
- Maven 3.6+
- Node.js 18+
- PostgreSQL 14+
- Docker（可选，用于容器化部署）

### 服务端安装

#### 一、Docker Compose部署（推荐）

1. 克隆项目到本地
2. 编辑 `docker-compose.yml` 文件，配置数据库和服务端口
3. 运行以下命令启动服务：
```bash
docker-compose up -d
```

#### 二、手动部署

1. **后端部署**
   - 克隆项目到本地
   - 进入 `backend` 目录
   - 修改 `application.yml` 配置文件，配置数据库连接和支付宝信息
   - 编译并打包：
     ```bash
     mvn clean package -DskipTests
     ```
   - 运行Spring Boot应用：
     ```bash
     java -jar target/onlinepay-backend.jar
     ```

2. **前端部署**
   - 进入 `frontend` 目录
   - 安装依赖：
     ```bash
     npm install
     ```
   - 构建生产版本：
     ```bash
     npm run build
     ```
   - 将 `dist` 目录部署到Nginx或其他Web服务器

### 服务端配置

1. 申请[支付宝当面付](https://open.alipay.com/intergraAssist/SC00002242?projectId=1487001107)
   - 【扫码支付】-【自研】，根据页面提示的接入准备完成当面付接入
2. 在 `backend/src/main/resources/application.yml` 中配置支付宝信息：
   ```yaml
alipay:
  app-id: your_app_id
  merchant-private-key: your_private_key
  alipay-public-key: alipay_public_key
  gateway-host: https://openapi.alipaydev.com/gateway.do
  notify-url: https://your_domain/api/notify/alipay
  return-url: https://your_domain/pay/success
  charset: UTF-8
  sign-type: RSA2
   ```
3. 配置PostgreSQL数据库连接：
   ```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/onlinepay
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
   ```

### 接入OnlinePay支付

参考[onlinepay-sdk](https://github.com/aaron-h/Online_easy_pay)进行接入

## 文档

[阅读文档](https://onlinepay.example.com/)


## 开源协议

Apache-2.0


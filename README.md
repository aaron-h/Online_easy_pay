 <p align="center">
<img src="./frontend/src/assets/logo.svg" width="300">
</p>


<h3 align="center">🚀 OnlinePay 服务端</h3>

<p align="center">
 <img src="https://img.shields.io/static/v1?label=license&message=Apache-2.0&color=brightgreen&style=for-the-badge"/>
 <img src="https://img.shields.io/static/v1?label=version&message=5.0.0&color=9cf&style=for-the-badge"/>
 <img src="https://img.shields.io/static/v1?label=backend&message=Java%2021&color=007396&style=for-the-badge"/>
 <img src="https://img.shields.io/static/v1?label=frontend&message=Vue%203&color=4FC08D&style=for-the-badge"/>

</p>

## 系统功能

OnlinePay是一款现代化的个人收款解决方案，提供完整的在线支付服务，支持多种支付方式和灵活的集成方式。

### 核心功能

- ✅ 支持支付宝、微信支付等多种支付方式
- ✅ 完整的订单管理系统
- ✅ 实时交易监控和通知
- ✅ 灵活的商户管理
- ✅ 支持多种集成方式（API、SDK）
- ✅ 现代化的管理后台
- ✅ 安全可靠的支付流程
- ✅ 详细的交易日志和统计

## 分支说明

- `master`分支：主分支，包含最新稳定版本
- `main`分支：主分支，与master分支保持同步
- `dev`分支：开发分支，包含最新开发中的功能
- 旧版PHP版本已迁移至[Vpay-Collection/VPay](https://github.com/Vpay-Collection/VPay)仓库

## 技术栈

- **后端**：Java 21 + Spring Boot 3.x + PostgreSQL
- **前端**：Vue 3.x + Vite + Element Plus
- **部署**：Docker Compose
- **数据库**：PostgreSQL（生产环境）、H2（测试环境）
- **ORM框架**：Spring Data JPA
- **API设计**：RESTful API
- **构建工具**：Maven（后端）、npm（前端）

## 环境要求

- JDK 21+
- Maven 3.6+
- Node.js 18+
- PostgreSQL 14+
- Docker（可选，用于容器化部署）

## 快速开始

### 测试用户

- 管理员账号：admin / admin123
- 商户账号：merchant / merchant123

### 系统启动步骤

#### 一、Docker Compose部署（推荐）

1. 克隆项目到本地
   ```bash
   git clone https://github.com/aaron-h/Online_easy_pay.git
   cd Online_easy_pay
   ```

2. 配置环境变量
   - 编辑 `docker-compose.yml` 文件，根据需要修改数据库密码和服务端口
   - 编辑 `backend/src/main/resources/application.yml`，配置支付宝等支付方式信息

3. 启动服务
   ```bash
   docker-compose up -d
   ```

4. 访问应用
   - 前端地址：http://localhost:80
   - 后端API地址：http://localhost:8080
   - 管理后台：http://localhost:80/admin

#### 二、手动部署

##### 1. 数据库准备

- 安装PostgreSQL数据库
- 创建数据库：`CREATE DATABASE onlinepay;`
- 执行初始化脚本：`backend/src/main/resources/db/init.sql`

##### 2. 后端部署

1. 进入后端目录
   ```bash
   cd backend
   ```

2. 配置数据库和支付方式
   - 编辑 `src/main/resources/application.yml` 文件
   - 配置数据库连接信息
   - 配置支付宝、微信支付等支付方式信息

3. 编译并打包
   ```bash
   mvn clean package -DskipTests
   ```

4. 运行应用
   ```bash
   java -jar target/onlinepay-backend.jar
   ```

   或者使用Maven直接运行
   ```bash
   mvn spring-boot:run
   ```

##### 3. 前端部署

1. 进入前端目录
   ```bash
   cd frontend
   ```

2. 安装依赖
   ```bash
   npm install
   ```

3. 配置API地址
   - 编辑 `src/config/index.js`（如果存在）或 `vite.config.js`
   - 配置后端API地址

4. 构建生产版本
   ```bash
   npm run build
   ```

5. 部署到Web服务器
   - 将 `dist` 目录下的文件复制到Nginx或其他Web服务器的根目录
   - 配置Nginx反向代理（示例）：
     ```nginx
     server {
         listen 80;
         server_name localhost;
         
         location / {
             root /path/to/onlinepay/frontend/dist;
             index index.html;
             try_files $uri $uri/ /index.html;
         }
         
         location /api {
             proxy_pass http://localhost:8080;
             proxy_set_header Host $host;
             proxy_set_header X-Real-IP $remote_addr;
         }
     }
     ```

## 与其他系统集成

OnlinePay提供多种集成方式，方便与其他系统快速对接。

### 1. RESTful API集成

#### 1.1 创建订单

**请求URL**：`POST /api/pay/create`

**请求参数**：
```json
{
  "amount": 100.00,
  "subject": "测试商品",
  "outTradeNo": "ORDER_20251211_001",
  "payType": "alipay",
  "returnUrl": "http://your-domain.com/pay/success",
  "notifyUrl": "http://your-domain.com/api/notify"
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "orderId": "1",
    "payUrl": "https://openapi.alipaydev.com/gateway.do?...",
    "qrCode": "data:image/png;base64,..."
  }
}
```

#### 1.2 查询订单

**请求URL**：`GET /api/pay/query/{orderId}`

**响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "orderId": "1",
    "outTradeNo": "ORDER_20251211_001",
    "amount": 100.00,
    "subject": "测试商品",
    "payType": "alipay",
    "status": "PAID",
    "createTime": "2025-12-11 10:00:00",
    "payTime": "2025-12-11 10:01:30"
  }
}
```

#### 1.3 支付回调

当用户支付成功后，OnlinePay会向您的`notifyUrl`发送POST请求，通知支付结果。

**回调示例**：
```json
{
  "orderId": "1",
  "outTradeNo": "ORDER_20251211_001",
  "amount": 100.00,
  "status": "PAID",
  "payTime": "2025-12-11 10:01:30",
  "sign": "..."
}
```

**签名验证**：
- 使用您的商户密钥对回调数据进行签名验证
- 验证方法：`sign = MD5(orderId + outTradeNo + amount + status + payTime + secret)`

### 2. SDK集成

#### 2.1 Java SDK

1. 添加依赖
   ```xml
   <dependency>
       <groupId>com.onlinepay</groupId>
       <artifactId>onlinepay-sdk</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

2. 初始化SDK
   ```java
   OnlinePayClient client = new OnlinePayClient(
       "your-api-key",
       "your-api-secret",
       "http://onlinepay-server:8080"
   );
   ```

3. 创建订单
   ```java
   CreateOrderRequest request = new CreateOrderRequest();
   request.setAmount(100.00);
   request.setSubject("测试商品");
   request.setOutTradeNo("ORDER_20251211_001");
   request.setPayType(PayType.ALIPAY);
   
   CreateOrderResponse response = client.createOrder(request);
   ```

#### 2.2 JavaScript SDK

1. 引入SDK
   ```html
   <script src="https://cdn.onlinepay.com/sdk/onlinepay-sdk.min.js"></script>
   ```

2. 初始化SDK
   ```javascript
   const onlinePay = new OnlinePaySDK({
     apiKey: 'your-api-key',
     apiUrl: 'http://onlinepay-server:8080'
   });
   ```

3. 创建订单并唤起支付
   ```javascript
   onlinePay.createOrder({
     amount: 100.00,
     subject: '测试商品',
     outTradeNo: 'ORDER_20251211_001',
     payType: 'alipay'
   }).then(order => {
     // 唤起支付
     onlinePay.pay(order.payUrl);
   });
   ```

### 3. iframe集成

您可以通过iframe方式将OnlinePay支付页面嵌入到您的网站中。

```html
<iframe 
  src="http://onlinepay-server:80/pay?amount=100.00&subject=测试商品&outTradeNo=ORDER_20251211_001&returnUrl=http://your-domain.com/pay/success" 
  width="600" 
  height="500" 
  frameborder="0"
></iframe>
```

## 完整支付流程

1. **创建订单**：商户系统调用OnlinePay API创建订单
2. **生成支付链接**：OnlinePay返回支付链接或二维码
3. **用户支付**：用户通过支付宝或微信扫描二维码完成支付
4. **支付回调**：OnlinePay向商户系统发送支付结果通知
5. **订单确认**：商户系统确认订单状态并更新业务数据
6. **返回结果**：商户系统返回支付成功页面给用户

## 系统架构

### 后端架构

```
├── config/          # 配置类
├── controller/      # 控制器层
│   ├── api/         # API接口
│   └── admin/       # 管理后台接口
├── entity/          # 实体类
├── repository/      # 数据访问层
├── service/         # 业务逻辑层
│   ├── alipay/      # 支付宝支付服务
│   └── wechat/      # 微信支付服务
└── utils/           # 工具类
```

### 前端架构

```
├── assets/          # 静态资源
├── components/      # 组件
├── router/          # 路由配置
├── views/           # 页面视图
│   ├── admin/       # 管理后台页面
│   └── user/        # 用户页面
├── App.vue          # 根组件
└── main.js          # 入口文件
```

## 开发指南

### 后端开发

1. 环境要求：JDK 21+, Maven 3.6+
2. IDE推荐：IntelliJ IDEA, Eclipse
3. 运行测试：`mvn test`
4. 代码风格：遵循Spring Boot编码规范

### 前端开发

1. 环境要求：Node.js 18+, npm 9+
2. IDE推荐：VS Code
3. 开发模式：`npm run dev`
4. 代码风格：遵循ESLint规范

## 监控与维护

### 日志管理

- 后端日志：默认输出到控制台和 `logs/` 目录
- 日志级别：可在 `application.yml` 中配置
- 常见问题排查：查看 `logs/error.log`

### 性能监控

- 可集成Spring Boot Actuator进行性能监控
- 推荐使用Prometheus + Grafana进行监控

### 常见问题

1. **支付回调失败**
   - 检查回调URL是否可访问
   - 检查签名验证是否正确
   - 查看后端日志获取详细错误信息

2. **订单状态不更新**
   - 检查支付方式配置是否正确
   - 检查异步通知是否开启
   - 手动查询支付平台订单状态

3. **前端无法访问后端API**
   - 检查CORS配置是否正确
   - 检查后端服务是否正常运行
   - 检查网络连接和防火墙设置

## 文档

- [API文档](http://localhost:8080/swagger-ui.html)（部署后可访问）
- [用户手册](USER_MANUAL.md)
- [开发文档](DEVELOP_GUIDE.md)（待完善）

## 开源协议

Apache License 2.0

## 联系方式

- 项目地址：https://github.com/aaron-h/Online_easy_pay
- 问题反馈：https://github.com/aaron-h/Online_easy_pay/issues
- 邮箱：contact@onlinepay.com

## 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本项目
2. 创建特性分支：`git checkout -b feature/your-feature`
3. 提交代码：`git commit -m 'Add some feature'`
4. 推送分支：`git push origin feature/your-feature`
5. 提交Pull Request

## 更新日志

### v5.0.0 (2025-12-11)

- 🔄 迁移至Vue 3.x + Java 21 + Spring Boot 3.x
- 🎨 全新设计的UI界面和logo
- 📦 支持Docker Compose部署
- 🔒 增强的安全性
- 🚀 优化的性能
- 📱 响应式设计，支持移动端
- 🌐 支持多种支付方式集成

### v4.2.0 (2024-08-15)

- 支持微信支付
- 优化订单管理功能
- 修复已知bug

## 许可证

[Apache License 2.0](LICENSE)



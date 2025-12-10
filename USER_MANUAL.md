# OnlinePay用户手册

## 1. 系统概述

OnlinePay是一款基于Vue 3.x + Java 21 + Spring Boot + PostgreSQL技术栈开发的个人收款平台，提供安全高效的收款解决方案。

### 1.1 核心功能

- ✅ 支持支付宝、微信支付等多种支付方式
- ✅ 完整的订单管理系统
- ✅ 可视化管理后台
- ✅ 商品商店功能
- ✅ 第三方网站集成API
- ✅ 异步通知机制
- ✅ 订单自动过期处理
- ✅ 简洁科技感的UI设计

### 1.2 技术栈

- **前端**: Vue 3.x + Vite + Element Plus
- **后端**: Java 21 + Spring Boot 3.x + JPA
- **数据库**: PostgreSQL
- **部署**: Docker Compose

## 2. 快速开始

### 2.1 环境要求

- Docker 20.10+
- Docker Compose 2.0+
- 8GB RAM
- 2核CPU

### 2.2 启动步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/your-repo/OnlinePay.git
   cd OnlinePay
   ```

2. **配置环境变量**
   ```bash
   cp backend/src/main/resources/application.yml.example backend/src/main/resources/application.yml
   # 编辑application.yml，配置数据库、支付宝等信息
   ```

3. **启动应用**
   ```bash
   docker-compose up -d
   ```

4. **访问应用**
   - 前端地址: http://localhost/
   - 后端API地址: http://localhost:8080
   - 管理后台: http://localhost/admin/console

5. **初始账号**
   - 用户名: admin
   - 密码: admin

### 2.3 停止应用

```bash
docker-compose down
```

## 3. 管理后台使用

### 3.1 登录管理后台

1. 访问 http://localhost/admin/console
2. 输入用户名和密码
3. 点击"登录"按钮

### 3.2 控制台

- 查看系统概览
- 监控订单数据
- 查看系统状态

### 3.3 应用管理

- 创建和管理应用
- 配置应用参数
- 生成应用密钥

### 3.4 订单管理

- 查看所有订单
- 搜索和筛选订单
- 处理异常订单
- 导出订单数据

### 3.5 商店管理

- 添加、编辑和删除商品
- 配置商品分类
- 设置商品价格
- 管理商品状态

## 4. 商店功能

### 4.1 浏览商品

1. 访问 http://localhost/shop
2. 浏览商品列表
3. 使用分类筛选商品

### 4.2 购买商品

1. 选择商品
2. 点击"立即购买"按钮
3. 进入支付页面
4. 选择支付方式
5. 扫描二维码支付
6. 支付成功后跳转到支付成功页面

### 4.3 查看订单

- 在支付成功页面查看订单信息
- 记录订单号以便后续查询

## 5. 第三方网站集成指南

### 5.1 集成准备

1. **登录管理后台**
2. 进入"应用管理"
3. 点击"创建应用"
4. 填写应用信息
5. 获取`appId`和`appKey`

### 5.2 集成方式

OnlinePay提供两种集成方式：

#### 5.2.1 前端跳转集成

**使用场景**: 适合已有网站，希望通过跳转方式集成支付

**集成步骤**:

1. **构造支付链接**
   ```javascript
   const baseUrl = 'http://your-onlinepay-domain/pay';
   const params = {
     appId: 'your_app_id',
     item: '商品名称',
     price: '100.00',
     notifyUrl: 'http://your-domain/notify',
     returnUrl: 'http://your-domain/success',
     // 使用MD5签名
     sign: md5(`${appId}${item}${price}${notifyUrl}${returnUrl}${appKey}`)
   };
   
   const payUrl = `${baseUrl}?${new URLSearchParams(params)}`;
   ```

2. **跳转到支付页面**
   ```javascript
   window.location.href = payUrl;
   ```

3. **处理支付结果**
   - 支付成功后自动跳转至`returnUrl`
   - 异步通知会发送至`notifyUrl`

#### 5.2.2 API集成

**使用场景**: 适合需要自定义支付页面，或在后端处理支付逻辑

**集成步骤**:

1. **引入依赖**
   ```xml
   <!-- Maven依赖 -->
   <dependency>
     <groupId>com.alipay.sdk</groupId>
     <artifactId>alipay-easysdk</artifactId>
     <version>2.2.0</version>
   </dependency>
   ```

2. **创建订单API**
   ```http
   POST /api/pay/create
   Content-Type: application/json
   
   {
     "appId": "your_app_id",
     "appItem": "商品名称",
     "price": 100.00,
     "payType": 2, // 2: 支付宝, 3: 微信
     "notifyUrl": "http://your-domain/notify",
     "returnUrl": "http://your-domain/success",
     "sign": "md5_sign"
   }
   ```

3. **响应示例**
   ```json
   {
     "code": 200,
     "message": "成功",
     "data": {
       "orderId": "ORD1234567890",
       "appItem": "商品名称",
       "price": 100.00,
       "payImage": "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://www.alipay.com",
       "createTime": "2025-12-10T16:30:00"
     }
   }
   ```

4. **查询订单API**
   ```http
   POST /api/pay/query
   Content-Type: application/json
   
   {
     "appId": "your_app_id",
     "orderId": "ORD1234567890",
     "sign": "md5_sign"
   }
   ```

5. **关闭订单API**
   ```http
   POST /api/pay/close
   Content-Type: application/json
   
   {
     "appId": "your_app_id",
     "orderId": "ORD1234567890",
     "sign": "md5_sign"
   }
   ```

### 5.3 异步通知处理

#### 5.3.1 通知格式

```http
POST /your-notify-url
Content-Type: application/x-www-form-urlencoded

orderId=ORD1234567890&appId=your_app_id&state=2&price=100.00&sign=md5_sign
```

#### 5.3.2 通知状态码

| 状态码 | 含义 |
|-------|------|
| 1 | 等待支付 |
| 2 | 已支付 |
| 3 | 订单成功 |
| -1 | 已关闭 |

#### 5.3.3 通知处理步骤

1. **验证签名**
   ```javascript
   const receivedSign = req.body.sign;
   const data = {
     orderId: req.body.orderId,
     appId: req.body.appId,
     state: req.body.state,
     price: req.body.price
   };
   const expectedSign = md5(`${data.orderId}${data.appId}${data.state}${data.price}${appKey}`);
   
   if (receivedSign !== expectedSign) {
     return res.send('fail');
   }
   ```

2. **处理业务逻辑**
   ```javascript
   // 更新订单状态
   // 发货处理
   // 发送通知等
   ```

3. **返回响应**
   ```javascript
   // 处理成功返回success
   res.send('success');
   // 处理失败返回fail
   res.send('fail');
   ```

### 5.4 签名算法

所有API请求和通知都需要使用MD5签名，签名规则如下：

1. **参数排序**：将所有参数按字母顺序排序
2. **拼接字符串**：将排序后的参数值与appKey拼接
3. **MD5加密**：对拼接后的字符串进行MD5加密
4. **转换大写**：将加密结果转换为大写

**示例**：
```javascript
const params = { appId: '123', item: 'test', price: '100' };
const sortedKeys = Object.keys(params).sort();
const signStr = sortedKeys.map(key => params[key]).join('') + appKey;
const sign = md5(signStr).toUpperCase();
```

## 6. API文档

### 6.1 基础信息

- **API地址**: http://localhost:8080/api
- **请求方式**: POST
- **Content-Type**: application/json
- **编码**: UTF-8

### 6.2 接口列表

#### 6.2.1 创建订单

**接口地址**: `/pay/create`

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| appId | String | 是 | 应用ID |
| appItem | String | 是 | 商品名称 |
| price | BigDecimal | 是 | 商品价格 |
| payType | Integer | 是 | 支付类型(2:支付宝, 3:微信) |
| notifyUrl | String | 是 | 异步通知地址 |
| returnUrl | String | 否 | 同步返回地址 |
| sign | String | 是 | MD5签名 |

**响应示例**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "orderId": "ORD1234567890",
    "appItem": "商品名称",
    "price": 100.00,
    "payImage": "二维码地址",
    "createTime": "2025-12-10T16:30:00"
  }
}
```

#### 6.2.2 查询订单

**接口地址**: `/pay/query`

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| appId | String | 是 | 应用ID |
| orderId | String | 是 | 订单号 |
| sign | String | 是 | MD5签名 |

**响应示例**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "orderId": "ORD1234567890",
    "appItem": "商品名称",
    "price": 100.00,
    "state": 2,
    "createTime": "2025-12-10T16:30:00",
    "payTime": "2025-12-10T16:35:00"
  }
}
```

#### 6.2.3 关闭订单

**接口地址**: `/pay/close`

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
|-------|------|------|------|
| appId | String | 是 | 应用ID |
| orderId | String | 是 | 订单号 |
| sign | String | 是 | MD5签名 |

**响应示例**:

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "orderId": "ORD1234567890",
    "state": -1,
    "closeTime": "2025-12-10T16:40:00"
  }
}
```

#### 6.2.4 异步通知

**接口地址**: 自定义

**通知参数**:

| 参数名 | 类型 | 描述 |
|-------|------|------|
| orderId | String | 订单号 |
| appId | String | 应用ID |
| state | Integer | 订单状态 |
| price | BigDecimal | 支付金额 |
| sign | String | MD5签名 |

**响应要求**:

- 成功: 返回 `success`
- 失败: 返回 `fail`

## 7. 常见问题

### 7.1 支付成功后订单状态未更新

**问题原因**:
- 异步通知未收到
- 签名验证失败
- 业务逻辑处理错误

**解决方案**:
1. 检查异步通知地址是否可访问
2. 检查签名算法是否正确
3. 查看服务器日志，排查业务逻辑错误
4. 使用查询订单API手动查询订单状态

### 7.2 支付二维码无法生成

**问题原因**:
- 支付配置错误
- 第三方支付平台API调用失败
- 网络连接问题

**解决方案**:
1. 检查支付配置是否正确
2. 查看服务器日志，排查API调用错误
3. 检查网络连接是否正常
4. 尝试切换支付方式

### 7.3 集成时签名验证失败

**问题原因**:
- 参数排序错误
- 拼接字符串错误
- appKey错误
- 编码问题

**解决方案**:
1. 确保参数按字母顺序排序
2. 检查签名拼接字符串是否正确
3. 验证appKey是否匹配
4. 确保使用UTF-8编码

### 7.4 订单自动关闭

**问题原因**:
- 订单超过600秒未支付
- 第三方支付平台返回订单关闭

**解决方案**:
1. 提醒用户及时支付
2. 检查支付流程是否顺畅
3. 调整订单过期时间配置

## 8. 系统配置

### 8.1 应用配置

**文件路径**: `backend/src/main/resources/application.yml`

**主要配置项**:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/onlinepay
    username: postgres
    password: 123456
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  
  redis:
    host: redis
    port: 6379

alipay:
  app-id: your_app_id
  merchant-private-key: your_private_key
  alipay-public-key: alipay_public_key
  notify-url: http://your_domain/api/notify/alipay
  return-url: http://your_domain/pay/success
  gateway-host: https://openapi.alipaydev.com/gateway.do
  
server:
  port: 8080
  servlet:
    context-path: /
```

### 8.2 Docker配置

**文件路径**: `docker-compose.yml`

**主要配置项**:

```yaml
services:
  postgres:
    image: postgres:15-alpine
    container_name: onlinepay-postgres
    environment:
      POSTGRES_DB: onlinepay
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 123456
    ports:
      - "5432:5432"
  
  redis:
    image: redis:7-alpine
    container_name: onlinepay-redis
    ports:
      - "6379:6379"
  
  backend:
    build: ./backend
    container_name: onlinepay-backend
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - redis
  
  frontend:
    build: ./frontend
    container_name: onlinepay-frontend
    ports:
      - "80:80"
    depends_on:
      - backend
```

## 9. 技术支持

### 9.1 联系方式

- 官方网站: http://onlinepay.example.com
- GitHub: https://github.com/your-repo/OnlinePay
- 邮箱: support@onlinepay.example.com

### 9.2 贡献指南

欢迎提交Issue和Pull Request，贡献代码和改进建议。

### 9.3 更新日志

**v1.0.0** (2025-12-10)
- 初始版本发布
- 支持支付宝、微信支付
- 完整的订单管理系统
- 管理后台功能
- 商品商店功能
- 第三方网站集成API

## 10. 许可证

OnlinePay采用MIT许可证开源，详见LICENSE文件。

---

感谢您使用OnlinePay！如果您有任何问题或建议，欢迎随时联系我们。
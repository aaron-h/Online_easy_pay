-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS onlinepay;

-- 切换到onlinepay数据库
\c onlinepay;

-- 创建管理员表
CREATE TABLE IF NOT EXISTS admin (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建应用表
CREATE TABLE IF NOT EXISTS app (
    id SERIAL PRIMARY KEY,
    app_name VARCHAR(100) NOT NULL,
    app_key VARCHAR(255) NOT NULL,
    app_image VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建订单表
CREATE TABLE IF NOT EXISTS "order" (
    id SERIAL PRIMARY KEY,
    pay_type INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    real_price DECIMAL(10, 2) NOT NULL,
    app_name VARCHAR(100) NOT NULL,
    app_item VARCHAR(255) NOT NULL,
    appid INTEGER NOT NULL REFERENCES app(id),
    order_id VARCHAR(100) NOT NULL UNIQUE,
    notify_url VARCHAR(255) NOT NULL,
    return_url VARCHAR(255),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    pay_time TIMESTAMP,
    close_time TIMESTAMP,
    state INTEGER NOT NULL DEFAULT 1,
    param TEXT,
    "user" VARCHAR(100),
    pay_image VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建商店表
CREATE TABLE IF NOT EXISTS shop (
    id SERIAL PRIMARY KEY,
    item VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    inputs TEXT,
    api VARCHAR(255),
    category VARCHAR(50) NOT NULL DEFAULT '默认',
    stop BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建文件表
CREATE TABLE IF NOT EXISTS file (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    path VARCHAR(255) NOT NULL,
    count INTEGER NOT NULL DEFAULT 0,
    timeout TIMESTAMP,
    hash VARCHAR(255) NOT NULL,
    link VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_order_state ON "order"(state);
CREATE INDEX IF NOT EXISTS idx_order_create_time ON "order"(create_time);
CREATE INDEX IF NOT EXISTS idx_order_order_id ON "order"(order_id);
CREATE INDEX IF NOT EXISTS idx_file_hash ON file(hash);

-- 插入默认管理员数据
INSERT INTO admin (username, password, nickname) VALUES ('admin', '$2a$10$E6q7W5Y3R4T2Y1U0I9O8P7N6M5L4K3J2H1G0F9E8D7C6B5A4', '管理员') ON CONFLICT DO NOTHING;

-- 插入测试商品数据
INSERT INTO shop (item, description, price, category, stop) VALUES 
('在线课程', '高级Java开发课程，包含Spring Boot、微服务等内容', 99.99, '教育', false),
('电子书', '《现代前端开发实战》，包含Vue 3、React等框架', 39.99, '电子书', false),
('软件许可', 'OnlinePay专业版许可，支持无限商户', 199.00, '软件', false),
('技术支持', '1小时专业技术支持服务', 150.00, '服务', false),
('定制开发', '根据需求定制开发支付系统', 5000.00, '服务', false),
('服务器租赁', '高性能云服务器，适合中小型应用', 299.00, '云服务', false),
('域名注册', '顶级域名注册服务，包含一年DNS管理', 59.99, '云服务', false),
('SSL证书', '通配符SSL证书，保护所有子域名', 199.00, '安全', false) ON CONFLICT DO NOTHING;

-- 注释：默认密码为 admin，使用 BCrypt 加密

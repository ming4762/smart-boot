# Smart Boot

<div align="center">

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.1-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)

企业级通用开发框架，基于 Spring Boot 3.x 和 Spring Cloud 构建

</div>

## 📖 项目简介

Smart Boot 是一个功能完善的企业级通用开发框架，提供了丰富的开箱即用的功能模块和 Starter 组件，帮助开发者快速构建企业级应用。框架采用模块化设计，支持单体和微服务架构，集成了主流的技术栈和最佳实践。

## ✨ 核心特性

- 🚀 基于最新的 Spring Boot 3.5.11 和 Spring Cloud 2025.0.1
- 🔐 完善的认证授权体系（JWT、SAML2、短信、微信、钉钉、域认证等）
- 📁 多种文件存储支持（本地磁盘、阿里云 OSS、MinIO、七牛云、AWS S3、FTP/SFTP）
- 📄 文档处理能力（Excel 导入导出、文档转换、二维码生成）
- 💬 消息通知系统（邮件、短信、站内消息）
- 🤖 AI 集成（Langchain4j、Dify）
- 🔄 数据 ETL 支持（Kettle 集成）
- 📊 监控与日志（慢 SQL 记录、操作日志、登录日志）
- 🌍 国际化与多时区支持
- 🎨 代码生成器
- 🔒 License 授权管理
- 📱 多租户支持

## 🏗️ 技术栈

### 核心框架
- Spring Boot 3.5.11
- Spring Cloud 2025.0.1
- Spring Cloud Alibaba 2023.0.3.3
- MyBatis Plus 3.5.16

### 数据库
- MySQL / Oracle / PostgreSQL
- Redis (Redisson 4.3.0)
- Druid 1.2.28

### 中间件
- Nacos（服务注册与配置中心）
- RocketMQ 5.4.0
- XXL-Job 3.2.0

### 工具库
- Hutool 5.8.40
- Guava 33.4.8
- Lombok

### AI 集成
- Langchain4j 1.3.0
- Dify AI

## 📦 项目结构

```
smart-boot/
├── smart-boot-framework/          # 框架核心模块
│   ├── smart-boot-actuator/       # 监控模块
│   ├── smart-boot-autoconfigure/  # 自动配置
│   └── smart-boot-starters/       # Starter 组件集合
│       ├── smart-boot-starter-auth/           # 认证授权
│       ├── smart-boot-starter-file/           # 文件存储
│       ├── smart-boot-starter-document/       # 文档处理
│       ├── smart-boot-starter-message/        # 消息通知
│       ├── smart-boot-starter-ai-langchain4j/ # AI 集成
│       ├── smart-boot-starter-ai-dify/        # Dify AI
│       ├── smart-boot-starter-crud/           # CRUD 增强
│       ├── smart-boot-starter-druid/          # 数据源
│       ├── smart-boot-starter-redis/          # Redis
│       ├── smart-boot-starter-captcha/        # 验证码
│       ├── smart-boot-starter-i18n/           # 国际化
│       ├── smart-boot-starter-log/            # 日志
│       └── ...                                # 更多组件
├── smart-framework/               # 基础框架
├── smart-cloud-framework/         # 微服务框架
├── smart-cloud-services/          # 微服务应用
├── smart-modules/                 # 业务模块
│   ├── smart-module-system/       # 系统管理
│   ├── smart-module-auth/         # 认证模块
│   ├── smart-module-file/         # 文件管理
│   ├── smart-module-message/      # 消息管理
│   ├── smart-module-code/         # 代码生成
│   └── smart-module-document/     # 文档管理
├── smart-services/                # 服务应用
├── smart-module-api/              # API 模块
├── deployment/                    # 部署配置
│   └── docker/                    # Docker 配置
└── document/                      # 项目文档
```

## 🚀 快速开始

### 环境要求

- JDK 25+
- Maven 3.6+
- MySQL 5.7+ / Oracle 11g+ / PostgreSQL 12+
- Redis 5.0+
- Nacos 2.x（微服务模式）

### 构建项目

```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd smart-boot

# 编译打包
mvn clean install -DskipTests
```

### 配置说明

项目支持多环境配置，通过 Maven Profile 切换：

- `dev`：开发环境（默认）
- `pro`：生产环境

```bash
# 使用开发环境
mvn clean package -Pdev

# 使用生产环境
mvn clean package -Ppro
```

### 运行服务

```bash
# 运行单体应用
java -jar smart-services/smart-service-system/target/smart-service-system.jar

# 运行微服务（需先启动 Nacos）
java -jar smart-cloud-services/<service-name>/target/<service-name>.jar
```

### Docker 部署

```bash
cd deployment/docker

# 构建镜像
docker build -f Dockerfile -t smart-boot:latest .

# 运行容器
docker run -d -p 8080:8080 smart-boot:latest
```

## 📚 功能模块

### 认证授权
- JWT Token 认证
- SAML2 单点登录
- 短信验证码登录
- 微信登录
- 钉钉登录
- 域账号认证
- Access Secret 认证
- 验证码（图形、滑块）
- 缓存支持（Guava、Redis）

### 文件管理
- 本地磁盘存储
- 阿里云 OSS
- MinIO
- 七牛云
- AWS S3
- FTP/SFTP

### 文档处理
- Excel 导入导出（JXLS）
- 文档格式转换（JOD Converter、Office）
- 二维码生成（ZXing）

### 消息通知
- 邮件发送
- 短信发送（阿里云、腾讯云）
- 站内消息
- 钉钉消息
- 微信消息

### 系统管理
- 用户管理
- 角色管理
- 权限管理
- 菜单管理
- 部门管理
- 租户管理
- 数据权限
- 操作日志
- 登录日志
- 慢 SQL 记录

### 代码生成
- 支持多数据库
- 自定义模板
- 在线预览
- 一键生成

### AI 能力
- Langchain4j 集成
- Dify AI 集成
- 支持多种 LLM 模型

## 🔧 配置示例

### Nacos 配置

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: dev
        username: nacos
        password: nacos
      config:
        server-addr: 127.0.0.1:8848
        namespace: dev
        username: nacos
        password: nacos
```

### 数据源配置

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/smart_boot?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: password
```

### Redis 配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

## 📝 开发指南

### 使用 Starter

在项目中引入所需的 Starter：

```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-auth-jwt</artifactId>
    <version>5.0.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-aliyun</artifactId>
    <version>5.0.0-SNAPSHOT</version>
</dependency>
```

### 代码生成

1. 访问代码生成器页面
2. 配置数据源连接
3. 选择要生成的表
4. 配置生成参数
5. 预览并生成代码

## 🗺️ 路线图

查看 [document/todo.md](document/todo.md) 了解项目规划和进度。

主要计划：
- ✅ 多时区支持
- ✅ 慢 SQL 记录
- ✅ 数据权限重构
- ✅ 租户管理完善
- 🔄 单点登录
- 🔄 规则引擎集成（LiteFlow）
- 🔄 APP 端支持（Uniapp）
- 🔄 动态数据源
- 🔄 SQL2API 支持

## 📄 文档

详细文档请查看 [document](document/) 目录：

- [AuthAccessSecret 说明文档](document/smart-auth/AuthAccessSecret说明文档.md)
- [License 使用说明](document/smart-license/readme.md)
- [Smart Table 使用说明](document/smart-ui/Smart-table.md)
- [修改记录功能说明](document/smart-system/修改记录ChangeLog功能使用说明.md)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 👨‍💻 开发者

- **zhongming4762**
- Email: zhongming4762@hotmail.com

## 📜 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

## 🙏 致谢

感谢所有开源项目和贡献者的支持！

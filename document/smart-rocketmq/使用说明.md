# smart-rocketmq 使用说明

## 概述

`smart-rocketmq` 是对 RocketMQ 的封装模块，提供了统一的消息生产者、消费者抽象，并实现了 **Spring Event 与 RocketMQ 的桥接能力**：

- **单体架构**：直接使用 Spring `ApplicationEvent` 进行进程内事件发布/监听
- **微服务架构**：通过 RocketMQ 将 Spring Event 广播给其他服务实例，实现跨服务事件驱动

---

## 模块结构

| 模块 | 说明 |
|------|------|
| `smart-rocketmq` | 核心封装（生产者、消费者、事件桥接） |
| `smart-boot-starter-rocketmq` | Spring Boot Starter，自动装配 |

---

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-rocketmq</artifactId>
    <version>${smart.version}</version>
</dependency>
```

> 该 Starter 已包含 `rocketmq-spring-boot-starter`（版本 2.3.5，对应 RocketMQ 5.4.0），无需单独引入。

### 2. 配置 RocketMQ 连接

```yaml
rocketmq:
  name-server: 127.0.0.1:9876
  producer:
    group: my-service-producer-group

spring:
  application:
    name: my-service
```

### 3. smart-mq 配置项

```yaml
smart:
  mq:
    # Topic 前缀，用于区分不同环境（如 dev:、test:），默认为空
    prefix: ""
    # Spring Event 桥接使用的 Topic，默认 smart-boot-event
    event-topic: smart-boot-event
    # Spring Event 桥接使用的 Tag，默认 event
    event-tag: event
```

---

## 核心 API

### 消息模型：SmartMqMessage

所有消息统一使用 `SmartMqMessage<T>` 进行包装：

| 字段 | 类型 | 说明 |
|------|------|------|
| `messageId` | `String` | 消息唯一ID（UUID，自动生成） |
| `timestamp` | `long` | 消息时间戳（自动生成） |
| `source` | `String` | 消息来源标识（可选） |
| `payload` | `T` | 业务数据，需实现 `Serializable` |
| `userJson` | `String` | 当前用户信息 JSON（框架自动填充） |

> 框架会自动将当前登录用户（`RestUserDetails`）序列化到 `userJson`，消费端也会自动还原用户上下文。

---

## 消息生产者

### 注入 SmartMqProducer

```java
@Autowired
private SmartMqProducer smartMqProducer;
```

### 发送方式

#### 同步发送

```java
// 构建消息
SmartMqMessage<OrderDTO> message = SmartMqMessage.<OrderDTO>builder()
        .source("order-service")
        .payload(orderDTO)
        .build();

// 同步发送（topic:tag 格式）
SendResult result = smartMqProducer.syncSend("order-topic:create", message);
```

#### 异步发送

```java
smartMqProducer.asyncSend("order-topic:create", message, new SendCallback() {
    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("发送成功: {}", sendResult.getMsgId());
    }
    @Override
    public void onException(Throwable e) {
        log.error("发送失败", e);
    }
});
```

#### 延迟发送

```java
// 延迟 30 秒发送
SendResult result = smartMqProducer.syncSendDelay("order-topic:timeout", message, Duration.ofSeconds(30));
```

#### 顺序消息

```java
// hashKey 相同的消息保证顺序（通常传业务ID）
SendResult result = smartMqProducer.syncSendOrderly("order-topic:create", message, orderId);

// 异步顺序
smartMqProducer.asyncSendOrderly("order-topic:create", message, orderId, sendCallback);
```

### Topic 前缀说明

实际发送的 destination = `smart.mq.prefix` + `destination`。

例如，`prefix=dev:` 时，`syncSend("order-topic:create", ...)` 实际发送到 `dev:order-topic:create`。

---

## 消息消费者

继承 `SmartBaseConsumer<T>` 并添加 `@RocketMQMessageListener` 注解即可。

### 基本用法

```java
@Component
@RocketMQMessageListener(
    topic = "order-topic",
    selectorExpression = "create",
    consumerGroup = "my-service-order-consumer"
)
public class OrderCreateConsumer implements SmartBaseConsumer<OrderDTO> {

    @Override
    public void doOnMessage(SmartMqMessage<OrderDTO> message) {
        OrderDTO order = message.getPayload();
        log.info("收到订单消息: {}", order.getOrderId());
        // 处理业务逻辑...
    }
}
```

### 幂等处理

重写 `isDuplicate` 方法实现幂等校验，返回 `true` 时框架自动跳过处理：

```java
@Component
@RocketMQMessageListener(
    topic = "order-topic",
    selectorExpression = "create",
    consumerGroup = "my-service-order-consumer"
)
public class OrderCreateConsumer implements SmartBaseConsumer<OrderDTO> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isDuplicate(SmartMqMessage<OrderDTO> message) {
        String key = "mq:processed:" + message.getMessageId();
        // 使用 Redis 实现幂等（SET NX）
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.ofHours(24));
        return Boolean.FALSE.equals(isNew);
    }

    @Override
    public void doOnMessage(SmartMqMessage<OrderDTO> message) {
        // 业务逻辑
    }
}
```

### SmartBaseConsumer 特性

- **自动日志**：收到消息时自动打印 `messageId`、`source`、消息类型
- **异常重试**：处理异常时自动抛出 `SmartMqException`，触发 RocketMQ 重试机制
- **用户上下文传递**：自动还原生产者的用户上下文，消费端可直接使用 `AuthUtils.getCurrentUser()`

---

## Spring Event 桥接（跨服务事件）

这是 `smart-rocketmq` 的核心特性之一：**让 Spring Event 透明地跨服务传播**，业务代码无需感知底层是本地调用还是 MQ。

### 工作原理

```
服务A                              RocketMQ                    服务B
─────                              ────────                    ────
publishEvent(MyEvent)               ↗ Topic: smart-boot-event   ↘
      ↓                           ↗   Tag: event                ↘
SmartSpringEventProducer ────────→                        SmartSpringEventConsumer
(监听 LOCAL 类型事件)               发送 SmartEventWrapper         ↓
                                                           publishEvent(MyEvent)
                                                                 ↓
                                                          @EventListener(MyEvent)
                                                          业务监听器处理
```

**关键机制**：
- `SmartSpringEventProducer` 监听所有继承 `AbstractSmartCommonEvent` 的事件，将 `LOCAL` 类型事件序列化发送到 RocketMQ
- `SmartSpringEventConsumer` 消费 MQ 消息，反序列化后调用 `applicationContext.publishEvent()` 重新发布为 Spring Event
- 过滤自身服务的消息，避免回环重复消费（通过 `eventSourceService` == `spring.application.name` 判断）

### 第一步：定义事件类

事件类必须继承 `AbstractSmartCommonEvent`：

```java
public class UserRegisteredEvent extends AbstractSmartCommonEvent {

    private final Long userId;
    private final String username;

    public UserRegisteredEvent(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
}
```

### 第二步：发布事件

业务代码直接通过 `ApplicationEventPublisher` 发布事件，与普通 Spring Event **完全一致**：

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final ApplicationEventPublisher eventPublisher;

    public void register(UserDTO dto) {
        // 业务逻辑...
        
        // 发布事件（单体模式下本地消费，微服务模式下同时广播到其他服务）
        eventPublisher.publishEvent(new UserRegisteredEvent(user.getId(), user.getUsername()));
    }
}
```

### 第三步：监听事件

在任意服务中使用标准 `@EventListener` 或 `SmartEventHandler` 监听事件：

**方式一：@EventListener（推荐）**

```java
@Component
public class UserEventHandler {

    @EventListener(UserRegisteredEvent.class)
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("用户注册: userId={}, username={}", event.getUserId(), event.getUsername());
        // 发送欢迎邮件、初始化用户数据等...
    }
}
```

**方式二：SmartEventHandler 接口**

```java
@Component
public class UserWelcomeHandler implements SmartEventHandler<UserRegisteredEvent> {

    @Override
    public void handle(UserRegisteredEvent event) {
        // 处理用户注册事件
    }
}
```

### 注意事项

1. **事件类需在各服务中可见**：跨服务事件的事件类，建议放到公共 API 模块（如 `smart-module-api`）中
2. **事件类需支持 JSON 序列化/反序列化**：字段需有 getter 方法，建议使用 `@JsonDeserialize` 配置构造函数
3. **自动过滤自身消息**：事件发布服务不会重复消费自身发出的事件
4. **只传播 LOCAL 事件**：从 MQ 消费重新发布的事件（REMOTE 类型）不会再次发送到 MQ，避免无限循环

---

## 配置参考

### 完整配置示例

```yaml
spring:
  application:
    name: my-service

rocketmq:
  name-server: 127.0.0.1:9876
  producer:
    group: my-service-producer-group
    send-msg-timeout: 3000

smart:
  mq:
    # 环境前缀，多环境共用同一 MQ 集群时使用
    prefix: "dev:"
    # Spring Event 桥接 Topic
    event-topic: smart-boot-event
    # Spring Event 桥接 Tag
    event-tag: event
```

### 消费者 Topic 与前缀

> 注意：`SmartBaseConsumer` 中 `@RocketMQMessageListener` 的 topic 配置**不受** `smart.mq.prefix` 影响，前缀仅用于 `SmartMqProducer` 发送侧。如需环境隔离，请在消费者 topic 上同样添加对应的前缀配置。

```java
@RocketMQMessageListener(
    topic = "${smart.mq.prefix:}order-topic",  // 手动处理前缀
    consumerGroup = "my-consumer-group"
)
```

---

## 核心类说明

| 类/接口 | 说明 |
|---------|------|
| `SmartMqProducer` | 消息生产者接口，提供同步/异步/延迟/顺序发送 |
| `SmartMqProducerRocketImpl` | `SmartMqProducer` 的 RocketMQ 实现，自动注入用户上下文 |
| `SmartBaseConsumer<T>` | 消费者基础接口，封装日志、异常、幂等、用户上下文还原 |
| `SmartMqMessage<T>` | 统一消息模型 |
| `AbstractSmartCommonEvent` | Spring Event 桥接的事件基类，业务事件必须继承此类 |
| `SmartSpringEventProducer` | 监听本地 Spring Event，转发到 RocketMQ |
| `SmartSpringEventConsumer` | 消费 RocketMQ 中的事件消息，重新发布为 Spring Event |
| `SmartEventWrapper` | 事件在 MQ 中的传输包装类 |
| `SmartEventHandler<T>` | 事件处理器接口，注册为 Bean 后由 `SmartEventListener` 统一分发 |
| `SmartMqException` | MQ 处理异常，消费失败时抛出触发 RocketMQ 重试 |

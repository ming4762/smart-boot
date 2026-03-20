# smart-exception 异常处理模块说明文档

## 1. 模块概述

`smart-exception` 是 smart-boot 框架的统一异常处理模块，负责拦截全局 HTTP 请求中产生的异常，将其转化为标准响应格式返回，同时支持异步多渠道异常通知（控制台、数据库、远程等）。

**Maven 坐标：**
```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-exception</artifactId>
    <version>5.0.0-SNAPSHOT</version>
</dependency>
```

**Starter 引入（推荐）：**
```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-exception</artifactId>
</dependency>
```

---

## 2. 整体架构

```
HTTP 请求抛出异常
       │
       ▼
GlobalExceptionHandler（@ControllerAdvice 全局拦截）
       │
       ├──► AsyncNoticeHandler（异步多渠道通知）
       │           │
       │           ├── ConsoleExceptionNotice（控制台输出，默认）
       │           ├── DbExceptionNotice（写入数据库，smart-module-system）
       │           └── RemoteExceptionNotice（远程调用写库，smart-cloud 场景）
       │
       └──► ExceptionMessageHandler（异常消息转换为响应体）
                   │
                   └── DefaultExceptionMessageHandler
                               │
                               └── ExceptionMessageProcessor<T>（按异常类型分发处理）
                                           ├── DefaultExceptionHandler（通用兜底）
                                           ├── DefaultBaseExceptionProcessor
                                           ├── DefaultBusinessExceptionProcessor
                                           ├── DefaultI18nExceptionMessageProcessor
                                           ├── DefaultAccessDeniedExceptionProcessor
                                           ├── DefaultBindExceptionProcessor
                                           ├── DefaultMethodArgumentNotValidExceptionProcessor
                                           ├── DefaultConstraintViolationExceptionProcessor
                                           ├── DefaultHandlerMethodValidationExceptionProcessor
                                           ├── DefaultHttpRequestMethodNotSupportedExceptionMessageProcessor
                                           ├── DefaultHttpMediaTypeNotSupportedExceptionProcessor
                                           ├── DefaultHttpMessageNotReadableExceptionMessageProcessor
                                           ├── DefaultMethodArgumentTypeMismatchExceptionMessageProcessor
                                           ├── DefaultNoHandlerFoundExceptionMessageProcessor
                                           └── DefaultAsyncRequestTimeoutExceptionProcessor
```

---

## 3. 核心组件说明

### 3.1 GlobalExceptionHandler

**类：** `com.smart.framework.exception.handler.GlobalExceptionHandler`

全局异常入口，使用 Spring 的 `@ControllerAdvice` + `@ExceptionHandler(Exception.class)` 拦截所有请求异常。

**处理流程：**
1. 对 `UndeclaredThrowableException` 进行解包，提取真实原因异常；
2. 通过 `SmartIdGenerator.nextId()` 为本次异常生成唯一编号 `exceptionNo`（雪花 ID）；
3. 调用 `AsyncNoticeHandler.noticeException()` 进行异步通知；
4. 调用 `ExceptionMessageHandler.message()` 返回响应体。

---

### 3.2 ExceptionMessageHandler / DefaultExceptionMessageHandler

**接口：** `com.smart.framework.exception.handler.ExceptionMessageHandler`  
**默认实现：** `com.smart.framework.exception.handler.DefaultExceptionMessageHandler`

负责将异常对象转换为 HTTP 响应体（`Result` 对象）。

**核心机制：**
- 在 `afterPropertiesSet()` 阶段，从 Spring 容器中收集所有 `ExceptionMessageProcessor<T>` 实现，按泛型类型归类；
- 若同一类型存在多个处理器，取 `order()` 最小的优先执行（并打印 warn 日志）；
- 查找处理器时支持**继承链向上查找**，即子类异常可被父类处理器匹配；
- 最终返回的 `Result` 对象会被封装为 `ExceptionResult`（携带 `exceptionNo`）；
- 若找不到任何匹配处理器，直接返回 `Result.failure("系统发生未知异常")`。

---

### 3.3 ExceptionMessageProcessor

**接口：** `com.smart.framework.exception.processor.ExceptionMessageProcessor<T extends Exception>`

异常消息处理器核心接口，泛型 `T` 指定处理的异常类型。

| 方法 | 说明 |
|------|------|
| `processorType()` | 返回处理的异常类型 |
| `order()` | 处理器优先级，值越小优先级越高（接口默认值为 `0`） |
| `message(T e, long exceptionNo, HttpServletRequest request)` | 返回响应体 |

**抽象基类继承体系：**

```
ExceptionMessageProcessor<T>
    └── AbstractTypeExceptionMessageProcessor<T>   // 通过反射自动获取泛型类型，实现 processorType()
            └── AbstractI18nExceptionMessageProcessor<T>  // 提供 i18nMessage() 工具方法，支持国际化消息
```

---

### 3.4 内置异常处理器一览

所有内置处理器的 `order()` 均返回 `Integer.MAX_VALUE`，便于业务自定义处理器以更小的 order 值优先覆盖。

| 处理器类 | 处理的异常类型 | 说明 |
|----------|--------------|------|
| `DefaultExceptionHandler` | `Exception`（通用兜底） | 记录 error 日志，返回"系统发生未知异常" |
| `DefaultBaseExceptionProcessor` | `BaseException` | 使用异常自身的 code 和 message 构造失败响应 |
| `DefaultBusinessExceptionProcessor` | `BusinessException` | 使用 `BUSINESS_ERROR` 状态码构造失败响应 |
| `DefaultI18nExceptionMessageProcessor` | `I18nException` | 通过 `I18nUtils` 解析国际化消息后返回 |
| `DefaultAccessDeniedExceptionProcessor` | `AccessDeniedException` | 直接重新抛出，交由 Spring Security 的 `AccessDeniedHandler` 处理 |
| `DefaultBindExceptionProcessor` | `BindException` | 提取 `BindingResult` 中的校验错误信息返回 |
| `DefaultMethodArgumentNotValidExceptionProcessor` | `MethodArgumentNotValidException` | 提取 `BindingResult` 中的校验错误信息返回 |
| `DefaultConstraintViolationExceptionProcessor` | `ConstraintViolationException` | 返回 400 BAD_REQUEST（支持 i18n） |
| `DefaultHandlerMethodValidationExceptionProcessor` | `HandlerMethodValidationException` | 提取所有参数校验错误信息，以 `;` 拼接后返回 400 |
| `DefaultNoHandlerFoundExceptionMessageProcessor` | `NoHandlerFoundException` | 记录 error 日志，返回 404 提示（支持 i18n） |
| `DefaultHttpRequestMethodNotSupportedExceptionMessageProcessor` | `HttpRequestMethodNotSupportedException` | 返回 405 HTTP 方法不支持 |
| `DefaultHttpMediaTypeNotSupportedExceptionProcessor` | `HttpMediaTypeNotSupportedException` | 返回 415 Content-Type 不支持 |
| `DefaultHttpMessageNotReadableExceptionMessageProcessor` | `HttpMessageNotReadableException` | 返回请求体读取失败提示 |
| `DefaultMethodArgumentTypeMismatchExceptionMessageProcessor` | `MethodArgumentTypeMismatchException` | 返回参数类型不匹配提示 |
AsyncRequestTimeoutException` | 返回异步请求超时提示 |

---

### 3.5 异步通知机制

**类：** `com.smart.framework.exception.notice.AsyncNoticeHandler`

异常发生后，通过 `CompletableFuture.runAsync()` 异步执行所有注册的 `ExceptionNotice` 实现。

**上下文传播：**
- 使用 `TtlRunnable`（transmittable-thread-local）传播 ThreadLocal 上下文；
- 使用 `DelegatingSecurityContextRunnable` 传播 Spring Security 上下文；
- 使用 Micrometer Tracing 传播当前 Span（支持链路追踪）；
- 手动从请求头提取 Token 并通过 `TokenHolder` 传入异步线程，通知完成后清理。

---

### 3.6 ExceptionNotice 通知接口体系

.notice.ExceptionNotice`

| 方法 | 说明 |
|------|------|
| `notice(ExceptionNoticeDTO)` | 执行通知逻辑（入口） |
| `include()` | 白名单：指定需要通知的异常类型，返回 `null` 则通知所有 |
| `exclude()` | 黑名单：指定不通知的异常类型，默认返回空列表 |

**抽象基类继承体系：**

```
ExceptionNotice
    └── AbstractExceptionNotice          // 实现 include/exclude 过滤逻辑，满足条件才调用 doNotice()
            └── AbstractCommonExcludeExceptionNotice  // 预置通用排除列表（参数校验类、业务异常、权限异常）
                    ├── ConsoleExceptionNotice         // 控制台输出（默认，无其他 ExceptionNotice Bean 时注册）
                    ├── DbExceptionNotice              // 写入数据库（smart-module-system，bean name = dbExceptionNotice）
                    └── RemoteExceptionNotice          // 远程调用写库（smart-cloud-starter-exception）
```

**`AbstractCommonExcludeExceptionNotice` 默认排除的异常类型：**

| 排除的异常类 | 原因 |
|-------------|------|
| `BindException` | 参数校验类异常，无需通知 |
| `MethodArgumentNotValidException` | 参数校验类异常，无需通知 |
| `ConstraintViolationException` | 参数校验类异常，无需通知 |
| `BusinessException` | 业务异常，无需通知 |
| `I18nException` | 国际化业务异常，无需通知 |
| `AccessDeniedException` | 权限拒绝异常，无需通知 |
| `HandlerMethodValidationException` | 接口参数校验异常，无需通知 |

---

### 3.7 ExceptionNoticeDTO

**类：** `com.smart.framework.exception.pojo.dto.ExceptionNoticeDTO`

通知时携带的异常上下文信息：

| 字段 | 类型 | 说明 |
|------|------|------|
| `exception` | `Exception` | 异常对象 |
| `exceptionNo` | `Long` | 异常唯一编号（雪花 ID） |
| `requestIp` | `String` | 请求客户端 IP |
| `requestPath` | `String` | 请求路径（`request.getServletPath()`） |

---

## 4. 关联的异常基类体系（smart-commons-core）

```
RuntimeException
    └── BaseException（code + message + cause，默认 code = 500）
            ├── SystemException（系统异常）
            ├── BusinessException（业务异常，固定使用 BUSINESS_ERROR 状态码）
            │       ├── I18nException（国际化业务异常，携带 I18nMessage + args）
            │       └── RateLimitException（限流异常）
```

**BaseException 构造方式：**

```java
// 仅消息
new BaseException("错误信息");

// 消息 + 原因
new BaseException("错误信息", cause);

// 自定义 code + 消息 + 原因
new BaseException(400, "错误信息", cause);
```

**I18nException 快捷抛出：**

```java
// 通过静态方法直接抛出

I18nException.of(MyI18nMessage.SOME_ERROR, cause);
```

---

## 5. 自动配置（smart-boot-autoconfigure）

`SmartExceptionAutoConfiguration` 在 `@ConditionalOnClass(GlobalExceptionHandler.class)` 条件下生效，负责自动注册以下 Bean：

| Bean | 条件 | 说明 |
|------|------|------|
| `AsyncNoticeHandler` | 无条件 | 异步通知调度器 |
| `DefaultExceptionMessageHandler` | `@ConditionalOnMissingBean(ExceptionMessageHandler.class)` | 可自定义替换 |
| `GlobalExceptionHandler` | `@ConditionalOnMissingBean(GlobalExceptionHandler.class)` | 可自定义替换 |
| `ConsoleExceptionNotice` | `@ConditionalOnMissingBean(ExceptionNotice.class)` | 无其他通知实现时启用 |

`ExceptionMessageProcessorBeanConfiguration`（通过 `@Import` 引入）负责注册所有内置 `ExceptionMessageProcessor` Bean。

---

## 6. 关联模块

### 6.1 smart-module-system（数据库持久化）

`smart-module-system` 模块提供 `DbExceptionNotice`（bean name = `dbExceptionNotice`），将异常写入 `sys_exception` 表。

**sys_exception 表字段：**

| 字段 | 说明 |
|------|------|
| id | 异常编号（雪花 ID，即 exceptionNo） |
| exceptionMessage | 异常信息（`exception.toString()`） |
| stackTrace | 完整堆栈信息 |
| reque| 请求客户端 IP |
| serverIp | 服务端 IP（`InetAddress.getLocalHost().getHostAddress()`） |
| requestPath | 请求路径 |
| operateUserId | 操作人员 ID |
| operationBy | 操作人员名称 |
| createTime | 创建时间 |
| userFeedback | 用户是否反馈 |

### 6.2 smart-cloud-starter-exception（微服务场景）

微服务场景下，各子服务引入 `smart-cloud-starter-exception`，通过 `RemoteExceptionNotice` 调用系统服务 API（`SysExceptionApi`）保存异常，与单体模式的 `DbExceptionNotice` 逻辑完全一致，区别仅在于通过 Feign 远程调用。

> 若容器"dbExceptionNotice")`）。

**微服务场景引入：**
```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-cloud-starter-exception</artifactId>
</dependency>
```

---

## 7. 扩展指南

### 7.1 自定义异常处理器

实现 `ExceptionMessageProcessor<T>` 接口，注册为 Spring Bean 即可。设置较小的 `order()` 值可覆盖内置处理器：

```java
@Component
public class MyCustomExceptionProcessor extends AbstractTypeExceptionMessageProcessor<MyException> {

    @Override
    public int order() {
        return 0; // 小于内置处理器的 Integer.MAX_VALUE，优先执行
    }

    @Override
    public Object message(MyException e, long exceptionNo, HttpServletRequest request) {
        return Result.failure("自定义业务错误：" + e.getMessage());
    }
}
```

### 7.2 自定义异常通知渠道

继承 `AbstractCommonExcludeExceptionNotice`（自动排除常规低优先异常），注册为 Bean：

```java
@Component
public class DingTalkExceptionNotice extends AbstractCommonExcludeExceptionNotice {

    @Override
    protected void doNotice(@NonNull ExceptionNoticeDTO exceptionData) {
        // 发送钉钉告警
        String m\n信息：%s",
                exceptionData.getExceptionNo(),
                exceptionData.getRequestPath(),
                exceptionData.getException().getMessage());
        // dingTalkClient.send(msg);
    }
}
```

如需完全控制通知过滤逻辑，继承 `AbstractExceptionNotice` 并重写 `include()` / `exclude()`：

```java
@Component
public class MyNotice extends AbstractExceptionNotice {

中返回给前端，便于用户反馈时定位问题；
2. 作为 `sys_exception` 表的主键 ID 存储；
3. 在异步通知日志中打印，便于问题追踪。
   protected void doNotice(@NonNull ExceptionNoticeDTO exceptionData) {
        // 通知逻辑
    }
}
```

### 7.3 替换全局异常 Handler

注册自定义 `ExceptionMessageHandler` Bean 即可（`@ConditionalOnMissingBean` 会跳过默认实现）：

```java
@Bean
public ExceptionMessageHandler myExceptionMessageHandler() {
    return (e, exceptionNo, request) -> {
        // 自定义逻辑
        return Result.failure(e.getMessage());
    };
}
```

---

## 8. 异常编号说明

每次异常均由 `SmartIdGenerator.nextId()`（雪花算法）生成唯一 `exceptionNo`，该编号会：

1. 携带在响应体的 `ExceptionResult`     @Override
    public List<Class<? extends Exception>> include() {
        return List.of(SystemException.class); // 只通知系统异常
    }

    @Override
 
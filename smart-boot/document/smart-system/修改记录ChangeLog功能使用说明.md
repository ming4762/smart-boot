## 修改记录ChangeLog功能使用说明

> 改功能提供后台API和前台组件

### 一、后台集成

#### 1、引入maven

```xml
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-system-api</artifactId>
    <version>${smart-boot.version}</version>
</dependency>
```

#### 2、使用SysToolApi保存修改记录

> spring注入com.smart.module.api.system.SysToolApi
>
> SysToolApi提供4个接口保存修改记录
>
> <span style="color:red">建议使用接口4<span>

##### 2.1 接口1

```java
/**
 * 保存修改记录
 * @param parameter 参数
 * @return 是否保存成功
 */
boolean saveChangeLog(RemoteChangeLogSaveParameter parameter);
```

##### 2.2 接口2

> 该接口自动比对数据差异

```java
/**
 * 保存修改记录
 * @param parameter 参数
 * @param beforeData 原数据
 * @param afterData 修改后的数据
 * @return 是否修改成功
 * @param <T> 泛型
 */
<T> boolean saveChangeLog(CommonChangeLogSaveParameter parameter, @Nullable T beforeData, @Nullable T afterData);
```

##### 2.3 接口3

> 该接口在接口2的基础上，可以指定哪些字段需要保存修改记录哪些字段不需要保存修改记录

```java
/**
 * 保存修改记录
 * @param parameter 参数
 * @param beforeData 原数据
 * @param afterData 修改后的数据
 * @param fieldList 保存的字段列表，null则保存所有
 * @param excludeList 排除的字段列表
 * @return 是否修改成功
 * @param <T> 泛型
 */
<T> boolean saveChangeLog(CommonChangeLogSaveParameter parameter, @Nullable T beforeData, @Nullable T afterData, Set<String> fieldList, Set<String> excludeList);
```

##### 2.4 接口4

```java
/**
 * 保存修改记录
 * @param parameter 参数
 * @param beforeData 原数据
 * @param afterData 修改后的数据
 * @param fieldList 保存的字段列表，null则保存所有
 * @param excludeList 排除的字段列表
 * @return 是否修改成功
 * @param <T> 泛型
 */
<T> boolean saveChangeLog(CommonChangeLogSaveParameter parameter, @Nullable T beforeData, @Nullable T afterData, List<Func1<T, ?>> fieldList, List<Func1<T, ?>> excludeList);
```




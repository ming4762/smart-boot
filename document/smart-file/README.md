# smart-file 统一文件管理模块

## 模块概述

`smart-file` 是 smart-boot 框架中的统一文件存储封装模块，提供统一的文件操作接口，支持多种文件存储后端，屏蔽各存储平台的实现差异，业务层只需依赖核心接口即可完成文件的上传、下载、删除和访问地址获取操作。

**版本**：`5.0.0-SNAPSHOT`  
**GroupId**：`com.smart`

---

## 模块结构

```
smart-file
├── smart-file-core                      # 核心模块（接口定义、参数对象、属性配置）
├── smart-file-extensions-disk           # 本地磁盘存储扩展
├── smart-file-extensions-ftp            # FTP 存储扩展
├── smart-file-extensions-sftp           # SFTP 存储扩展
├── smart-file-extensions-minio          # MinIO 对象存储扩展
├── smart-file-extensions-aliyun-oss     # 阿里云 OSS 扩展
├── smart-file-extensions-s3             # Amazon S3 扩展
└── smart-file-extensions-qiniu          # 七牛云存储扩展
```

---

## 核心设计

### 核心接口

#### `FileStorageService` — 底层存储器服务接口

每个存储后端均实现此接口，提供原子级文件操作能力：

| 方法 | 说明 |
|------|------|
| `save(InputStream, FileStorageSaveParameter)` | 保存文件，返回存储标识 |
| `delete(FileStorageDeleteParameter)` | 删除文件 |
| `download(FileStorageGetParameter)` | 下载文件，返回输入流 |
| `download(FileStorageGetParameter, OutputStream)` | 下载文件并写入输出流（默认方法） |
| `getAddress(FileStorageGetParameter)` | 获取文件访问地址 |
| `init(FileStorageInitProperties)` | 初始化存储器配置 |
| `destroy(Long fileStorageId)` | 销毁指定存储器实例 |
| `getRegisterName()` | 获取存储器注册名称 |

#### `FileService` — 上层文件服务接口

面向业务层，提供更高层次的文件管理能力（通常结合数据库元数据使用）：

| 方法 | 说明 |
|------|------|
| `save(MultipartFile, FileSaveParameter)` | 保存 Web 上传文件 |
| `save(InputStream, FileSaveParameter)` | 保存输入流文件 |
| `save(File, FileSaveParameter)` | 保存本地文件 |
| `delete(Long fileId)` | 删除单个文件 |
| `batchDelete(Collection<Long>)` | 批量删除文件 |
| `download(Long id)` | 根据 ID 下载文件 |
| `download(Long id, OutputStream)` | 下载文件并写入输出流 |
| `download(String fileStorageCode, String filename)` | 通过存储器代码和文件名下载 |
| `listAddress(List<Long>)` | 批量获取文件访问地址 |

---

## 参数对象说明

### `FileStorageSaveParameter` — 文件保存参数

| 字段 | 类型 | 说明 |
|------|------|------|
| `fileStorageId` | `Long` | 存储器 ID（继承自 `FileStorageCommonParameter`） |
| `filename` | `String` | 文件名，为空则使用原始文件名 |
| `folder` | `String` | 存储目录，为空则使用当天日期（`yyyy/MM/dd`） |
| `useOriginalFilename` | `boolean` | 是否使用原始文件名（不加时间戳后缀） |

### `FileStorageDeleteParameter` — 文件删除参数

| 字段 | 类型 | 说明 |
|------|------|------|
| `fileStorageId` | `Long` | 存储器 ID |
| `fileStoreList` | `List<FileStorageDeleteItem>` | 待删除文件列表 |

`FileStorageDeleteItem` 字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| `fileStoreKey` | `String` | 文件存储 Key |
| `encryptedYn` | `boolean` | 是否加密存储 |

### `FileStorageGetParameter` — 文件查询/下载参数

| 字段 | 类型 | 说明 |
|------|------|------|
| `fileStorageId` | `Long` | 存储器 ID |
| `storageStoreKey` | `String` | 文件存储 Key |
| `encryptedYn` | `boolean` | 是否加密 |

### `FileStorageInitProperties` — 存储器初始化参数

| 字段 | 类型 | 说明 |
|------|------|------|
| `fileStorageId` | `Long` | 存储器 ID |
| `properties` | `String` | 存储器配置（JSON 字符串） |
| `encryptedYn` | `boolean` | 是否加密 |
| `privateKey` | `String` | 私钥（加密模式使用） |
| `publicKey` | `String` | 公钥（加密模式使用） |

### `FileStorageSaveResult` — 文件保存结果

| 字段 | 类型 | 说明 |
|------|------|------|
| `fileStoreKey` | `String` | 文件存储 Key（用于后续下载/删除） |
| `fileStorageId` | `Long` | 存储器 ID |
| `encryptedYn` | `boolean` | 是否加密存储 |

---

## 各扩展模块说明

### 1. 本地磁盘（`smart-file-extensions-disk`）

实现类：`FileStorageDiskServiceImpl`

**配置属性（`SmartFileStorageDiskProperties`）：**

| 属性 | 说明 |
|------|------|
| `basePath` | 本地磁盘存储根路径 |

**特性说明：**
- 文件按 `yyyy/MM/dd` 日期目录自动分级存储
- 文件名自动追加时间戳以避免重名，可通过 `useOriginalFilename=true` 关闭
- 使用 Base64 编码的 `fileId` 作为存储 Key，内含目录、时间戳、文件名信息
- 暂不支持加密存储

---

### 2. FTP（`smart-file-extensions-ftp`）

实现类：`FileStorageFtpServiceImpl`

**配置属性（`SmartFileStorageFtpProperties`）：**

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `host` | — | FTP 服务器地址 |
| `port` | `21` | FTP 端口 |
| `basePath` | — | 存储根路径 |
| `username` | — | 用户名 |
| `password` | — | 密码 |
| `encoding` | — | 编码格式 |

**特性说明：**
- 使用 Apache Commons Pool 管理 FTP 连接池，提升性能

---

### 3. SFTP（`smart-file-extensions-sftp`）

实现类：`FileStorageNfsServiceImpl`

**配置属性（`SmartFileStorageSftpProperties`）：**

| 属性 | 默认值 | 说明 |
|------|--------|------|
| `host` | — | SFTP 服务器地址 |
| `port` | `22` | SFTP 端口 |
| `basePath` | — | 存储根路径 |
| `username` | — | 用户名 |
| `password` | — | 密码（与 privateKey 二选一）|
| `privateKey` | — | 私钥路径（与 password 二选一）|

**特性说明：**
- 基于 JSch 库实现 SSH/SFTP 通道
- 支持密码和私钥两种认证方式
- 使用连接池管理 Session 和 Channel

---

### 4. MinIO（`smart-file-extensions-minio`）

接口：`MinioService`，实现类：`FileStorageMinioServiceImpl`

**配置属性（`SmartFileStorageMinioProperties`）：**

| 属性 | 说明 |
|------|------|
| `endpoint` | MinIO 服务地址 |
| `accessKey` | Access Key |
| `secretKey` | Secret Key |
| `bucketName` | 默认存储桶名称 |

**扩展能力（`MinioService` 接口）：**

| 方法 | 说明 |
|------|------|
| `bucketExists(parameter, bucketName)` | 判断存储桶是否存在 |
| `makeBucket(parameter, bucketName)` | 创建存储桶 |
| `removeBucket(parameter, bucketName)` | 删除存储桶 |
| `listBuckets(parameter)` | 查询存储桶列表 |
| `save(parameter, bucketName, file/inputStream)` | 上传文件到指定存储桶 |
| `getObjectUrl(parameter, bucketName, expiry)` | 获取带过期时间的文件外链 |
| `download(parameter, bucketName)` | 从指定存储桶下载文件 |

---

### 5. 阿里云 OSS（`smart-file-extensions-aliyun-oss`）

接口：`AliyunOssService`，实现类：`AliyunOssServiceImpl`

**配置属性（`SmartFileStorageAliyunOssProperties`）：**

| 属性 | 说明 |
|------|------|
| `endpoint` | OSS Endpoint 地址 |
| `accessKey` | Access Key ID |
| `secretKey` | Access Key Secret |
| `bucketName` | 默认 Bucket 名称 |

**扩展能力（`AliyunOssService` 接口）：**

| 方法 | 说明 |
|------|------|
| `getOssClient(id)` | 获取 OSS 客户端实例 |
| `save(parameter, bucketName, file/inputStream)` | 上传文件到指定 Bucket |
| `delete(parameter, bucketName)` | 从指定 Bucket 删除文件 |
| `download(parameter, bucketName)` | 从指定 Bucket 下载文件 |

---

### 6. Amazon S3（`smart-file-extensions-s3`）

接口：`AmazonS3Service`，实现类：`DefaultAmazonS3ServiceImpl`

**配置属性（`SmartFileStorageAmazonS3Properties`）：**

| 属性 | 说明 |
|------|------|
| `endpoint` | S3 兼容服务地址 |
| `accessKey` | Access Key |
| `secretKey` | Secret Key |
| `bucketName` | 默认 Bucket 名称 |

> 支持 Amazon S3 协议兼容的对象存储服务（如 AWS S3、华为 OBS 等）

---

### 7. 七牛云（`smart-file-extensions-qiniu`）

接口：`QiniuService`，实现类：`FileStorageQiniuServiceImpl`

**配置属性（`SmartFileStorageQiniuProperties`）：**

| 属性 | 说明 |
|------|------|
| `accessKey` | Access Key |
| `secretKey` | Secret Key |
| `bucketName` | 存储桶名称 |
| `region` | 区域（如 `z0`、`z1`、`na0` 等）|
| `url` | 文件访问域名 |
| `useHttps` | 是否使用 HTTPS |

---

## 存储类型枚举

系统通过 `FileStorageTypeEnum` 区分存储类型：

| 枚举值 | 存储类型 |
|--------|----------|
| `DISK` | 本地磁盘 |
| `FTP` | FTP |
| `SFTP` | SFTP/NFS |
| `MINIO` | MinIO |
| `ALIYUN_OSS` | 阿里云 OSS |
| `AMAZON_S3` | Amazon S3 |
| `QINIU` | 七牛云 |

---

## Maven 依赖引入

### Spring Boot 应用（推荐使用 Starter）

`smart-boot-starter-file` 是对各扩展模块的 Spring Boot Starter 封装，位于 `smart-boot-framework/smart-boot-starters/smart-boot-starter-file`。**Spring Boot 应用推荐直接引入 Starter**，无需单独引入底层扩展模块。

**Starter 与扩展模块对应关系：**

| Starter artifactId | 对应扩展模块 | 说明 |
|---|---|---|
| `smart-boot-starter-file-disk` | `smart-file-extensions-disk` | 本地磁盘存储 |
| `smart-boot-starter-file-ftp` | `smart-file-extensions-ftp` | FTP 存储 |
| `smart-boot-starter-file-sftp` | `smart-file-extensions-sftp` | SFTP 存储 |
| `smart-boot-starter-file-minio` | `smart-file-extensions-minio` | MinIO 对象存储 |
| `smart-boot-starter-file-aliyun` | `smart-file-extensions-aliyun-oss` | 阿里云 OSS |
| `smart-boot-starter-file-s3` | `smart-file-extensions-s3` | Amazon S3 |
| `smart-boot-starter-file-qiniu` | `smart-file-extensions-qiniu` | 七牛云存储 |

**Spring Boot 应用按需引入 Starter：**

```xml
<!-- 本地磁盘 -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-disk</artifactId>
</dependency>

<!-- FTP -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-ftp</artifactId>
</dependency>

<!-- SFTP -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-sftp</artifactId>
</dependency>

<!-- MinIO -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-minio</artifactId>
</dependency>

<!-- 阿里云 OSS -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-aliyun</artifactId>
</dependency>

<!-- Amazon S3 -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-s3</artifactId>
</dependency>

<!-- 七牛云 -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-boot-starter-file-qiniu</artifactId>
</dependency>
```

### 非 Spring Boot 应用（直接引入扩展模块）

```xml
<!-- 核心模块（必选） -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-core</artifactId>
</dependency>

<!-- 本地磁盘 -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-disk</artifactId>
</dependency>

<!-- FTP -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-ftp</artifactId>
</dependency>

<!-- SFTP -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-sftp</artifactId>
</dependency>

<!-- MinIO -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-minio</artifactId>
</dependency>

<!-- 阿里云 OSS -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-aliyun-oss</artifactId>
</dependency>

<!-- Amazon S3 -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-s3</artifactId>
</dependency>

<!-- 七牛云 -->
<dependency>
    <groupId>com.smart</groupId>
    <artifactId>smart-file-extensions-qiniu</artifactId>
</dependency>
```

> 版本通过父 BOM（`smart-boot` 版本管理）统一管理，无需单独指定。

---

## 使用示例

### 上传文件

```java
@Autowired
private FileService fileService;

// 上传 MultipartFile（常用于 Web 接口）
FileHandlerResult result = fileService.save(multipartFile, FileSaveParameter.builder()
        .fileStorageCode("your-storage-code")
        .folder("upload/images")
        .build());

// 上传本地文件
FileHandlerResult result2 = fileService.save(new File("/tmp/test.pdf"), 
        FileSaveParameter.builder().fileStorageCode("your-storage-code").build());
```

### 下载文件

```java
// 通过文件 ID 下载
FileDownloadResult download = fileService.download(fileId);
InputStream inputStream = download.getInputStream();

// 下载并写入响应流
fileService.download(fileId, response.getOutputStream());
```

### 删除文件

```java
// 删除单个文件
fileService.delete(fileId);

// 批量删除
fileService.batchDelete(List.of(fileId1, fileId2, fileId3));
```

### 获取访问地址

```java
List<String> addresses = fileService.listAddress(List.of(fileId1, fileId2));
```

---

## 扩展新的存储类型

1. 在 `smart-file-extensions-xxx` 中创建新模块
2. 实现 `FileStorageService` 接口
3. 在 `getRegisterName()` 中返回对应的 `FileStorageTypeEnum` 枚举值
4. 在 `init(FileStorageInitProperties)` 中解析 JSON 配置并缓存
5. 将实现类注册为 Spring Bean

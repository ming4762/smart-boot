## SMART-AUTH ACCESS SECRET认证方式说明

## 1、Header添加签名说明

需要生成签名信息放到header  Authorization

请求头中需要添加的参数：CONTENT_TYPE，DATE，NONCE

## 2、生成签名方法

```java
String Authorization = "JFHD" + ":" + ACCESS_KEY + ":" + signature;
String signature = base64(hmac-sha1(SECRET_KEY, String.join(":", List.of(HTTP_METHOD, CONTENT_TYPE, DATE, NONCE))))
```

#### 参数说明：

序号 | 参数 | 类型 | 说明
-- | -- | -- | --
1 |  ACCESS_KEY|String|
2 | SECRET_KEY |String|
3 | HTTP_METHOD |PUT、GET、POST、HEAD、DELETE、OPTIONS|HTTP请求头
4 | CONTENT_TYPE |String|请求类型，例如：application/json
5 | DATE |String|请求的时间，格式为GMT，且与服务器时间差值不能大于10分钟，请求头key为：Date
6 | NONCE |String|随机字符串，无论请求成功失败，只能使用一次，请求头的key为：nonce

#### 代码示例

```java
String httpMethod = request.getMethod();
String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
String date = request.getHeader(HttpHeaders.DATE);
String nonce = request.getHeader("nonce");
String encryptKey = String.join(":", List.of(HTTP_METHOD, CONTENT_TYPE, DATE, NONCE));

String signature = base64(hmac-sha1(SECRET_KEY, encryptKey))
  
String Authorization = "JFHD:" + ACCESS_KEY + ":" + signature;

// hmacSha1 加密
public static byte[] hmacSha1Encrypt(String encryptContent, String encryptKey) {

    byte[] keyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, HMAC_SHA1);

    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(secretKeySpec);
    byte[] contentBytes = encryptContent.getBytes(StandardCharsets.UTF_8);

    return mac.doFinal(contentBytes);
}

// base64编码
public static String encode(@NonNull byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
}
```


package com.smart.framework.commons.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.framework.commons.core.exception.SystemException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RestUtils 同步环境测试用例
 * 使用 MockWebServer 模拟 HTTP 服务，所有调用均在普通 JUnit 线程（同步环境）中执行
 */
class RestUtilsTest {

    private MockWebServer mockWebServer;
    private String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        baseUrl = mockWebServer.url("/").toString();
        // 去掉末尾斜杠
        baseUrl = baseUrl.substring(0, baseUrl.length() - 1);

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        RestUtils.setWebClient(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    // ===================== rest() 方法测试 =====================

    @Test
    void rest_get_shouldReturnParsedResponse() throws Exception {
        // 构造响应体
        Map<String, Object> responseBody = Map.of("id", 1, "name", "张三");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(responseBody)));

        // 执行 GET 请求（同步调用）
        Map<String, Object> result = RestUtils.rest(
                baseUrl + "/api/user",
                HttpMethod.GET,
                null,
                null,
                new ParameterizedTypeReference<>() {},
                null
        );

        assertNotNull(result);
        assertEquals("张三", result.get("name"));

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/api/user", request.getPath());
    }

    @Test
    void rest_post_shouldSendBodyAndReturnResponse() throws Exception {
        // 构造响应体
        Map<String, Object> responseBody = Map.of("code", 0, "message", "success");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(responseBody)));

        // 构造请求体
        Map<String, Object> requestBody = Map.of("username", "admin", "password", "123456");

        Map<String, Object> result = RestUtils.rest(
                baseUrl + "/api/login",
                HttpMethod.POST,
                Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                requestBody,
                new ParameterizedTypeReference<>() {},
                null
        );

        assertNotNull(result);
        assertEquals(0, result.get("code"));
        assertEquals("success", result.get("message"));

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertEquals("/api/login", request.getPath());
        // 验证请求体包含 username
        assertTrue(request.getBody().readUtf8().contains("admin"));
    }

    @Test
    void rest_withCustomHeaders_shouldSendHeaders() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"status\":\"ok\"}"));

        Map<String, Object> result = RestUtils.rest(
                baseUrl + "/api/secure",
                HttpMethod.GET,
                Map.of("Authorization", "Bearer test-token", "X-Custom-Header", "custom-value"),
                null,
                new ParameterizedTypeReference<>() {},
                null
        );

        assertNotNull(result);
        assertEquals("ok", result.get("status"));

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("Bearer test-token", request.getHeader("Authorization"));
        assertEquals("custom-value", request.getHeader("X-Custom-Header"));
    }

    @Test
    void rest_withUriVariables_shouldExpandUrlTemplate() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"id\":42}"));

        Map<String, Object> result = RestUtils.rest(
                baseUrl + "/api/user/{id}",
                HttpMethod.GET,
                null,
                null,
                new ParameterizedTypeReference<>() {},
                Map.of("id", "42")
        );

        assertNotNull(result);
        assertEquals(42, result.get("id"));

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("/api/user/42", request.getPath());
    }

    @Test
    void rest_serverError_shouldThrowSystemException() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody("Internal Server Error"));

        assertThrows(SystemException.class, () ->
                RestUtils.rest(
                        baseUrl + "/api/error",
                        HttpMethod.GET,
                        null,
                        null,
                        new ParameterizedTypeReference<Map<String, Object>>() {},
                        null
                )
        );
    }

    @Test
    void rest_returnsListResponse() throws Exception {
        String jsonBody = "[{\"id\":1,\"name\":\"item1\"},{\"id\":2,\"name\":\"item2\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody(jsonBody));

        List<Map<String, Object>> result = RestUtils.rest(
                baseUrl + "/api/items",
                HttpMethod.GET,
                null,
                null,
                new ParameterizedTypeReference<>() {},
                null
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("item1", result.get(0).get("name"));
        assertEquals("item2", result.get(1).get("name"));
    }

    // ===================== restForm() 方法测试 =====================

    @Test
    void restForm_shouldSendFormDataAndReturnResponse() throws Exception {
        Map<String, Object> responseBody = Map.of("uploaded", true, "fileName", "test.txt");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody(objectMapper.writeValueAsString(responseBody)));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("field1", "value1");
        formData.add("field2", "value2");

        Map<String, Object> result = RestUtils.restForm(
                baseUrl + "/api/upload",
                HttpMethod.POST,
                null,
                formData,
                new ParameterizedTypeReference<>() {},
                null
        );

        assertNotNull(result);
        assertTrue((Boolean) result.get("uploaded"));

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        // 验证 Content-Type 为 multipart/form-data
        String contentType = request.getHeader("Content-Type");
        assertNotNull(contentType);
        assertTrue(contentType.startsWith("multipart/form-data"));
    }

    @Test
    void restForm_withNullParameter_shouldNotThrow() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"result\":\"ok\"}"));

        Map<String, Object> result = RestUtils.restForm(
                baseUrl + "/api/form",
                HttpMethod.POST,
                null,
                null,
                new ParameterizedTypeReference<>() {},
                null
        );

        assertNotNull(result);
        assertEquals("ok", result.get("result"));
    }

    // ===================== restReactive() 方法测试 =====================

    @Test
    void restReactive_shouldReturnFluxAndBlockCollect() {
        String jsonBody = "[{\"id\":1},{\"id\":2},{\"id\":3}]";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody(jsonBody));

        // 在同步环境中，通过 collectList().block() 获取结果
        // Flux<Map> 逐条 emit，每条为一个 Map 对象
        List<Map<String, Object>> result = RestUtils.<Map<String, Object>>restReactive(
                baseUrl + "/api/stream",
                HttpMethod.GET,
                null,
                null,
                new ParameterizedTypeReference<>() {},
                null
        ).collectList().block();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).get("id"));
        assertEquals(2, result.get(1).get("id"));
        assertEquals(3, result.get(2).get("id"));
    }

    // ===================== download() 方法测试 =====================

    @Test
    void download_shouldWriteContentToOutputStream() throws Exception {
        byte[] fileContent = "Hello, World! This is a test file.".getBytes();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .setBody(new String(fileContent)));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CountDownLatch latch = RestUtils.download(
                baseUrl + "/api/file/download",
                HttpMethod.GET,
                null,
                null,
                outputStream,
                null
        );

        // 同步等待下载完成
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "下载未在超时时间内完成");
        assertArrayEquals(fileContent, outputStream.toByteArray());

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/api/file/download", request.getPath());
    }

    @Test
    void download_withPostMethod_shouldSendRequestBody() throws Exception {
        byte[] fileContent = "PDF content bytes".getBytes();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_PDF_VALUE)
                .setBody(new String(fileContent)));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, Object> requestBody = Map.of("reportId", "RPT001");

        CountDownLatch latch = RestUtils.download(
                baseUrl + "/api/report/export",
                HttpMethod.POST,
                Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                requestBody,
                outputStream,
                null
        );

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "下载未在超时时间内完成");

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertTrue(request.getBody().readUtf8().contains("RPT001"));
    }

    // ===================== setWebClient / getWebClient 测试 =====================

    @Test
    void setWebClient_shouldReplaceExistingWebClient() {
        WebClient newWebClient = WebClient.create();
        RestUtils.setWebClient(newWebClient);
        assertSame(newWebClient, RestUtils.getWebClient());
    }

}

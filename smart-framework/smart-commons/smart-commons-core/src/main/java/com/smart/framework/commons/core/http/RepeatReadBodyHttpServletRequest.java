package com.smart.framework.commons.core.http;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * body可重复读取的HttpServletRequest
 * @author shizhongming
 * 2025/9/9 17:13
 * @since 5.0.0
 */
public class RepeatReadBodyHttpServletRequest extends HttpServletRequestWrapper {
    private final byte[] body;

    public RepeatReadBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream inputStream = request.getInputStream();
        body = inputStream.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // do nothing
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 获取body
     * @return body
     */
    public byte[] getBody() {
        return this.body;
    }
}

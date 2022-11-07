package com.smart.commons.core.utils;

import com.smart.commons.core.message.Result;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shizhongming
 * 2020/1/16 9:25 下午
 */
public class RestJsonWriter {

    private RestJsonWriter() {
        throw new IllegalStateException("Utility class");
    }

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    public static void writeJson(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(JsonUtils.toJsonString(result));
    }
}

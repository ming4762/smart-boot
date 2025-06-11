package com.smart.cloud.starter.feign.codec;

import com.smart.cloud.starter.feign.exception.SmartFeignBusinessException;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 业务错误解码器
 * 解决统一异常拦截后导致的无法降级的问题
 * @author shizhongming
 * 2025/6/11 10:54
 * @since 5.0.0
 */
public class BusinessDecoder extends ResponseEntityDecoder {

    private static final String CODE_KEY = "code";
    private static final String SUCCESS_CODE = "200";

    public BusinessDecoder(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Object data = super.decode(response, type);
        if (data instanceof Map<?,?> map && map.containsKey(CODE_KEY) && !SUCCESS_CODE.equals(map.get(CODE_KEY))) {
            throw new SmartFeignBusinessException(data);
        }
        return data;
    }
}

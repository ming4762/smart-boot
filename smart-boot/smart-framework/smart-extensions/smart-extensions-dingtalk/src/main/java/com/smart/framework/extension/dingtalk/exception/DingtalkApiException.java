package com.smart.framework.extension.dingtalk.exception;

import com.taobao.api.TaobaoResponse;
import lombok.Getter;

import java.io.Serial;

/**
 * 钉钉接口调用异常
 * @author shizhongming
 * 2024/4/28 16:22
 * @since 3.0.0
 */
@Getter
public class DingtalkApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3752290778515026166L;

    private TaobaoResponse taobaoResponse;

    public DingtalkApiException() {
        super();
    }


    public DingtalkApiException(String message) {
        super(message);
    }


    public DingtalkApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public DingtalkApiException(TaobaoResponse taobaoResponse) {
        super(taobaoResponse.getMsg());
        this.taobaoResponse = taobaoResponse;
    }
}

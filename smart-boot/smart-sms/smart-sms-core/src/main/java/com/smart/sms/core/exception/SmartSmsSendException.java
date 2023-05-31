package com.smart.sms.core.exception;

/**
 * 短信发送异常
 * @author zhongming4762
 * 2023/5/30
 */
public class SmartSmsSendException extends SmartSmsException {


    public SmartSmsSendException() {
        super();
    }

    public SmartSmsSendException(String message) {
        super(message);
    }

    public SmartSmsSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartSmsSendException(Throwable cause) {
        super(cause);
    }
}

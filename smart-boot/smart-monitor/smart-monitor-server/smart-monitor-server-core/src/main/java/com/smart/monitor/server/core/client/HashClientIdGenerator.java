package com.smart.monitor.server.core.client;

import com.smart.monitor.core.model.Application;
import com.smart.monitor.server.common.model.ClientId;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 通过客户端名字和URLhash
 * @author shizhongming
 * 2021/3/21 10:44 下午
 */
public class HashClientIdGenerator implements ClientIdGenerator {

    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    @SneakyThrows
    @Override
    public ClientId create(Application application) {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] bytes = digest.digest((application.getApplicationName() + application.getClientUrl()).getBytes(StandardCharsets.UTF_8));
        return ClientId.create(new String(this.encodeHex(bytes, 0, 12)));
    }


    private char[] encodeHex(byte[] bytes, int offset, int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i = i + 2) {
            byte b = bytes[offset + (i / 2)];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }

}

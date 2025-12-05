package com.smart.framework.commons.validate.utils;

import com.smart.framework.commons.validate.exception.ValidateException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author shizhongming
 * 2025/10/16 13:54
 * @since 5.0.0
 */
class ValidatorUtilsTest {

    @Test
    void testCheckMobile() {
        // 有效手机号
        assertTrue(ValidatorUtils.checkMobile("13812345678"));
        assertTrue(ValidatorUtils.checkMobile("+8613812345678"));

        // 无效手机号
        assertFalse(ValidatorUtils.checkMobile("23812345678"));
        assertFalse(ValidatorUtils.checkMobile("1381234567"));
        assertFalse(ValidatorUtils.checkMobile("138123456789"));
    }

    @Test
    void testCheckPhone() {
        // 有效座机
        assertTrue(ValidatorUtils.checkPhone("010-12345678"));
        assertTrue(ValidatorUtils.checkPhone("0571-1234567"));
        assertTrue(ValidatorUtils.checkPhone("+8610-12345678"));
        assertTrue(ValidatorUtils.checkPhone("01012345678"));

        // 无效座机
        assertFalse(ValidatorUtils.checkPhone("010-123456"));
    }

    @Test
    void testCheckIdCard() {
        // 15位身份证
        assertTrue(ValidatorUtils.checkIdCard("130503670401001"));
        // 18位身份证
        assertTrue(ValidatorUtils.checkIdCard("11010519491231002X"));
        assertTrue(ValidatorUtils.checkIdCard("110105194912310021"));

        // 无效身份证
        assertFalse(ValidatorUtils.checkIdCard("12345678901234"));
        assertFalse(ValidatorUtils.checkIdCard("abcdefghijklmno"));
        assertFalse(ValidatorUtils.checkIdCard("11010519491231002A")); // 最后一位非法
    }

    @Test
    void testValidate() {
        class TestBean {
            @NotNull(message = "name must not be null")
            private String name;

            public TestBean(String name) {
                this.name = name;
            }
        }

        // 验证通过
        TestBean validBean = new TestBean("zhongming");
        assertTrue(ValidatorUtils.validate(validBean));

        // 验证失败
        TestBean invalidBean = new TestBean(null);
        ValidateException exception = assertThrows(ValidateException.class,
                () -> ValidatorUtils.validate(invalidBean));
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("must not be null"));
    }
}

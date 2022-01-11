package com.smart.document.code.qrcode.eye;

import com.smart.document.code.qrcode.eye.render.CodeEyeRenderer;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2021/8/11 16:02
 * @since 1.0
 */
public interface CodeEyeFormatProvider extends Serializable {

    /**
     * 获取渲染类
     * @return 渲染类
     */
    Class<? extends CodeEyeRenderer> getRenderClass();
}

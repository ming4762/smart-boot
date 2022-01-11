package com.smart.document.code.qrcode.eye.render;

import com.smart.document.code.exception.CodeEyeRendererException;
import com.smart.document.code.qrcode.eye.CodeEyeFormatProvider;
import com.smart.document.code.qrcode.eye.CodeEyePosition;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 支持多种类型的码眼渲染类
 * @author Bosco.Liao
 * @author ShiZhongMing
 * 2021/8/11 16:01
 * @since 1.0
 */
public class MultiFormatCodeEyeRenderer implements CodeEyeRenderer {
    @SneakyThrows
    @Override
    public void render(BufferedImage bufferedImage, CodeEyeFormatProvider codeEyeFormatProvider, CodeEyePosition position, Color slave, Color border, Color point) {
        Class<? extends CodeEyeRenderer> renderClass = codeEyeFormatProvider.getRenderClass();
        Assert.notNull(renderClass, "render class can not null");
        if (MultiFormatCodeEyeRenderer.class.equals(renderClass)) {
            throw new CodeEyeRendererException("render class can not be MultiFormatCodeEyeRenderer");
        }
        CodeEyeRenderer codeEyeRenderer = renderClass.getConstructor().newInstance();
        codeEyeRenderer.render(bufferedImage, codeEyeFormatProvider, position, slave, border, point);
    }
}

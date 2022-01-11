package com.smart.document.code.qrcode.eye.render;

import com.smart.document.code.exception.CodeEyeRendererException;
import com.smart.document.code.qrcode.eye.CodeEyeFormatProvider;
import com.smart.document.code.qrcode.eye.CodeEyePosition;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 码眼渲染接口
 * @author Bosco.Liao
 * @author ShiZhongMing
 * 2021/8/11 15:50
 * @since 1.0
 */
public interface CodeEyeRenderer {

    /**
     * 渲染码眼
     * @param bufferedImage 图片对象
     * @param codeEyeFormatProvider 码眼类型
     * @param position 码眼位置
     * @param slave slave颜色
     * @param border border颜色
     * @param point point颜色
     */
    void render(BufferedImage bufferedImage, CodeEyeFormatProvider codeEyeFormatProvider, CodeEyePosition position, Color slave, Color border, Color point);

    /**
     * 验证是否支持格式
     * @param codeEyeFormatProvider codeEyeFormatProvider
     */
    default void checkFormat(CodeEyeFormatProvider codeEyeFormatProvider) {
        if (!this.getClass().equals(codeEyeFormatProvider.getRenderClass())) {
            throw new CodeEyeRendererException("render format disaccord");
        }
    }

    /**
     * 获取点的形状
     * @param x x
     * @param y y
     * @param w w
     * @param h h
     * @param arcw arcw
     * @param arch arch
     * @return 点形状
     */
    default Shape getPointShape(double x, double y, double w, double h, double arcw, double arch) {
        return new Rectangle2D.Double(x, y, w, h);
    }
}

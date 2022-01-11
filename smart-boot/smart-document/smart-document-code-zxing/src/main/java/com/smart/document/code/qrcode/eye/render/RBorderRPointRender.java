package com.smart.document.code.qrcode.eye.render;

import com.smart.document.code.qrcode.eye.CodeEyeFormatProvider;
import com.smart.document.code.qrcode.eye.CodeEyePosition;
import org.springframework.util.ReflectionUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * @author ShiZhongMing
 * 2021/8/11 16:04
 * @since 1.0
 */
public class RBorderRPointRender implements CodeEyeRenderer {
    @Override
    public void render(BufferedImage bufferedImage, CodeEyeFormatProvider codeEyeFormatProvider, CodeEyePosition position, Color slave, Color border, Color point) {
        this.checkFormat(codeEyeFormatProvider);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int borderSize = position.getBorderSize(width);

        final String[] directions = { "topLeft", "topRight", "bottomLeft" };

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setBackground(slave);
        for (String direction : directions) {
            int[] rect = (int[]) ReflectionUtils.invokeMethod(Objects.requireNonNull(ReflectionUtils.findMethod(CodeEyePosition.class, direction + "Rect")), position);
            graphics.clearRect(rect[0], rect[1], rect[2], rect[3]);

            // 渲染border
            Shape shape = new Rectangle2D.Float(rect[0] + borderSize / 2, rect[1] + borderSize / 2,
                    rect[2] - borderSize, rect[3] - borderSize);
            graphics.setColor(slave);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.fill(shape);
            graphics.setStroke(new BasicStroke(borderSize));
            graphics.setColor(border);
            graphics.draw(shape);

            // 渲染point
            rect = (int[]) ReflectionUtils.invokeMethod(Objects.requireNonNull(ReflectionUtils.findMethod(CodeEyePosition.class, direction + "Point")),position.focusPoint(width, height));
            shape = getPointShape(rect[0], rect[1], rect[2], rect[3], 5, 5);
            graphics.setColor(point);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.fill(shape);

            graphics.setStroke(new BasicStroke(0));
            graphics.setColor(point);
            graphics.draw(shape);
        }

        graphics.dispose();
        bufferedImage.flush();
    }

    @Override
    public Shape getPointShape(double x, double y, double w, double h, double arcw, double arch) {
        return new RoundRectangle2D.Double(x, y, w, h, arcw, arch);
    }
}

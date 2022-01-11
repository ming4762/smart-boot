package com.smart.document.code.utils;

import com.smart.document.code.constants.CodeConstants;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author ShiZhongMing
 * 2021/8/11 15:41
 * @since 1.0
 */
public final class BarcodeUtils {

    private BarcodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String HEX_COLOR_START = "#";

    /**
     * 获取颜色
     * @param hexColor 颜色值
     * @return 颜色
     */
    public static Color getColor(final String hexColor) {
        String hexc = hexColor;
        Color result = Color.WHITE;
        if (hexc != null && !"".equals(hexc)) {
            if (hexc.startsWith(HEX_COLOR_START)) {
                hexc = hexc.replaceFirst(HEX_COLOR_START, "");
            }
            result = new Color(Integer.parseInt(hexc, 16));
        }
        return result;
    }

    public static BufferedImage renderMargin(BufferedImage image, int radius, int borderSize, String borderColor, CodeConstants.BorderStyle style, int borderDashGranularity, int margin) {
        int width = image.getWidth();
        int height = image.getHeight();
        int canvasWidth = width + margin * 2;
        int canvasHeight = height + margin * 2;
        BufferedImage canvasImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = canvasImage.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        graphics.setComposite(AlphaComposite.SrcAtop);
        graphics.drawImage(clip(image, radius), margin, margin, null);

        if (borderSize > 0) {
            Stroke stroke;
            if (CodeConstants.BorderStyle.SOLID == style) {
                stroke = new BasicStroke(borderSize);
            } else {
                stroke = new BasicStroke(borderSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f,
                        new float[] { borderDashGranularity, borderDashGranularity }, 0.0f);
            }
            graphics.setColor(getColor(borderColor));
            graphics.setStroke(stroke);
            graphics.drawRoundRect(margin, margin, width - 1, height - 1, radius, radius);
        }

        graphics.dispose();
        image.flush();
        return canvasImage;
    }

    public static BufferedImage clip(final BufferedImage image, final int radius) {

        int width = image.getWidth();
        int height = image.getHeight();

        int max = Math.max(width, height);

        Shape shape;

        // draw circular
        if (max == radius) {
            shape = new Ellipse2D.Float(0, 0, width, height);
        } else {
            shape = new RoundRectangle2D.Float(0, 0, width, height, radius, radius);
        }

        BufferedImage canvasImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = canvasImage.createGraphics();
        graphics.setClip(shape);
        // 抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(image, 0, 0, null);

        graphics.dispose();
        image.flush();

        return canvasImage;
    }

}

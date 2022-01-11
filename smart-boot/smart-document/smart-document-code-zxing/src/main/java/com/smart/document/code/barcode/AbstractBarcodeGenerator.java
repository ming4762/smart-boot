package com.smart.document.code.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.smart.document.code.Generator;
import com.smart.document.code.constants.CodeConstants;
import com.smart.document.code.utils.BarcodeUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 条形码生成工具
 * @author ShiZhongMing
 * 2021/8/12 19:14
 * @since 1.0
 */
public abstract class AbstractBarcodeGenerator implements BarcodeGenerator {

    private final BarcodeConfig config;

    private BufferedImage bufferedImage;

    protected AbstractBarcodeGenerator() {
        this(new BarcodeConfig());
    }

    protected AbstractBarcodeGenerator(BarcodeConfig config) {
        this.config = config;
    }

    @Override
    public BufferedImage getImage() {
        return this.bufferedImage;
    }

    @Override
    public boolean toFile(@NonNull String filePath) throws IOException {
        return ImageIO.write(this.bufferedImage, CodeConstants.IMAGE_TYPE, new File(filePath));
    }

    @Override
    public boolean toStream(@NonNull OutputStream outputStream) throws IOException {
        return ImageIO.write(this.bufferedImage, CodeConstants.IMAGE_TYPE, outputStream);
    }

    @Override
    public Generator generate(@NonNull String content) throws WriterException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, this.config.getFormat(), this.config.getWidth() - this.config.getMargin() * 2, this.config.getHeight() - this.config.getMargin() * 2, this.config.getHints());
        this.bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return this.renderMarginAndWord(content);
    }

    /**
     * 渲染边框、下方文字
     * @param content 内容
     * @return this
     */
    protected BarcodeGenerator renderMarginAndWord(String content) {
        if (!this.config.isShowWord() && this.config.getMargin() == 0) {
            // 不显示文字并且没有边框则不处理
            return this;
        }
        int width = this.config.getWidth();
        int height = this.config.getHeight();
        if (this.config.isShowWord()) {
            height += this.config.getWordHeight();
        }
        BufferedImage wordImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = wordImage.createGraphics();
        // 抗锯齿
        setGraphics2D(g2d);
        // 设置白色
        setColorWhite(g2d);
        // 画条形码到新的面板
        g2d.drawImage(this.bufferedImage, this.config.getMargin(), this.config.getMargin(), this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), null);
        if (this.config.isShowWord()) {
            // 画文字
            Color color = BarcodeUtils.getColor(this.config.getMasterColor());
            g2d.setColor(color);
            // 字体、字型、字号
            g2d.setFont(this.config.getWordFont());
            //文字长度
            if (Boolean.TRUE.equals(this.config.getAutoSpace())) {
                content = this.contentAddSpace(content, g2d.getFontMetrics().stringWidth(content), this.bufferedImage.getWidth(), g2d);
            }
            int strWidth = g2d.getFontMetrics().stringWidth(content);
            //总长度减去文字长度的一半  （居中显示）
            int wordStartX = (width - strWidth) / 2;
            int wordStartY = this.config.getHeight() + this.config.getWordTopMargin();
            // 画文字
            g2d.drawString(content, wordStartX, wordStartY);
        }
        g2d.dispose();
        wordImage.flush();
        this.bufferedImage = wordImage;
        return this;
    }

    protected String contentAddSpace(String oldContent, int contentWidth, int imageWidth, Graphics2D g2d) {
        // 判断 文字长度是否小于条码长度
        if (oldContent.length() <= 1 || imageWidth < contentWidth * 2) {
            return oldContent;
        }
        String newString = StringUtils.join(oldContent.split(""), " ");
        return this.contentAddSpace(newString, g2d.getFontMetrics().stringWidth(newString), imageWidth, g2d);
    }

    /**
     * 设置 Graphics2D 属性  （抗锯齿）
     * @param g2d  Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setGraphics2D(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        g2d.setStroke(s);
    }

    /**
     * 设置背景为白色
     * @param g2d Graphics2D提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制
     */
    private static void setColorWhite(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        //填充整个屏幕
        g2d.fillRect(0,0,600,600);
        //设置笔刷
        g2d.setColor(Color.BLACK);
    }

    @Override
    public void close() {
    }

    @Override
    public BarcodeGenerator format(BarcodeFormat format) {
        this.config.setFormat(format);
        return this;
    }

    @Override
    public BarcodeGenerator generate(@NonNull String content, BarcodeFormat format) throws WriterException {
        this.format(format);
        return (BarcodeGenerator)this.generate(content);
    }
}

package com.smart.document.code.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.Version;
import com.smart.document.code.Generator;
import com.smart.document.code.constants.CodeConstants;
import com.smart.document.code.qrcode.eye.CodeEyeFormatProvider;
import com.smart.document.code.qrcode.eye.CodeEyePosition;
import com.smart.document.code.qrcode.eye.CodeEyeRenderStrategy;
import com.smart.document.code.qrcode.eye.render.MultiFormatCodeEyeRenderer;
import com.smart.document.code.qrcode.writer.QrcodeWriter;
import com.smart.document.code.utils.BarcodeUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author ShiZhongMing
 * 2021/8/11 13:42
 * @since 1.0
 */
public abstract class AbstractQrcodeGenerator implements QrcodeGenerator {

    /**
     * LOGO 输入流
     */
    private InputStream logoInputStream;

    private BufferedImage image;

    /**
     * 二维码配置信息
     */
    private final QrcodeConfig config;

    private String content;

    protected AbstractQrcodeGenerator() {
        this(new QrcodeConfig());
    }

    protected AbstractQrcodeGenerator(@NonNull QrcodeConfig config) {
        this.config = config;
    }

    @Override
    public boolean toFile(@NonNull String filePath) throws IOException {
        this.validateImage();
        return ImageIO.write(this.image, CodeConstants.IMAGE_TYPE, new File(filePath));
    }

    @Override
    public boolean toStream(@NonNull OutputStream outputStream) throws IOException {
        this.validateImage();
        return ImageIO.write(this.image, CodeConstants.IMAGE_TYPE, outputStream);
    }

    @Override
    public Generator generate(@NonNull String content) {
        this.content = content;
        this.image = generateQrcode(this);
        return this;
    }

    @Override
    public @NonNull QrcodeConfig getConfig() {
        return this.config;
    }

    @Override
    public QrcodeGenerator generate(@NonNull String content, String logo) throws IOException {
        this.setLogo(logo);
        return (QrcodeGenerator)this.generate(content);
    }

    @Override
    public QrcodeGenerator setLogo(@NonNull String path, boolean remote) throws IOException {
        this.readLogo(path, remote);
        return this;
    }

    @Override
    public QrcodeGenerator setLogo(@NonNull InputStream inputStream) {
        this.logoInputStream  = inputStream;
        return this;
    }

    @Override
    public BufferedImage getImage(boolean clear) {
        try {
            return this.image;
        } finally {
            this.close();
        }
    }

    @Override
    public void close() {
        this.image = null;
    }

    protected void validateImage() {
        Assert.notNull(this.image, "image is null");
    }

    protected void readLogo(String path, boolean remote) throws FileNotFoundException {
        if (!remote) {
            this.setLogo(new FileInputStream(path));
        }
        // todo: 远程logo支持
    }

    /**
     * 生成二维码
     * @param qrcodeGenerator 二维码创建对象
     * @return 图片信息
     */
    @SneakyThrows
    protected static BufferedImage generateQrcode(AbstractQrcodeGenerator qrcodeGenerator) {
        QrcodeConfig config = qrcodeGenerator.getConfig();
        QrcodeWriter.QrcodeBitMatrix qrcodeBitMatrix = new QrcodeWriter().encodeX(qrcodeGenerator.content, BarcodeFormat.QR_CODE, config.getWidth(), config.getHeight(), null);

        // 创建图像
        BufferedImage image = toBufferedImage(qrcodeBitMatrix, config);
        // 渲染 margin
        image = BarcodeUtils.renderMargin(image, config.getBorderRadius(), config.getBorderSize(), config.getBorderColor(), config.getBorderStyle(), config.getBorderDashGranularity(), config.getMargin());
        // todo:渲染logo
        return image;
    }

    protected static BufferedImage toBufferedImage(QrcodeWriter.QrcodeBitMatrix matrix, QrcodeConfig config) {
        BitMatrix bitMatrix = matrix.getBitMatrix();
        Version version = matrix.getQrcode().getVersion();
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        int modules = (version.getVersionNumber() - 1) * 4 + 21;
        int[] topLeftOnBit = bitMatrix.getTopLeftOnBit();

        // 二维码码眼位置
        CodeEyePosition position = new CodeEyePosition(modules, topLeftOnBit);

        int moduleHeight = position.getModuleHeight(height);
        int moduleWidth = position.getModuleWidth(width);

        // 获取码眼位置
        int leftStartX = topLeftOnBit[0] + moduleWidth * CodeEyeRenderStrategy.POINT_BORDER.getStart();
        int leftEndX = topLeftOnBit[0] + moduleWidth * CodeEyeRenderStrategy.POINT_BORDER.getEnd();
        int topStartY = topLeftOnBit[1] + moduleHeight * CodeEyeRenderStrategy.POINT_BORDER.getStart();
        int topEndY = topLeftOnBit[1] + moduleHeight * CodeEyeRenderStrategy.POINT_BORDER.getEnd();
        int rightStartX = topLeftOnBit[0] + moduleWidth * (modules - CodeEyeRenderStrategy.POINT_BORDER.getEnd());
        int rightEndX = width - topLeftOnBit[0] - moduleWidth * CodeEyeRenderStrategy.POINT_BORDER.getStart();
        int bottomStartY = height - topLeftOnBit[1] - moduleHeight * CodeEyeRenderStrategy.POINT_BORDER.getEnd();
        int bottomEndY = height - topLeftOnBit[1] - moduleHeight * CodeEyeRenderStrategy.POINT_BORDER.getStart();

        position.setPosition(leftStartX, leftEndX, topStartY, topEndY, rightStartX, rightEndX, bottomStartY,
                bottomEndY);
        // 创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取配置的颜色
        int masterColor = BarcodeUtils.getColor(config.getMasterColor()).getRGB();
        int slaveColor = BarcodeUtils.getColor(config.getSlaveColor()).getRGB();

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {
                // top left
                if (x >= leftStartX && x < leftEndX && y >= topStartY && y < topEndY) {
                    // do nothing
                }
                // top right
                else if (x >= rightStartX && x < rightEndX && y >= topStartY && y < topEndY) {
                    // do nothing
                }
                // bottom left
                else if (x >= leftStartX && x < leftEndX && y >= bottomStartY && y < bottomEndY) {
                    // do nothing
                }
                // non codeEyes region
                else {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? masterColor : slaveColor);
                }
            }
        }
        Color border = BarcodeUtils.getColor(config.getCodeEyesBorderColor());
        Color point = BarcodeUtils.getColor(config.getCodeEyesPointColor());
        CodeEyeFormatProvider format = config.getCodeEyeFormat();

        new MultiFormatCodeEyeRenderer().render(image, format, position, new Color(slaveColor), border, point);

        return image;
    }
}

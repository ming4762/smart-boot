package com.smart.document.code.qrcode;

import com.smart.document.code.Generator;
import lombok.NonNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * 二维码生成工具
 * @author ShiZhongMing
 * 2021/8/11 12:58
 * @since 1.0
 */
public interface QrcodeGenerator extends Generator {

    /**
     * 获取二维码配置信息
     * @return 二维码配置
     */
    @NonNull
    QrcodeConfig getConfig();

    /**
     * 生成二维码
     * @param content 二维码内容
     * @param logo 二维码logo
     * @return 二维码对象
     * @throws IOException IOException
     */
    QrcodeGenerator generate(@NonNull String content, String logo) throws IOException;

    /**
     * 设置logo
     * @param path logo路径
     * @param remote 是否远程logo
     * @return 二维码对象
     * @throws IOException IOException
     */
    QrcodeGenerator setLogo(@NonNull String path, boolean remote) throws IOException;

    /**
     * 设置logo
     * @param path logo path
     * @return this
     * @throws IOException IOException IOException
     */
    default QrcodeGenerator setLogo(@NonNull String path) throws IOException {
        return this.setLogo(path, false);
    }

    /**
     * 设置LOGO输入流
     * @param inputStream LOGO输入流
     * @return 二维码对象
     */
    QrcodeGenerator setLogo(@NonNull InputStream inputStream);

    /**
     * 获取图片
     * @param clear 是否清楚
     * @return 图片对象
     */
    BufferedImage getImage(boolean clear);

    /**
     * 获取图片对象
     * @return 图片
     */
    @Override
    default BufferedImage getImage() {
        return this.getImage(true);
    }
}

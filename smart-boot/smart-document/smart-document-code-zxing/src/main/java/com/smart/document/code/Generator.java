package com.smart.document.code;

import com.google.zxing.WriterException;
import lombok.NonNull;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 条码生成接口
 * @author Bosco.Liao
 * @author ShiZhongMing
 * 2021/8/11 12:51
 * @since 1.0
 */
public interface Generator extends Closeable {

    /**
     * 获取图片
     * @return 图片对象
     */
    BufferedImage getImage();

    /**
     * 写道文件
     * @param filePath 文件路径
     * @return 是否写入成功
     * @throws IOException IOException
     */
    boolean toFile(@NonNull String filePath) throws IOException;

    /**
     * 写入到输出流
     * @param outputStream 输出流
     * @return 是否写入成功
     * @throws IOException IOException
     */
    boolean toStream(@NonNull OutputStream outputStream) throws IOException;

    /**
     * 创建条码
     * @param content 内容
     * @return 条码对象
     * @throws WriterException WriterException
     */
    Generator generate(@NonNull String content) throws WriterException;
}

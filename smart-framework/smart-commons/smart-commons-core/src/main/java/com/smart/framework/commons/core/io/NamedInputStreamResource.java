package com.smart.framework.commons.core.io;

import org.springframework.core.io.InputStreamResource;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 命名的输入流资源
 * 用于在上传文件时，将文件名与输入流关联起来
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/3 16:09
 * @since 5.0.0
 */
public class NamedInputStreamResource extends InputStreamResource {

    private final String filename;

    public NamedInputStreamResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    public NamedInputStreamResource(MultipartFile multipartFile) throws IOException {
        this(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
    }


    /**
     * This implementation always returns {@code null},
     * assuming that this resource type does not have a filename.
     */
    @Override
    @NonNull
    public String getFilename() {
        return this.filename;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && this.filename.equals(((NamedInputStreamResource) other).filename);
    }


    @Override
    public int hashCode() {
        return super.hashCode() * 31 + this.filename.hashCode();
    }
}

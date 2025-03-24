package com.smart.module.api.file.dto;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhongming4762
 * 2023/3/21
 */
public class InputStreamMultipartFile implements MultipartFile {

    private final InputStream inputStream;

    private final String name;

    public InputStreamMultipartFile(InputStream inputStream, String name) {
        this.inputStream = inputStream;
        this.name = name;
    }

    @NonNull
    @Override
    public String getName() {
        return this.name;
    }

    @Nullable
    @Override
    public String getOriginalFilename() {
        return this.name;
    }

    @Nullable
    @Override
    public String getContentType() {
        return "multipart/form-data";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @NonNull
    @SneakyThrows({IOException.class})
    @Override
    public byte[] getBytes() {
        return IOUtils.toByteArray(this.inputStream);
    }

    @NonNull
    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        this.transferTo(dest.toPath());
    }
}

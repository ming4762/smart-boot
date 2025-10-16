package com.smart.framework.commons.core.upload;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhongming4762
 * 2023/8/10 19:37
 */
public class UploadInputStreamResource extends InputStreamResource {

    private final String filename;

    private final long contentLength;

    /**
     * Create a new InputStreamResource.
     *
     * @param inputStream the InputStream to use
     */
    public UploadInputStreamResource(InputStream inputStream, String filename) throws IOException {
        super(inputStream);
        this.filename = filename;
        this.contentLength = inputStream.available();
    }

    public UploadInputStreamResource(MultipartFile multipartFile) throws IOException {
        this(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
    }

    /**
     * This method reads the entire InputStream to determine the content length.
     * <p>For a custom subclass of {@code InputStreamResource}, we strongly
     * recommend overriding this method with a more optimal implementation, e.g.
     * checking File length, or possibly simply returning -1 if the stream can
     * only be read once.
     *
     * @see #getInputStream()
     */
    @Override
    public long contentLength() throws IOException {
        return contentLength;
    }

    /**
     * This implementation always returns {@code null},
     * assuming that this resource com.smart.framework.tool.code.type does not have a filename.
     */
    @Override
    public String getFilename() {
        return this.filename;
    }


    @Override
    public boolean equals(Object other) {
        return super.equals(other) && this.filename.equals(((UploadInputStreamResource) other).filename);
    }


    @Override
    public int hashCode() {
        return super.hashCode() * 31 + this.filename.hashCode();
    }
}

package com.smart.commons.core.upload;

import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;

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
    @SneakyThrows(IOException.class)
    public UploadInputStreamResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
        this.contentLength = inputStream.available();
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
     * assuming that this resource type does not have a filename.
     */
    @Override
    public String getFilename() {
        return this.filename;
    }
}

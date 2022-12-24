package com.smart.license.core;

import de.schlichtherle.license.AbstractKeyStoreParam;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author zhongming4762
 * 2022/12/18 16:07
 */
@Getter
public class CustomKeyStoreParam extends AbstractKeyStoreParam {

    private static final String CLASSPATH_PREFIX = "classpath:";

    private final String storePath;
    private final String alias;
    private final String storePwd;
    private final String keyPwd;

    private final Class<?> clazz;

    public CustomKeyStoreParam(Class<?> clazz, String resource, String alias, String storePwd, String keyPwd) {
        super(clazz, resource);
        this.clazz = clazz;
        this.storePath = resource;
        this.alias = alias;
        this.storePwd = storePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getStorePwd() {
        return this.storePwd;
    }

    @Override
    public String getKeyPwd() {
        return this.keyPwd;
    }

    /**
     * Looks up the resource provided to the constructor using the classloader
     * provided to the constructor and returns it as an {@link InputStream}.
     */
    @Override
    public InputStream getStream() throws IOException {
        InputStream inputStream;
        if (this.storePath.startsWith(CLASSPATH_PREFIX)) {
            String path = this.storePath.replace(CLASSPATH_PREFIX, "");
            inputStream = this.clazz.getClassLoader().getResourceAsStream(path);
        } else {
            inputStream = Files.newInputStream(Paths.get(this.storePath));
        }
        if (inputStream == null) {
            throw new FileNotFoundException(this.storePath);
        }
        return inputStream;
    }
}

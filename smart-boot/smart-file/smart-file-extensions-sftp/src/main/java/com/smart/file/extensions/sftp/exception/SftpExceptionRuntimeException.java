package com.smart.file.extensions.sftp.exception;

import com.jcraft.jsch.SftpException;

/**
 * @author shizhongming
 * 2020/12/13 12:10 下午
 */
public class SftpExceptionRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -8968802972165721512L;

    public SftpExceptionRuntimeException(SftpException e) {
        super(e.getMessage(), e);
    }
}

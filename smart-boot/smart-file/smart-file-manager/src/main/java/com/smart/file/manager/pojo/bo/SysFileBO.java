package com.smart.file.manager.pojo.bo;

import com.smart.commons.core.utils.SmartIdGenerator;
import com.smart.file.manager.model.SmartFilePO;
import com.smart.module.api.file.dto.FileSaveParameter;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author jackson
 * 2020/1/27 7:52 下午
 */
@Getter
@Setter
@ToString
public class SysFileBO {

    private static final String DEFAULT_FILE_TYPE = "NORMAL";

    private SmartFilePO file;

    private InputStream inputStream;

    private FileSaveParameter parameter;

    @SneakyThrows(IOException.class)
    public SysFileBO(@NonNull InputStream inputStream, FileSaveParameter parameter, String contentType) {
        // 设置过期时间
        this.file = SmartFilePO.builder()
                .fileId(SmartIdGenerator.nextId())
                .filename(parameter.getFilename())
                .type(parameter.getType() == null ? DEFAULT_FILE_TYPE : parameter.getType())
                .contentType(contentType)
                .fileSize(Integer.valueOf(inputStream.available()).longValue())
                .expireTime(getExpireTime(parameter))
                .build();
        this.parameter = parameter;
        this.inputStream = inputStream;
    }

    /**
     * 获取文件过期时间
     * @param parameter 参数
     * @return 过期时间
     */
    private static LocalDateTime getExpireTime(FileSaveParameter parameter) {
        if (parameter.getExpireIn() != null) {
            return LocalDateTime.now().plus(parameter.getExpireIn());
        }
        return parameter.getExpireAt();
    }
}

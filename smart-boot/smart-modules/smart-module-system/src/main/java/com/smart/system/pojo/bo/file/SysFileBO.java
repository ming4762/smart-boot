package com.smart.system.pojo.bo.file;

import com.smart.commons.core.utils.IdGenerator;
import com.smart.file.core.parameter.FileSaveParameter;
import com.smart.system.model.file.SmartFilePO;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.io.InputStream;

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

    @SneakyThrows
    public SysFileBO(@NonNull InputStream inputStream, FileSaveParameter parameter, String contentType) {
        this.file = SmartFilePO.builder()
                .fileId(IdGenerator.nextId())
                .fileName(parameter.getFilename())
                .type(parameter.getType() == null ? DEFAULT_FILE_TYPE : parameter.getType())
                .contentType(contentType)
                .fileSize(Integer.valueOf(inputStream.available()).longValue())
                .build();
        this.parameter = parameter;
        this.inputStream = inputStream;
    }

}

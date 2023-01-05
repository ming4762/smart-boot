package com.smart.file.manager.pojo.bo;

import com.smart.commons.core.utils.IdGenerator;
import com.smart.file.manager.constants.FileTypeEnum;
import com.smart.file.manager.model.SysFilePO;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author jackson
 * 2020/1/27 7:52 下午
 */
@Getter
@Setter
@ToString
public class SysFileBO {
    private SysFilePO file;

    private InputStream inputStream;


    private String folder;

    @SneakyThrows
    public SysFileBO(@NonNull MultipartFile multipartFile, String filename, String folder, FileTypeEnum type, String handlerType) {
        this.file = SysFilePO.builder()
                .fileId(IdGenerator.nextId())
                .fileName(StringUtils.isEmpty(filename) ? multipartFile.getOriginalFilename() : filename)
                .type(type == null ? FileTypeEnum.TEMP : type)
                .contentType(multipartFile.getContentType())
                .handlerType(handlerType)
                .fileSize(multipartFile.getSize())
                .build();
        this.folder = StringUtils.isBlank(folder) ? "" : folder;
        this.inputStream = multipartFile.getInputStream();
    }


    public SysFileBO(SysFilePO file, InputStream inputStream) {
        this.file = file;
        this.inputStream = inputStream;
    }
}

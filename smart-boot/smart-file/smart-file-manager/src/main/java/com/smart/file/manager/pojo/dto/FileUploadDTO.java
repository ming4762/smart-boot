package com.smart.file.manager.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/2/18
 */
@Getter
@Setter
@ToString
public class FileUploadDTO implements Serializable {

    private transient MultipartFile file;

    private String fileName;

    private String type;

    private Integer seq;

    private String fileStorageCode;

    private Long fileStorageId;
}

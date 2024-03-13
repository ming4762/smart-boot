package com.smart.module.api.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemoteFileSaveParameter extends FileSaveParameter {

    @Serial
    private static final long serialVersionUID = -6836820080390963264L;
    private transient MultipartFile multipartFile;
}

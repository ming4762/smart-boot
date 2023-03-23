package com.smart.module.api.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

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

    private transient MultipartFile multipartFile;
}

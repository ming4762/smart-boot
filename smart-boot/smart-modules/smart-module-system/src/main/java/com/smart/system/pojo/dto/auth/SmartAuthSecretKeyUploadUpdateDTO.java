package com.smart.system.pojo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 秘钥上传参数
 * @author zhongming4762
 * 2023/2/19
 */
@Getter
@Setter
@ToString
@Schema(title = "秘钥上传参数")
public class SmartAuthSecretKeyUploadUpdateDTO implements Serializable {

    private Long id;

    @NotNull(message = "秘钥名称不能为空")
    private String keyName;

    @NotNull(message = "请选择文件存储器")
    private Long fileStorageId;

    private Long systemId;

    @Schema(title = "序号", required = true)
    @NotNull(message = "序号不能为空")
    private Integer seq;

    @Schema(title = "密钥库密码", required = true)
    @NotNull(message = "密钥库密码不能为空")
    private String storePassword;

    @Schema(title = "私钥密码", required = true)
    @NotNull(message = "私钥密码不能为空")
    private String keyPassword;

    @Schema(title = "私钥别称", required = true)
    @NotNull(message = "私钥别称不能为空")
    private String alias;

    @NotNull(message = "请选择公钥文件")
    private transient MultipartFile publicKeyFile;

    @NotNull(message = "请选择私钥文件")
    private transient MultipartFile privateKeyFile;

    private String remark;
}

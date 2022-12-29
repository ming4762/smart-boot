package com.smart.system.pojo.dto.license;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * smart_auth_license - 许可证管理
 * @author SmartCodeGenerator
 * todo：国际化
* 2022-12-17 14:31:50
*/
@Getter
@Setter
@ToString
public class SmartAuthLicenseSaveUpdateDTO implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * license编码
    */
    @NotNull(message = "license编码不能为空")
    private String licenseCode;

    @NotNull(message = "license名称不能为空")
    private String licenseName;

    /**
    * mac地址，多个mac地址以逗号分隔
    */
    private String macAddress;
    /**
    * 主板序列号，多个IP以逗号分隔
    */
    private String ipAddress;
    /**
    * cpu序列号
    */
    private String cpuSerial;
    /**
    * 主板序列号
    */
    private String mainBoardSerial;
    /**
    * 生效时间
    */
    @NotNull(message = "生效时间不能为空")
    private LocalDateTime effectiveTime;
    /**
    * 过期时间
    */
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime expirationTime;

    private String storePath;

    private String licensePath;

    private String storePassword;

    private String keyPassword;

    private String alias;

    private String subject;

    /**
     * 企业
     */
    private String enterprise;

    /**
     * 项目&系统
     */
    private String project;

    /**
     * 系统版本号
     */
    private String version;

    /**
     * 合同编号
     */
    private String contractNo;

}

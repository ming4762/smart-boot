package com.smart.system.model.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.system.constants.LicenseStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
* smart_auth_license - 许可证管理
* @author SmartCodeGenerator
* 2022-12-17 14:31:49
*/
@Getter
@Setter
@TableName("smart_auth_license")
public class SmartAuthLicensePO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * license_code - license编码
    */
    private String licenseCode;

    private String licenseName;

    /**
     * 使用秘钥ID
     */
    private Long secretKeyId;

    /**
    * project_id - 所属项目
    */
    private Long systemId;

    /**
    * mac_address - mac地址
    */
    private String macAddress;

    /**
    * ip_address - 主板序列号
    */
    private String ipAddress;

    /**
    * cpu_serial - cpu序列号
    */
    private String cpuSerial;

    /**
    * main_board_serial - 主板序列号
    */
    private String mainBoardSerial;

    /**
    * effective_date - 生效时间
    */
    private LocalDateTime effectiveTime;

    /**
    * expiration_time - 过期时间
    */
    private LocalDateTime expirationTime;

    /**
     * 可登录用户数 -1不限制
     */
    private Long userNum;

    /**
     * 文件存储器ID，license存储位置
     */
    private Long fileStorageId;

    /**
     * license文件ID
     */
    private Long licenseFileId;

    private String subject;

    /**
    * status - 状态
    */
    private LicenseStatusEnum status;

    private String remark;

    /**
     * 企业
     */
    private String enterprise;


    /**
     * 系统版本号
     */
    private String version;

    /**
     * 合同编号
     */
    private String contractNo;

    private Integer seq;

    private Boolean useYn;

    private Boolean deleteYn;
}

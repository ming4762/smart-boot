package com.smart.system.model;

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
    * project_id - 所属项目
    */
    private Long projectId;

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

    private String storePath;

    private String licensePath;

    private String storePassword;

    private String keyPassword;

    private String alias;

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

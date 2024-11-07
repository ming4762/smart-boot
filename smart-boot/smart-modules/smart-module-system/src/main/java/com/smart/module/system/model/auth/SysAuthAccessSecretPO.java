package com.smart.module.system.model.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* sys_auth_access_secret - 
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:25
*/
@Getter
@Setter
@TableName("sys_auth_access_secret")
public class SysAuthAccessSecretPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = -8036894937398185505L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * seq - 序号
    */
    private Integer seq;

    /**
    * access_key - Access key
    */
    private String accessKey;

    /**
    * secret_key - Secret key
    */
    private String secretKey;

    /**
    * expire_date - 过期时间
    */
    private LocalDateTime expireDate;

    /**
    * access_ip - 授权IP或域名
    */
    private String accessIp;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

    /**
     * 租户ID
     */
    private Long tenantId;

}
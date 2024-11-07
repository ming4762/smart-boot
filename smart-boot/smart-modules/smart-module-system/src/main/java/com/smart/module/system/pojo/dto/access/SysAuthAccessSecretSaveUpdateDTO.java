package com.smart.module.system.pojo.dto.access;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* sys_auth_access_secret - 
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:26
*/
@Getter
@Setter
@ToString
public class SysAuthAccessSecretSaveUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8202607119457769084L;
    /**
    * 
    */
    private Long id;

    /**
    * 过期时间
    */
    private LocalDateTime expireDate;
    /**
    * 授权IP或域名
    */
    private String accessIp;
    /**
    * 备注
    */
    private String remark;
    /**
    * 
    */
    private Boolean useYn;
    /**
    * 序号
    */
    private Integer seq;

    private Long tenantId;

}
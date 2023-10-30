package com.smart.system.model.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
* sys_auth_access_secret - 
* @author SmartCodeGenerator
* 2023年10月26日 下午7:25:25
*/
@Getter
@Setter
@TableName("sys_auth_access_secret")
public class SysAuthAccessSecretPO extends BaseModel {

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
    * create_user_id - 创建人员ID
    */
    private Long createUserId;

    /**
    * create_by - createBy
    */
    private String createBy;

    /**
    * create_time - 创建时间
    */
    private LocalDateTime createTime;

    /**
    * update_user_id - 更新人员ID
    */
    private Long updateUserId;

    /**
    * update_by - updateBy
    */
    private String updateBy;

    /**
    * update_time - 更新时间
    */
    private LocalDateTime updateTime;

}
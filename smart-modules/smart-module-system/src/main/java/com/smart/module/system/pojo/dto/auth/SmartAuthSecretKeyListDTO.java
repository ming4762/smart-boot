package com.smart.module.system.pojo.dto.auth;

import com.smart.framework.crud.query.PageSortQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * 秘钥查询dto
 * @author zhongming4762
 * 2023/2/19
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(title = "秘钥查询参数")
public class SmartAuthSecretKeyListDTO extends PageSortQuery {

    @Serial
    private static final long serialVersionUID = 3589614800181253734L;

    @Schema(title = "所属系统ID")
    private Long systemId;
}

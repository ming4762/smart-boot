package com.smart.system.pojo.dto.auth;

import com.smart.crud.query.PageSortQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @Schema(title = "所属系统ID")
    private Long systemId;
}

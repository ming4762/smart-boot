package com.smart.system.pojo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author shizhongming
 * 2020/6/2 1:37 下午
 */
@Getter
@Setter
@ToString
public class UserSaveDTO extends UserSaveUpdateDTO {
    @Serial
    private static final long serialVersionUID = 4845445091894712530L;
}

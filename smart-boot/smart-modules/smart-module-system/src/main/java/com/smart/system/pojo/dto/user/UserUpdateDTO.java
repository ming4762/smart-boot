package com.smart.system.pojo.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shizhongming
 * 2020/6/2 4:50 下午
 */
@Getter
@Setter
@ToString
public class UserUpdateDTO extends UserSaveUpdateDTO {
    private static final long serialVersionUID = -8473696763380911595L;

    private Long userId;
}

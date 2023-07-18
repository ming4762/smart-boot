package com.smart.message.manager.pojo.vo;

import com.smart.message.manager.model.SmartMessageSystemPO;
import com.smart.module.api.system.dto.SysUserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/7/17 17:37
 */
@Getter
@Setter
@ToString
public class SmartMessageSystemDetailVO extends SmartMessageSystemPO {

    /**
     * 发送人信息
     */
    private SysUserDTO sender;
}

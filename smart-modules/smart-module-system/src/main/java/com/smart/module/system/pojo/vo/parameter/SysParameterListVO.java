package com.smart.module.system.pojo.vo.parameter;

import com.smart.module.system.model.SysParameterPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author shizhongming
 * 2025/9/6 10:03
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SysParameterListVO extends SysParameterPO {

    private String commonParameter;
}

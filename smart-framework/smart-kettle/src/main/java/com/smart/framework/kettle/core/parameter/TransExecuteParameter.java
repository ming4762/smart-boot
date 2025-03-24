package com.smart.framework.kettle.core.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * trans执行采纳数
 * @author shizhongming
 * 2024/3/13 9:41
 * @since 3.0.0
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TransExecuteParameter extends BasicExecuteParameter {
    @Serial
    private static final long serialVersionUID = 6365958377404254315L;

    /**
     * 位置参数，最多10个
     */
    private List<String> params;
}

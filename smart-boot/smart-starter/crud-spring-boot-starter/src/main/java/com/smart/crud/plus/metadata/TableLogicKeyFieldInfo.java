package com.smart.crud.plus.metadata;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.smart.crud.plus.logic.TableLogicKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author shizhongming
 * 2023/10/31 14:00
 * @since 3.0.0
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TableLogicKeyFieldInfo {

    private TableFieldInfo fieldInfo;

    private TableLogicKey tableLogicKey;
}

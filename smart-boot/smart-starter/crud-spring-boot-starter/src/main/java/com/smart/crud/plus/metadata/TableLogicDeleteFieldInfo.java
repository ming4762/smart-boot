package com.smart.crud.plus.metadata;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.smart.crud.plus.logic.TableLogicKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shizhongming
 * 2023/10/31 14:00
 * @since 3.0.0
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class TableLogicDeleteFieldInfo {

    private TableFieldInfo deleteKeyFieldInfo;

    private TableLogicKey tableLogicKey;

    private TableFieldInfo deleteByFieldInfo;

    private TableFieldInfo deleteTimeFieldInfo;

    private TableFieldInfo deleteUserIdFieldInfo;
}

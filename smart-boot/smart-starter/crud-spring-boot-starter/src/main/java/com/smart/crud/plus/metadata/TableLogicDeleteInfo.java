package com.smart.crud.plus.metadata;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.smart.crud.plus.logic.LogicKeyStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2023/10/31 14:00
 * @since 3.0.0
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class TableLogicDeleteInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 7460831304058200430L;

    /**
     * 逻辑删除key
     */
    private TableFieldInfo deleteKeyFieldInfo;

    /**
     * 逻辑删除key填充策略
     */
    private LogicKeyStrategy logicKeyStrategy;

    /**
     * 逻辑删除需要填充的字典
     */
    private List<TableFieldInfo> fillFieldInfoList;
}

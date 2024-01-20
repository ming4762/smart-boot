package com.smart.message.manager.pojo.paramteter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2023/1/20 15:39
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SmartMessageMarkAsReadParameter implements Serializable {

    /**
     * 消息ID列表
     */
    private List<Long> messageIdList;

    /**
     * 是否标记所有
     */
    private Boolean markAll;
}

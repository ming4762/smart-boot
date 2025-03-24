package com.smart.framework.crud.model;

import com.smart.framework.crud.annotation.TableLogicField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2024/4/21 17:36
 * @since 3.0.0
 */
@Getter
@Setter
public class BaseModelDeleteUserTime extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = 7721655965021218117L;
    /**
     * delete_key - deleteKey
     */
    @TableLogicField(isDeleteKey = true)
    private Long deleteKey;

    /**
     * delete_time - deleteTime
     */
    @TableLogicField(isFill = true)
    private ZonedDateTime deleteTime;

    /**
     * delete_user_id - deleteUserId
     */
    @TableLogicField(isFill = true)
    private Long deleteUserId;

    /**
     * delete_by - deleteBy
     */
    @TableLogicField(isFill = true)
    private String deleteBy;

}

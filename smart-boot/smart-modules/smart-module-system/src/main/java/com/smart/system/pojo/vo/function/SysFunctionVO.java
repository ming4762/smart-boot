package com.smart.system.pojo.vo.function;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.system.model.SysFunctionPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shizhongming
 * 2022/11/12 19:28
 */
@ToString
@Getter
@Setter
public class SysFunctionVO implements CreateUpdateUserSetter, Serializable {

    private SysFunctionPO function;

    private BaseUser createUser;

    private BaseUser updateUser;

    private SysFunctionPO parent;

    @Override
    public Long getCreateUserId() {
        return this.function.getCreateUserId();
    }

    @Override
    public Long getUpdateUserId() {
        return this.function.getUpdateUserId();
    }
}

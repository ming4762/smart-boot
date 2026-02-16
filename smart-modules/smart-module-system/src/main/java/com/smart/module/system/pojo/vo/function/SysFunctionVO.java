package com.smart.module.system.pojo.vo.function;

import com.smart.framework.crud.model.BaseUser;
import com.smart.framework.crud.model.CreateUpdateUserSetter;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.model.micorapp.SysFunctionMicroFrontendPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2022/11/12 19:28
 */
@ToString
@Getter
@Setter
public class SysFunctionVO implements CreateUpdateUserSetter, Serializable {

    @Serial
    private static final long serialVersionUID = 6189136778631359053L;

    private SysFunctionPO function;

    private BaseUser createUser;

    private BaseUser updateUser;

    private SysFunctionPO parent;

    /**
     * 微前端微应用
     */
    private SysFunctionMicroFrontendPO microFrontend;

    @Override
    public Long getCreateUserId() {
        return this.function.getCreateUserId();
    }

    @Override
    public Long getUpdateUserId() {
        return this.function.getUpdateUserId();
    }
}

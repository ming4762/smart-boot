package com.smart.system.pojo.vo;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.system.model.SysFunctionPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.Map;

/**
 * @author ShiZhongMing
 * 2021/7/2 16:38
 * @since 1.0
 */
@Setter
@Getter
@ToString
public class SysFunctionListVO extends SysFunctionPO implements CreateUpdateUserSetter {

    @Serial
    private static final long serialVersionUID = 1260522096785428563L;
    private BaseUser createUser;

    private BaseUser updateUser;

    /**
     * 国际化信息
     * @since 1.0.7
     * 2021-11-12
     */
    private Map<String, String> locales;

    private SysFunctionPO parent;
}

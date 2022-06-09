package com.smart.db.generator.pojo.vo.template;

import com.smart.crud.model.BaseUser;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.db.generator.model.DbCodeTemplatePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/6/28 10:33
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCodeTemplateListVO extends DbCodeTemplatePO implements CreateUpdateUserSetter {
    @Serial
    private static final long serialVersionUID = -3923663463869455255L;

    private BaseUser createUser;

    private BaseUser updateUser;

}

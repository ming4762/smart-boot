package com.smart.system.pojo.dto.system;

import com.smart.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/2/8 21:14
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class SmartAuthLicenseListBySystemDTO extends PageSortQuery {

    private Long systemId;
}

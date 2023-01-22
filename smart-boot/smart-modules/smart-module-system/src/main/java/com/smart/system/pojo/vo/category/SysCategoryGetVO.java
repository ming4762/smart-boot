package com.smart.system.pojo.vo.category;

import com.smart.system.model.SysCategoryPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/1/22 18:38
 */
@Getter
@Setter
@ToString
public class SysCategoryGetVO extends SysCategoryPO {

    private SysCategoryPO parent;
}

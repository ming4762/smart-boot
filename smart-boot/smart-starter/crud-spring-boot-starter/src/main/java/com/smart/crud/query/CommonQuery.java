package com.smart.crud.query;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用查询
 * @author shizhongming
 * 2021/4/24 6:45 下午
 * @since 2.0
 */
@Getter
@Setter
public class CommonQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 25607615568253403L;

    /**
     * 查询参数
     */
    private Map<String, Serializable> parameter = new HashMap<>(0);

    /**
     * 外层 or 内层 and查询参数，例如：or (name = '李白' and status <> '活着')
     */
    private List<Map<String, Serializable>> orAndParameter = new ArrayList<>(0);

    /**
     * 外层and 内层 or查询参数，例如：and (name = '李白' and status <> '活着')
     */
    private List<Map<String, Serializable>> andOrParameter = new ArrayList<>(0);

    /**
     * 查询的属性列表
     */
    private List<String> propertyList = new ArrayList<>(0);
}

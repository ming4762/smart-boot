package com.smart.commons.core.data;

import com.smart.commons.core.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * tree 实体类
 * @author shizhongming
 * 2020/1/8 8:56 下午
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tree<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = -4056107665873673992L;

    private Serializable id = null;

    private String text = null;

    private T data = null;

    private List<Tree<T>> children = new ArrayList<>(0);

    private Serializable parentId = null;

    private Boolean hasParent = Boolean.FALSE;

    private Boolean hasChildren = Boolean.FALSE;

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}

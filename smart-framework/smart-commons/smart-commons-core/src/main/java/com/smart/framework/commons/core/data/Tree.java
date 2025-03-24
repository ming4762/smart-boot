package com.smart.framework.commons.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.framework.commons.core.utils.JsonUtils;
import lombok.*;

import java.io.Serial;
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
@Builder
@EqualsAndHashCode
public class Tree<T extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = -4056107665873673992L;

    private Serializable id = null;

    private String text = null;

    private T data = null;

    private Serializable parentId = null;

    @JsonIgnore
    private Tree<T> parent;

    @Builder.Default
    private List<Tree<T>> children = new ArrayList<>(0);

    @Builder.Default
    private Boolean hasParent = Boolean.FALSE;

    @Builder.Default
    private Boolean hasChildren = Boolean.FALSE;

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }
}

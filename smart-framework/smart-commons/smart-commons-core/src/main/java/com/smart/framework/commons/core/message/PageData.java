package com.smart.framework.commons.core.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页数据
 * @author shizhongming
 * 2020/1/12 8:30 下午
 */
@Getter
@Setter
public final class PageData<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3546130315339330080L;

    private transient List<T> rows;

    private long total;

    private PageData() {
        // do nothing
    }

    private PageData(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public static <T> PageData<T> of(List<T> rows, long total) {
        return new PageData<>(rows, total);
    }

}

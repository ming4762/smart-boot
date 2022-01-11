package com.smart.crud.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @author shizhongming
 * 2021/4/18 4:28 下午
 */
public class CrudPageHelper extends PageHelper {

    /**
     * 设置分页信息
     * @param page 分页信息
     */
    public static void setPage(Page<?> page) {
        setLocalPage(page);
    }
}

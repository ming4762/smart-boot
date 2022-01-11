package com.smart.starter.kettle.xxl.pojo.dto;

import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * kettle转换参数
 * @author shizhongming
 * 2021/7/18 8:54 下午
 */
@ToString
@Setter
public class KettleTransExecuteDTO extends AbstractKettleExecuteDTO {

    /**
     * 转换名字
     */
    private String transName;

    private List<String> params;

    public String getTransName() {
        Assert.notNull(this.transName, "转换名字不能为空");
        return transName;
    }

    public List<String> getParams() {
        if (params == null) {
            return new ArrayList<>(0);
        }
        return params;
    }
}

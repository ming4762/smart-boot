package com.smart.smc.inter.qingdaoport.dto.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 云港通返回列表结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 16:38
 * @since 5.0.0
 */
@Getter
@Setter
public class QingdaoPortListResult<T> extends QingdaoPortCommonResult {

    /**
     * 可能是list，也可能是data
     */
    private List<T> list;

    private List<T> data;
}

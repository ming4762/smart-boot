package com.smart.smc.inter.qingdaoport.dto.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云港通-智能转运平台-查询IMO号结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:21
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class QingdaoPortTransferImoResult implements Serializable {

    /**
     * 船舶MIO号
     */
    private String imo;

    /**
     * 码头代码
     */
    private String mtdm;
}

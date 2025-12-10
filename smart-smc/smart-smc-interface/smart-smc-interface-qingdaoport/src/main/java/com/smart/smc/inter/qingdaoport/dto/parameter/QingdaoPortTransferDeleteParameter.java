package com.smart.smc.inter.qingdaoport.dto.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 智能转运平台删除转运申请参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:40
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QingdaoPortTransferDeleteParameter implements Serializable {

    /**
     * 转运申请编号
     */
    @JsonProperty("TransferNo")
    private String transferNo;

    /**
     * 转运申请申报企业代码
     */
    @JsonProperty("AgentCopCode")
    private String agentCopCode;

    /**
     * 转运申请申报企业代码
     */
    @JsonProperty("AgentCopName")
    private String agentCopName;

    @JsonProperty("ContaInfos")
    private List<TransferDeleteConta> contaInfos;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TransferDeleteConta implements Serializable {

        @Serial
        private static final long serialVersionUID = -8266221457152672665L;
        @JsonProperty("ContaId")
        private String contaId;
    }
}

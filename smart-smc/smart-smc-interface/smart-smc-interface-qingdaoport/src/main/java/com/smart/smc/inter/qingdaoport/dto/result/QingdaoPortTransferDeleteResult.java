package com.smart.smc.inter.qingdaoport.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.smc.inter.qingdaoport.constants.QingdaoPortTransferVerifyStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 智能转运平台删除转运申请结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:41
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QingdaoPortTransferDeleteResult implements Serializable {

    @JsonProperty("AbnormalContaList")
    private List<AbnormalConta> abnormalContaList;

    @JsonProperty("ReturnCode")
    private Integer returnCode;

    @JsonProperty("ReturnMsg")
    private String returnMessage;

    @Getter
    @Setter
    @ToString
    public static class AbnormalConta implements Serializable {
        @Serial
        private static final long serialVersionUID = -7113635558698654851L;
        @JsonProperty("ContaId")
        private String contaId;

        @JsonProperty("BackReason")
        private String backReason;

        @JsonProperty("VerifyStatus")
        private QingdaoPortTransferVerifyStatusEnum verifyStatus;
    }
}

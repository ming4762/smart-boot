package com.smart.smc.inter.qingdaoport.dto.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.smc.inter.qingdaoport.constants.QingdaoPortTransferVerifyStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 云港通-智能转运平台-发送转运申请结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:29
 * @since 5.0.0
 */
@Getter
@Setter
public class QingdaoPortTransferApplyPushResult implements Serializable {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("TransferNo")
    private String transferNo;

    @JsonProperty("AbnormalContaList")
    private List<AbnormalConta> abnormalContaList;

    @JsonProperty("ContaId")
    private String contaId;

    @JsonProperty("BackReason")
    private String backReason;

    @JsonProperty("ReturnCode")
    private String returnCode;

    @JsonProperty("ReturnMsg")
    private String returnMsg;

    @JsonProperty("VerifyStatus")
    private String verifyStatus;

    @Getter
    @Setter
    public static class AbnormalConta implements Serializable {

        @Serial
        private static final long serialVersionUID = 1072373550608234939L;
        @JsonProperty("ContaId")
        private String contaId;

        @JsonProperty("BackReason")
        private String backReason;

        @JsonProperty("VerifyStatus")
        private QingdaoPortTransferVerifyStatusEnum verifyStatus;
    }
}

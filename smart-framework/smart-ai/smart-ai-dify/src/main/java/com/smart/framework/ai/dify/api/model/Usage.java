package com.smart.framework.ai.dify.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 模型用量信息
 * @author shizhongming
 * 2025/2/8 19:48
 * @since 5.0.0
 */
@Getter
@Setter
public class Usage {

    @JsonProperty("prompt_tokens")
    private Long promptTokens;

    @JsonProperty("prompt_unit_price")
    private BigDecimal promptUnitPrice;

    @JsonProperty("prompt_price_unit")
    private BigDecimal promptPriceUnit;

    @JsonProperty("prompt_price")
    private BigDecimal promptPrice;

    @JsonProperty("completion_tokens")
    private Long completionTokens;

    @JsonProperty("completion_unit_price")
    private BigDecimal completionUnitPrice;

    @JsonProperty("completion_price_unit")
    private BigDecimal completionPriceUnit;

    @JsonProperty("completion_price")
    private BigDecimal completionPrice;

    @JsonProperty("total_tokens")
    private Long totalTokens;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("latency")
    private BigDecimal latency;

}

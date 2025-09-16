package com.smart.smc.inter.sdport.pay.pojo.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/10/30 15:03
 * @since 1.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SdportPayCommonResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -674639879912399757L;

    private String message;

    private String status;

    private Boolean success;

    private transient Object data;

    private String code;
}

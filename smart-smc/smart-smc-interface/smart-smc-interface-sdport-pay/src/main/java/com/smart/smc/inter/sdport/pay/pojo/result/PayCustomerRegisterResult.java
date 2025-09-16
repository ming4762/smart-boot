package com.smart.smc.inter.sdport.pay.pojo.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smart.smc.inter.sdport.pay.constants.CustomerStatusEnum;
import com.smart.smc.inter.sdport.pay.jackson.JacksonConverter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/10/30 15:29
 * @since 1.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayCustomerRegisterResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 5050130141219594789L;

    private String cstNo;

    @JsonDeserialize(using = JacksonConverter.CustomerStatusEnumDeserializer.class)
    private CustomerStatusEnum cstStatus;

    private String fromSystem;
}

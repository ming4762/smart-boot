package com.smart.smc.inter.sdport.pay.pojo.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/10/30 15:54
 * @since 1.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayWakeupCashierResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -3769847400137809379L;


    private String empty;

    private transient Object model;

    private transient Object modelMap;

    private Boolean reference;

    private String status;

    private View view;

    private String viewName;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class View implements Serializable {

        @Serial
        private static final long serialVersionUID = -3730699437746518896L;
        
        private String contentType;
    }
}

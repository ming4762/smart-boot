package com.smart.smc.inter.sdport.pay;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/10/29 15:26
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("gc.business.interface.sdportpay")
public class SmartSmcSdportPayProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 4586008278660977199L;

    private String fromSystem;

}

package com.smart.framework.auth.core.share;

import lombok.*;

import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/11/8 16:07
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareLoginProperties implements Serializable {

    private String loginUrl;
}

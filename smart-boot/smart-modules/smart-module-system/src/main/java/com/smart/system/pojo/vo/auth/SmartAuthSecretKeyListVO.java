package com.smart.system.pojo.vo.auth;

import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import com.smart.system.model.auth.SmartAuthSecretKeyPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/2/19
 */
@Getter
@Setter
@ToString
public class SmartAuthSecretKeyListVO extends SmartAuthSecretKeyPO {

    private SmartFileStorageListDTO fileStorage;
}

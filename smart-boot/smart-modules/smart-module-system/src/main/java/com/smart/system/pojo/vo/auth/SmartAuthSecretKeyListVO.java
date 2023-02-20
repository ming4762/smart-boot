package com.smart.system.pojo.vo.auth;

import com.smart.system.model.auth.SmartAuthSecretKeyPO;
import com.smart.system.model.file.SmartFileStoragePO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhongming4762
 * 2023/2/19
 */
@Getter
@Setter
public class SmartAuthSecretKeyListVO extends SmartAuthSecretKeyPO {

    private SmartFileStoragePO fileStorage;
}

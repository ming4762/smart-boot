package com.smart.cloud.api.file.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.file.SmartFileStorageApi;
import com.smart.module.api.file.constants.SmartFileApiUrlConstants;
import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@FeignClient(value = CloudServiceNameConstants.FILE_SERVICE, contextId = "remoteSmartFileStorageApi")
public interface RemoteSmartFileStorageApi extends SmartFileStorageApi {

    /**
     * 通过ID查询列表
     *
     * @param idList ID列表
     * @return 文件存储器列表
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.FILE_STORAGE_LIST_BY_ID)
    List<SmartFileStorageListDTO> listByIds(Collection<Long> idList);
}

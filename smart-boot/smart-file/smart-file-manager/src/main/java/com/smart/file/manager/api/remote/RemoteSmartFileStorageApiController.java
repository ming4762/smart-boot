package com.smart.file.manager.api.remote;

import com.smart.file.manager.api.local.LocalSmartFileStorageApi;
import com.smart.module.api.file.SmartFileStorageApi;
import com.smart.module.api.file.constants.SmartFileApiUrlConstants;
import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@RestController
@RequestMapping
public class RemoteSmartFileStorageApiController implements SmartFileStorageApi {

    private final LocalSmartFileStorageApi smartFileStorageApi;

    public RemoteSmartFileStorageApiController(LocalSmartFileStorageApi smartFileStorageApi) {
        this.smartFileStorageApi = smartFileStorageApi;
    }

    /**
     * 通过ID查询列表
     *
     * @param idList ID列表
     * @return 文件存储器列表
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.FILE_STORAGE_LIST_BY_ID)
    public List<SmartFileStorageListDTO> listByIds(@RequestBody Collection<Long> idList) {
        return this.smartFileStorageApi.listByIds(idList);
    }
}

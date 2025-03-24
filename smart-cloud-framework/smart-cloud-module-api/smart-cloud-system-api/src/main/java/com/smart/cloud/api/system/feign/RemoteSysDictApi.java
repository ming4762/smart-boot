package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysDictApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysDictItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/6/1 14:41
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "sysDictApi")
public interface RemoteSysDictApi extends SysDictApi {

    /**
     * 通过字典编码查询字典项
     *
     * @param dictCode 字典编码
     * @return 字典项
     */
    @Override
    @PostMapping(SystemApiUrlConstants.DICT_LIST_BY_CODE)
    List<SysDictItemDTO> listByDictCode(String dictCode);

    /**
     * 通过字典编码批量查询字典项
     *
     * @param dictCode 字典编
     * @return 字典编码为key，字典项为value的list
     */
    @Override
    @PostMapping(SystemApiUrlConstants.DICT_BATCH_LIST_BY_CODE)
    Map<String, List<SysDictItemDTO>> listByDictCode(List<String> dictCode);
}

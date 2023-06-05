package com.smart.system.api.remote;

import com.smart.module.api.system.SysDictApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysDictItemDTO;
import com.smart.system.api.local.LocalSysDictApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/6/1
 */
@RestController
@RequestMapping
public class RemoteSysDictApiController implements SysDictApi {

    private final LocalSysDictApi localSysDictApi;

    public RemoteSysDictApiController(LocalSysDictApi localSysDictApi) {
        this.localSysDictApi = localSysDictApi;
    }

    /**
     * 通过字典编码查询字典项
     *
     * @param dictCode 字典编码
     * @return 字典项
     */
    @Override
    @PostMapping(SystemApiUrlConstants.DICT_LIST_BY_CODE)
    public List<SysDictItemDTO> listByDictCode(@RequestBody String dictCode) {
        return this.localSysDictApi.listByDictCode(dictCode);
    }

    /**
     * 通过字典编码批量查询字典项
     *
     * @param dictCode 字典编
     * @return 字典编码为key，字典项为value的list
     */
    @Override
    @PostMapping(SystemApiUrlConstants.DICT_BATCH_LIST_BY_CODE)
    public Map<String, List<SysDictItemDTO>> listByDictCode(@RequestBody List<String> dictCode) {
        return this.localSysDictApi.listByDictCode(dictCode);
    }
}

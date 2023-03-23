package com.smart.system.controller.auth;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.IdParameter;
import com.smart.system.model.auth.SmartAuthSecretKeyPO;
import com.smart.system.pojo.dto.auth.SmartAuthSecretKeyListDTO;
import com.smart.system.pojo.dto.auth.SmartAuthSecretKeyUploadUpdateDTO;
import com.smart.system.service.auth.SmartAuthSecretKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
* smart_auth_secret_key - 秘钥管理 Controller
* @author SmartCodeGenerator
* 2023-2-19 10:57:38
*/
@RestController
@RequestMapping("/sys/auth/secret")
@Tag(name = "秘钥管理")
public class SmartAuthSecretKeyController extends BaseController<SmartAuthSecretKeyService, SmartAuthSecretKeyPO> {

    @PostMapping("listBySystem")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('auth:secret', 'query')")
    public Result<Object> listBySystem(@RequestBody @NonNull SmartAuthSecretKeyListDTO parameter) {
        if (parameter.getSystemId() == null) {
            return Result.success();
        }
        parameter.getParameter().put(CrudCommonEnum.WITH_ALL.name(), true);
        parameter.getParameter().put("systemId@=", parameter.getSystemId());
        return super.list(parameter);
    }


    @Override
    @Operation(summary = "通过ID批量删除秘钥管理")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除秘钥管理", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('auth:secret', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('auth:secret', 'query')")
    public Result<SmartAuthSecretKeyPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }


    @Operation(summary = "上传秘钥")
    @PreAuthorize("hasPermission('auth:secret', 'save')")
    @PostMapping("saveUpdate")
    public Result<Boolean> saveUpdate(@Valid SmartAuthSecretKeyUploadUpdateDTO parameter) {
        return Result.success(this.service.saveUpdate(parameter));
    }


    @SneakyThrows(IOException.class)
    @PreAuthorize("hasPermission('auth:secret', 'download')")
    @Operation(summary = "下载秘钥")
    @PostMapping("download")
    public void download(@RequestBody IdParameter parameter, HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=secretKey.zip");
        this.service.download(parameter.getId(), response.getOutputStream());
    }

}
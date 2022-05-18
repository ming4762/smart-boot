package com.smart.module.document.controller;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.module.document.model.DocumentTemplatePO;
import com.smart.module.document.pojo.dto.template.DocumentTemplateSaveUpdateDTO;
import com.smart.module.document.service.DocumentTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 模板controller
 * @author shizhongming
 * 2021/8/14 8:14 下午
 */
@RestController
@RequestMapping("document/template")
@Api(value = "模板管理接口", tags = "模板管理接口")
public class DocumentTemplateController extends BaseController<DocumentTemplateService, DocumentTemplatePO> {

    /**
     * 保存修改模板
     * @param parameter 参数
     * @return 保存结果
     */
    @PostMapping("saveUpdate")
    @ApiOperation("上传/修改模板")
    public Result<Boolean> saveUpdate(@Valid DocumentTemplateSaveUpdateDTO parameter) throws IOException {
        DocumentTemplatePO model = new DocumentTemplatePO();
        BeanUtils.copyProperties(parameter, model);
        if (parameter.getFile() != null) {
            model.setData(parameter.getFile().getBytes());
        }
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }



    @Override
    @PostMapping("batchDeleteById")
    @ApiOperation("批量删除模板")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @PostMapping("list")
    @ApiOperation("查询模板列表")
    public Result<Object> list(@NonNull @RequestBody PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    @ApiOperation("通过ID查询模板")
    public Result<DocumentTemplatePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}

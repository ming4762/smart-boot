package com.smart.db.generator.controller;

import com.google.common.collect.Lists;
import com.smart.commons.core.document.DocumentVO;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.db.generator.constants.RuleTypeEnum;
import com.smart.db.generator.constants.TableTypeEnum;
import com.smart.db.generator.document.DbGeneratorDocumentCreator;
import com.smart.db.generator.model.DbCodeFormConfigCommonPO;
import com.smart.db.generator.model.DbCodeMainPO;
import com.smart.db.generator.pojo.dto.DbCodeMainSaveParameter;
import com.smart.db.generator.pojo.dto.DbCreateCodeDTO;
import com.smart.db.generator.pojo.dto.main.DbCodeMainListBySystemDTO;
import com.smart.db.generator.pojo.vo.DbCodeVO;
import com.smart.db.generator.pojo.vo.DbMainConfigVO;
import com.smart.db.generator.service.DbCodeMainService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:38
 * @since 1.0
 */
@RestController
@RequestMapping("db/code/main")
public class DbCodeMainController extends BaseController<DbCodeMainService, DbCodeMainPO> {

    @Override
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('db:codeConfig', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @PostMapping("list")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @PostMapping("listBySystem")
    @Operation(summary = "查询列表，如果systemId为null，则不返回空")
    public Result<Object> listBySystem(@RequestBody @Valid DbCodeMainListBySystemDTO parameter) {
        if (parameter.getSystemId() == null) {
            return Result.success();
        }
        parameter.getParameter().put("systemId@=", parameter.getSystemId());
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    public Result<DbCodeMainPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 通过ID获取配置信息
     * @param id 配置ID
     * @return 配置信息
     */
    @PostMapping("getConfigById")
    public Result<DbMainConfigVO> getConfigById(@RequestBody Long id) {
        if (id == null) {
            return Result.failure(400, "参数错误，配置ID不能为空");
        }
        return Result.success(this.service.getConfigById(id));
    }

    @PostMapping("save")
    @PreAuthorize("hasPermission('db:codeConfig', 'save')")
    public Result<Long> save(@RequestBody @Valid DbCodeMainSaveParameter parameter) {
        final List<String> message = Lists.newArrayList();
        // 验证参数
        // 1、验证类型和附表
        if (StringUtils.equals(parameter.getType(), TableTypeEnum.MAIN.getType()) && CollectionUtils.isEmpty(parameter.getAddendumTableList())) {
            message.add("请指定主表对应的附表");
        }
        // 2、验证表格查询 参数是否满足
        parameter.getCodeFormConfigList().forEach(item -> {
            this.validateTableSearch(item, message);
            this.validateRule(item.getRuleList(), message);
        });
        parameter.getCodeSearchConfigList().forEach(item -> this.validateTableSearch(item, message));
        if (!message.isEmpty()) {
            throw new BusinessException(String.join(",", message));
        }
        return Result.success(this.service.saveUpdate(parameter));
    }

    @PostMapping("createCode")
    public Result<List<DbCodeVO>> createCode(@RequestBody @Valid DbCreateCodeDTO parameter) {
       return Result.success(this.service.createCode(parameter));
    }

    /**
     * 获取模板数据文档
     * @return 模板数据文档
     */
    @PostMapping("getTemplateDataDocument")
    public Result<List<DocumentVO>> getTemplateDataDocument() {
        return Result.success(DbGeneratorDocumentCreator.createDocument());
    }

    /**
     * 验证 使用表格搜索是否满足条件
     * @param data 数据
     */
    private void validateTableSearch(DbCodeFormConfigCommonPO data, List<String> message) {
        if (Boolean.TRUE.equals(data.getUseTableSearch()) && !(StringUtils.isNotBlank(data.getTableName()) && StringUtils.isNotBlank(data.getKeyColumnName()) && StringUtils.isNotBlank(data.getValueColumnName()))) {
            message.add(String.format("使用表格查询，必须指定数据库信息，字段：%s", data.getColumnName()));
        }
    }

    /**
     * 验证 验证规则参数
     * @param ruleList 验证规则列表
     * @param message 结果信息
     */
    private void validateRule(@Nullable List<DbCodeMainSaveParameter.DbCodeRuleSaveDTO> ruleList, List<String> message) {
        if (CollectionUtils.isEmpty(ruleList)) {
            return;
        }
        ruleList.forEach(rule -> {
            if (RuleTypeEnum.REGEXP.equals(rule.getRuleType()) && StringUtils.isBlank(rule.getPattern())) {
                message.add("参数校验失败，验证类型为正则必须设置正则表达式");
            }
        });
    }
}

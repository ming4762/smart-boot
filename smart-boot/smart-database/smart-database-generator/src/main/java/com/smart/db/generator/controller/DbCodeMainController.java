package com.smart.db.generator.controller;

import com.google.common.collect.Lists;
import com.smart.commons.core.document.DocumentVO;
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
import com.smart.db.generator.pojo.vo.DbCodeVO;
import com.smart.db.generator.pojo.vo.DbMainConfigVO;
import com.smart.db.generator.service.DbCodeMainService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Override
    @PostMapping("getById")
    public Result<DbCodeMainPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * ??????ID??????????????????
     * @param id ??????ID
     * @return ????????????
     */
    @PostMapping("getConfigById")
    public Result<DbMainConfigVO> getConfigById(@RequestBody Long id) {
        if (id == null) {
            return Result.failure(400, "?????????????????????ID????????????");
        }
        return Result.success(this.service.getConfigById(id));
    }

    @PostMapping("save")
    @PreAuthorize("hasPermission('db:codeConfig', 'save')")
    public Result<List<String>> save(@RequestBody @Valid DbCodeMainSaveParameter parameter) {
        final List<String> message = Lists.newArrayList();
        // ????????????
        // 1????????????????????????
        if (StringUtils.equals(parameter.getType(), TableTypeEnum.MAIN.getType()) && CollectionUtils.isEmpty(parameter.getAddendumTableList())) {
            message.add("??????????????????????????????");
        }
        // 2????????????????????? ??????????????????
        parameter.getCodeFormConfigList().forEach(item -> {
            this.validateTableSearch(item, message);
            this.validateRule(item.getRuleList(), message);
        });
        parameter.getCodeSearchConfigList().forEach(item -> this.validateTableSearch(item, message));
        if (!message.isEmpty()) {
            return Result.failure(400, "??????????????????", message);
        }
        this.service.saveUpdate(parameter);
        return Result.success(message);
    }

    @PostMapping("createCode")
    public Result<List<DbCodeVO>> createCode(@RequestBody @Valid DbCreateCodeDTO parameter) {
       return Result.success(this.service.createCode(parameter));
    }

    /**
     * ????????????????????????
     * @return ??????????????????
     */
    @PostMapping("getTemplateDataDocument")
    public Result<List<DocumentVO>> getTemplateDataDocument() {
        return Result.success(DbGeneratorDocumentCreator.createDocument());
    }

    /**
     * ?????? ????????????????????????????????????
     * @param data ??????
     */
    private void validateTableSearch(DbCodeFormConfigCommonPO data, List<String> message) {
        if (Boolean.TRUE.equals(data.getUseTableSearch()) && !(StringUtils.isNotBlank(data.getTableName()) && StringUtils.isNotBlank(data.getKeyColumnName()) && StringUtils.isNotBlank(data.getValueColumnName()))) {
            message.add(String.format("????????????????????????????????????????????????????????????%s", data.getColumnName()));
        }
    }

    /**
     * ?????? ??????????????????
     * @param ruleList ??????????????????
     * @param message ????????????
     */
    private void validateRule(@Nullable List<DbCodeMainSaveParameter.DbCodeRuleSaveDTO> ruleList, List<String> message) {
        if (CollectionUtils.isEmpty(ruleList)) {
            return;
        }
        ruleList.forEach(rule -> {
            if (RuleTypeEnum.REGEXP.equals(rule.getRuleType()) && StringUtils.isBlank(rule.getPattern())) {
                message.add("?????????????????????????????????????????????????????????????????????");
            }
        });
    }
}

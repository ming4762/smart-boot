package com.smart.db.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.exception.BaseException;
import com.smart.commons.core.exception.BusinessException;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.db.generator.constants.*;
import com.smart.db.generator.engine.TemplateEngine;
import com.smart.db.generator.mapper.DbCodeMainMapper;
import com.smart.db.generator.model.*;
import com.smart.db.generator.pojo.dto.*;
import com.smart.db.generator.pojo.query.RelatedTableDeleteByMainConfigQuery;
import com.smart.db.generator.pojo.vo.*;
import com.smart.db.generator.service.*;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:37
 * @since 1.0
 */
@Service
public class DbCodeMainServiceImpl extends BaseServiceImpl<DbCodeMainMapper, DbCodeMainPO> implements DbCodeMainService {

    private final SysUserService sysUserService;
    private final DbCodePageConfigService dbCodePageConfigService;

    private final DbCodeFormConfigService dbCodeFormConfigService;

    private final DbConnectionService databaseConnectionService;

    private final DbCodeTemplateService dbCodeTemplateService;

    private final TemplateEngine templateEngine;

    private final DbCodeSearchConfigService dbCodeSearchConfigService;

    private final DbCodeButtonConfigService dbCodeButtonConfigService;

    private final DbCodeRelatedTableService dbCodeRelatedTableService;

    private final DbCodeRuleConfigService dbCodeRuleConfigService;

    public DbCodeMainServiceImpl(DbCodePageConfigService dbCodePageConfigService, DbCodeFormConfigService dbCodeFormConfigService, DbConnectionService databaseConnectionService, DbCodeTemplateService dbCodeTemplateService, TemplateEngine templateEngine, DbCodeSearchConfigService dbCodeSearchConfigService, DbCodeButtonConfigService dbCodeButtonConfigService, DbCodeRelatedTableService dbCodeRelatedTableService, DbCodeRuleConfigService dbCodeRuleConfigService, SysUserService sysUserService) {
        this.dbCodePageConfigService = dbCodePageConfigService;
        this.dbCodeFormConfigService = dbCodeFormConfigService;
        this.databaseConnectionService = databaseConnectionService;
        this.dbCodeTemplateService = dbCodeTemplateService;
        this.templateEngine = templateEngine;
        this.dbCodeSearchConfigService = dbCodeSearchConfigService;
        this.dbCodeButtonConfigService = dbCodeButtonConfigService;
        this.dbCodeRelatedTableService = dbCodeRelatedTableService;
        this.dbCodeRuleConfigService = dbCodeRuleConfigService;
        this.sysUserService = sysUserService;
    }

    @Override
    public List<? extends DbCodeMainPO> list(@NonNull QueryWrapper<DbCodeMainPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        // 设置权限，超级管理员可以查看所有，普通用户只能查看自己的
        if (!AuthUtils.isSuperAdmin()) {
            queryWrapper.lambda()
                    .eq(DbCodeMainPO :: getCreateUserId, AuthUtils.getNonNullCurrentUserId());
        }
        // 执行查询
        final List<? extends DbCodeMainPO> dbCodeMainList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(dbCodeMainList)) {
            return dbCodeMainList;
        }
        // 将结果转为VO
        final List<DbCodeMainPO> voList = dbCodeMainList.stream()
                .map(item -> {
                    final DbCodeMainListVO dbCodeMainListVo = new DbCodeMainListVO();
                    BeanUtils.copyProperties(item, dbCodeMainListVo);
                    return dbCodeMainListVo;
                }).collect(Collectors.toList());

        // 设置连接信息
        this.setConnection(voList);
        // 设置创建人修改人
        this.setCreateUpdateUser(voList);
        return voList;
    }

    /**
     * 设置创建人修改人
     * @param voList voList
     */
    private void setCreateUpdateUser(List<DbCodeMainPO> voList) {
        // 查询添加人/修改人
        Set<Long> userIds = Sets.newHashSet();
        voList.forEach(item -> {
            userIds.add(item.getCreateUserId());
            userIds.add(item.getUpdateUserId());
        });
        final Set<Long> nonNullUserIds = userIds.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (nonNullUserIds.isEmpty()) {
            return;
        }
        final Map<Long, SysUserPO> userMap = this.sysUserService.list(
                new QueryWrapper<SysUserPO>().lambda()
                        .select(SysUserPO :: getUserId, SysUserPO :: getUsername, SysUserPO :: getFullName)
                        .in(SysUserPO :: getUserId, nonNullUserIds)
        ).stream().collect(Collectors.toMap(SysUserPO :: getUserId, item -> item));
        voList.forEach(item -> {
            ((DbCodeMainListVO)item).setCreateUser(userMap.get(item.getCreateUserId()));
            ((DbCodeMainListVO)item).setUpdateUser(userMap.get(item.getUpdateUserId()));
        });
    }

    /**
     * 设置连接信息
     * @param voList voList
     */
    private void setConnection(List<DbCodeMainPO> voList) {
        // 查询连接信息
        final Set<Long> connectionIds = voList.stream().map(DbCodeMainPO :: getConnectionId).collect(Collectors.toSet());
        Map<Long, String> connectionMap = this.databaseConnectionService.list(
                new QueryWrapper<DbConnectionPO>().lambda()
                        .in(DbConnectionPO :: getId, connectionIds)
                        .select(DbConnectionPO :: getConnectionName, DbConnectionPO :: getId)
        ).stream().collect(Collectors.toMap(DbConnectionPO :: getId, DbConnectionPO :: getConnectionName));
        voList.forEach(item -> ((DbCodeMainListVO)item).setConnectionName(connectionMap.get(item.getConnectionId())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdate(DbCodeMainSaveParameter model) {
        final DbCodeMainPO dbCodeMain = new DbCodeMainPO();
        BeanUtils.copyProperties(model, dbCodeMain);
        // 判断是否是添加操作
        final boolean isAdd = this.isAdd(dbCodeMain);
        final long mainId = IdGenerator.nextId();
        // 如果是修改执行删除操作
        if (!isAdd) {
            this.removeByIds(Lists.newArrayList(model.getId()));
        }
        // 保存主表
        dbCodeMain.setId(mainId);
        this.saveWithUser(dbCodeMain, AuthUtils.getCurrentUserId());
        final AtomicInteger pageConfigIndex = new AtomicInteger(1);
        model.setId(mainId);
        // 保存表格配置
        this.dbCodePageConfigService.saveBatchWithUser(
                model.getCodePageConfigList().stream().peek(item -> {
                    item.setMainId(mainId);
                    item.setId(null);
                    // 设置序号
                    item.setSeq(pageConfigIndex.getAndIncrement());
                }).collect(Collectors.toList()),
                AuthUtils.getCurrentUserId()
        );
        // 保存附表配置
        if (StringUtils.equals(model.getType(), TableTypeEnum.MAIN.getType())) {
            this.saveRelatedTable(model);
        }
        // 保存Form配置
        this.saveFormConfig(model);
        // 保存搜索配置信息
        this.saveSearchConfig(model);
        // 保存按钮配置
        this.saveButtonConfig(model, mainId);
        return true;
    }

    /**
     * 保存附表信息
     * @param model 主表信息
     */
    private void saveRelatedTable(@NonNull DbCodeMainSaveParameter model) {
        final AtomicInteger atomicInteger = new AtomicInteger(1);
        final List<DbCodeRelatedTablePO> relatedTableList = model.getAddendumTableList().stream()
                .map(item -> {
                    final DbCodeRelatedTablePO relatedTable = new DbCodeRelatedTablePO();
                    relatedTable.setMainId(model.getId());
                    relatedTable.setAddendumId(item.getAddendumId());
                    relatedTable.setRelatedColumn(item.getRelatedColumn());
                    relatedTable.setIdent(RelatedTableIdentEnum.MAIN_TABLE);
                    relatedTable.setSeq(atomicInteger.getAndIncrement());
                    return relatedTable;
                }).collect(Collectors.toList());
        this.dbCodeRelatedTableService.saveBatch(relatedTableList);
    }

    /**
     * 保存搜索配置信息
     * @param model model
     */
    private void saveSearchConfig(@NonNull DbCodeMainSaveParameter model) {
        // 设置form 配置信息
        final AtomicInteger searchConfigIndex = new AtomicInteger(1);
        this.dbCodeSearchConfigService.saveBatchWithUser(
                model.getCodeSearchConfigList().stream().peek(item -> {
                    item.setMainId(model.getId());
                    item.setId(IdGenerator.nextId());
                    item.setSeq(searchConfigIndex.getAndIncrement());
                }).collect(Collectors.toList()),
                AuthUtils.getCurrentUserId()
        );

        // 保存表关联信息
        final List<DbCodeRelatedTablePO> relatedTableList = Lists.newArrayList();
        model.getCodeSearchConfigList().forEach(search -> {
            if (CollectionUtils.isNotEmpty(search.getSelectTableList())) {
                relatedTableList.addAll(search.getSelectTableList().stream().map(table -> {
                    final DbCodeRelatedTablePO dbCodeRelatedTable = new DbCodeRelatedTablePO();
                    BeanUtils.copyProperties(table, dbCodeRelatedTable);
                    dbCodeRelatedTable.setIdent(RelatedTableIdentEnum.SEARCH);
                    dbCodeRelatedTable.setMainId(search.getId());
                    dbCodeRelatedTable.setType(RelatedTableTypeEnum.SELECT_TABLE);
                    return dbCodeRelatedTable;
                }).collect(Collectors.toList()));
            }
        });
        if (!relatedTableList.isEmpty()) {
            this.dbCodeRelatedTableService.saveBatch(relatedTableList);
        }
    }

    /**
     * 保存form配置信息
     * @param model model
     */
    private void saveFormConfig(@NonNull DbCodeMainSaveParameter model) {
        // 设置form 配置信息
        final AtomicInteger formConfigIndex = new AtomicInteger(1);
        this.dbCodeFormConfigService.saveBatchWithUser(
                model.getCodeFormConfigList().stream().peek(item -> {
                    item.setMainId(model.getId());
                    item.setId(IdGenerator.nextId());
                    item.setSeq(formConfigIndex.getAndIncrement());
                }).collect(Collectors.toList()),
                AuthUtils.getCurrentUserId()
        );
        // 保存表关联信息
        final List<DbCodeRelatedTablePO> relatedTableList = Lists.newArrayList();
        model.getCodeFormConfigList().forEach(item -> {
            if (CollectionUtils.isNotEmpty(item.getSelectTableList())) {
                relatedTableList.addAll(item.getSelectTableList().stream().map(table -> {
                    final DbCodeRelatedTablePO dbCodeRelatedTable = new DbCodeRelatedTablePO();
                    BeanUtils.copyProperties(table, dbCodeRelatedTable);
                    dbCodeRelatedTable.setIdent(RelatedTableIdentEnum.FORM);
                    dbCodeRelatedTable.setMainId(item.getId());
                    dbCodeRelatedTable.setType(RelatedTableTypeEnum.SELECT_TABLE);
                    return dbCodeRelatedTable;
                }).collect(Collectors.toList()));
            }
        });
        if (!relatedTableList.isEmpty()) {
            this.dbCodeRelatedTableService.saveBatch(relatedTableList);
        }
        // 保存rule配置
        final List<DbCodeRuleConfigPO> ruleConfigList = Lists.newArrayList();
        model.getCodeFormConfigList().forEach(item -> {
            if (CollectionUtils.isNotEmpty(item.getRuleList())) {
                final AtomicInteger ruleSeq = new AtomicInteger();
                ruleConfigList.addAll(item.getRuleList().stream().map(rule -> {
                    final DbCodeRuleConfigPO dbCodeRuleConfig = new DbCodeRuleConfigPO();
                    BeanUtils.copyProperties(rule, dbCodeRuleConfig);
                    dbCodeRuleConfig.setSeq(ruleSeq.incrementAndGet());
                    dbCodeRuleConfig.setIdent(RuleIdentEnum.SAVE_FORM);
                    dbCodeRuleConfig.setRelationId(item.getId());
                    return dbCodeRuleConfig;
                }).collect(Collectors.toList()));
            }
        });
        if (!ruleConfigList.isEmpty()) {
            this.dbCodeRuleConfigService.saveBatch(ruleConfigList);
        }
    }

    /**
     * 保存按钮配置
     * @param model 主配置信息
     * @param mainId 主ID
     */
    private void saveButtonConfig(DbCodeMainSaveParameter model, long mainId) {
        final List<DbCodeButtonConfigPO> codeButtonConfigList = Lists.newArrayList();
        final AtomicInteger left = new AtomicInteger(1);
        final AtomicInteger right = new AtomicInteger(1);
        final AtomicInteger row = new AtomicInteger(1);
        // 保存按钮配置
        if (CollectionUtils.isNotEmpty(model.getLeftButtonList())) {
            codeButtonConfigList.addAll(
                    model.getLeftButtonList().stream().map(item ->
                            DbCodeButtonConfigPO.builder()
                                    .button(item.name())
                                    .ident(ButtonIdentEnum.LEFT_BUTTON)
                                    .relatedId(mainId)
                                    .seq(left.getAndIncrement())
                                    .build()
                    ).collect(Collectors.toList())
            );
        }
        if (CollectionUtils.isNotEmpty(model.getRightButtonList())) {
            codeButtonConfigList.addAll(
                    model.getRightButtonList().stream().map(item ->
                            DbCodeButtonConfigPO.builder()
                                    .button(item.name())
                                    .ident(ButtonIdentEnum.RIGHT_BUTTON)
                                    .relatedId(mainId)
                                    .seq(right.getAndIncrement())
                                    .build()
                    ).collect(Collectors.toList())
            );
        }
        if (CollectionUtils.isNotEmpty(model.getRowButtonList())) {
            codeButtonConfigList.addAll(
                    model.getRowButtonList().stream().map(item ->
                            DbCodeButtonConfigPO.builder()
                                    .button(item.name())
                                    .ident(ButtonIdentEnum.ROW_BUTTON)
                                    .relatedId(mainId)
                                    .seq(row.getAndIncrement())
                                    .build()
                    ).collect(Collectors.toList())
            );
        }
        if (!codeButtonConfigList.isEmpty()) {
            this.dbCodeButtonConfigService.saveBatch(codeButtonConfigList);
        }
    }

    @Override
    public List<DbCodeVO> createCode(@NonNull DbCreateCodeDTO parameter) {
        // 生成数据
        final DbCodeTemplateDataVO dbCodeTemplateData = new DbCodeTemplateDataVO();
        BeanUtils.copyProperties(parameter, dbCodeTemplateData);
        // 查询主表信息
        final DbTemplateCodeTableDTO main = this.createDbTemplateCodeMain(parameter.getMainId());
        if (StringUtils.equals(main.getType(), TableTypeEnum.ADDENDUM.getType())) {
            throw new BusinessException("请使用附表对应的主表生成代码");
        }
        dbCodeTemplateData.setMainTable(main);
        // 判断是否是主表，如果是主表，查询附表信息
        if (StringUtils.equals(main.getType(), TableTypeEnum.MAIN.getType())) {
            dbCodeTemplateData.setAddendumTableList(this.listAddendumTable(parameter.getMainId()));
        } else {
            dbCodeTemplateData.setAddendumTableList(Lists.newArrayList());
        }
        // 查询模板信息并生成代码
        final List<DbCodeTemplatePO> dbCodeTemplateList = this.dbCodeTemplateService.list(
                new QueryWrapper<DbCodeTemplatePO>().lambda()
                .select(DbCodeTemplatePO :: getTemplateId, DbCodeTemplatePO :: getName, DbCodeTemplatePO :: getLanguage, DbCodeTemplatePO :: getTemplate, DbCodeTemplatePO :: getFilenameSuffix)
                .in(DbCodeTemplatePO :: getTemplateId, parameter.getTemplateIdList())
        );
        return dbCodeTemplateList.stream().map(template -> {
            String code;
            try (final StringWriter stringWriter = new StringWriter()) {
                this.templateEngine.processTemplate(dbCodeTemplateData, template.getName(), template.getTemplate(), stringWriter);
                code = stringWriter.toString();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                code = e.getMessage();
            }
            return DbCodeVO.builder()
                    .templateId(template.getTemplateId())
                    .templateName(template.getName())
                    .language(template.getLanguage())
                    .filename(dbCodeTemplateData.getClassName() + template.getFilenameSuffix())
                    .code(code)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 通过ID获取配置信息
     * @param id 配置ID
     * @return 配置信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public DbMainConfigVO getConfigById(@NonNull Long id) {
        final DbMainConfigVO result = new DbMainConfigVO();
        // 1、获取主配置信息
        final DbCodeMainPO dbCodeMain = this.getById(id);
        if (dbCodeMain == null) {
            throw new BusinessException("获取主配置信息失败，请检查ID是否正确");
        }
        BeanUtils.copyProperties(dbCodeMain, result);
        // 2、获取附表信息
        if (StringUtils.equals(dbCodeMain.getType(), TableTypeEnum.MAIN.getType())) {
            // 查询附表ID集合
            // 查询附表ID
            final List<DbCodeRelatedTablePO> relatedTableList = this.dbCodeRelatedTableService.list(
                    new QueryWrapper<DbCodeRelatedTablePO>().lambda()
                    .eq(DbCodeRelatedTablePO :: getMainId, id)
                    .eq(DbCodeRelatedTablePO :: getIdent, RelatedTableIdentEnum.MAIN_TABLE)
                    .orderByAsc(DbCodeRelatedTablePO :: getSeq)
            );
            final List<Long> relatedTableIdList = relatedTableList.stream().map(DbCodeRelatedTablePO :: getAddendumId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(relatedTableIdList)) {
                final Map<Long, DbCodeMainPO> dbCodeMainIdMap = this.listByIds(relatedTableIdList)
                        .stream().collect(Collectors.toMap(DbCodeMainPO :: getId, item -> item));
                result.setRelatedTableList(relatedTableList.stream().map(item -> {
                    final DbCodeMainPO relatedTable = dbCodeMainIdMap.get(item.getAddendumId());
                    Assert.notNull(relatedTable, "系统发生未知错误，附表不存在");
                    return DbMainConfigVO.DbCodeRelatedTableVO.builder()
                            .mainId(id)
                            .addendumId(item.getAddendumId())
                            .configName(relatedTable.getConfigName())
                            .relatedColumn(item.getRelatedColumn())
                            .build();
                }).collect(Collectors.toList()));
            }
        }
        // 3、获取页面配置信息
        result.setCodePageConfigList(this.listDbCodePageConfig(dbCodeMain.getId()));
        // 4、获取表格配置信息
        result.setCodeFormConfigList(this.listDbCodeFormConfigDto(dbCodeMain.getId(), false));
        // 5、获取搜索配置信息
        result.setCodeSearchConfigList(this.listDbCodeSearchConfigDto(dbCodeMain.getId(), false));
        // 6、获取按钮配置
        final Map<ButtonIdentEnum, List<ButtonListEnum>> buttonMap = this.dbCodeButtonConfigService.queryButtonMapByRelatedId(dbCodeMain.getId());
        result.setLeftButtonList(buttonMap.get(ButtonIdentEnum.LEFT_BUTTON) == null ? Lists.newArrayList() : buttonMap.get(ButtonIdentEnum.LEFT_BUTTON));
        result.setRightButtonList(buttonMap.get(ButtonIdentEnum.RIGHT_BUTTON) == null ? Lists.newArrayList() : buttonMap.get(ButtonIdentEnum.RIGHT_BUTTON));
        result.setRowButtonList(buttonMap.get(ButtonIdentEnum.ROW_BUTTON) == null ? Lists.newArrayList() : buttonMap.get(ButtonIdentEnum.ROW_BUTTON));
        return result;
    }


    /**
     * 查询附表数据
     * @param mainId 主ID
     * @return 附表数据
     */
    private List<DbTemplateCodeTableDTO> listAddendumTable(@NonNull Long mainId) {
        final List<DbCodeRelatedTablePO> relatedTableList = this.dbCodeRelatedTableService.list(
                new QueryWrapper<DbCodeRelatedTablePO>().lambda()
                        .select(DbCodeRelatedTablePO :: getRelatedColumn, DbCodeRelatedTablePO :: getAddendumId, DbCodeRelatedTablePO :: getMainId)
                        .eq(DbCodeRelatedTablePO ::getMainId, mainId)
                        .eq(DbCodeRelatedTablePO :: getIdent, RelatedTableIdentEnum.MAIN_TABLE)
                        .orderByAsc(DbCodeRelatedTablePO :: getSeq)
        );
        if (relatedTableList.isEmpty()) {
            return Lists.newArrayList();
        }
        // 查询附表信息
        final Set<Long> relatedIdList = relatedTableList.stream().map(DbCodeRelatedTablePO :: getAddendumId).collect(Collectors.toSet());
        Map<Long, DbTemplateCodeTableDTO> result = relatedIdList.stream().map(this :: createDbTemplateCodeMain)
                .collect(Collectors.toMap(DbTemplateCodeTableDTO :: getId, item -> item));
        return relatedTableList.stream().map(item -> {
            final DbTemplateCodeTableDTO templateCodeTable = result.get(item.getAddendumId());
            Assert.notNull(templateCodeTable, "查询附表失败，未查询到指定附表");
            templateCodeTable.setRelatedColumn(item.getRelatedColumn());
            return templateCodeTable;
        }).collect(Collectors.toList());
    }

    /**
     * 创建单一表格数据
     * @param mainId 表格ID
     * @return 单一表格数据
     */
    private DbTemplateCodeTableDTO createDbTemplateCodeMain (@NonNull Long mainId) {
        // 1、查询主体信息
        final DbCodeMainPO dbCodeMain = this.getById(mainId);
        Assert.notNull(dbCodeMain, "系统发生错误，无法查询到代码生成配置");

        DbTemplateCodeTableDTO dbTemplateCodeMain = new DbTemplateCodeTableDTO();
        BeanUtils.copyProperties(dbCodeMain, dbTemplateCodeMain);
        // 2、处理页面配置数据
        this.dealCodePageConfig(dbTemplateCodeMain);
        // 3、查询 form配置
        this.queryCodeFormTemplateData(dbTemplateCodeMain);
        // 4、查询搜索配置
        this.queryCodeSearchTemplateData(dbTemplateCodeMain);

        // 5、查询按钮配置
        final Map<ButtonIdentEnum, List<ButtonListEnum>> buttonMap = this.dbCodeButtonConfigService.queryButtonMapByRelatedId(dbCodeMain.getId());
        dbTemplateCodeMain.setLeftButtonList(buttonMap.get(ButtonIdentEnum.LEFT_BUTTON) == null ? Lists.newArrayList() : buttonMap.get(ButtonIdentEnum.LEFT_BUTTON));
        dbTemplateCodeMain.setRightButtonList(buttonMap.get(ButtonIdentEnum.RIGHT_BUTTON) == null ? Lists.newArrayList() : buttonMap.get(ButtonIdentEnum.RIGHT_BUTTON));
        dbTemplateCodeMain.setRowButtonList(buttonMap.get(ButtonIdentEnum.ROW_BUTTON) == null ? Lists.newArrayList() : buttonMap.get(ButtonIdentEnum.ROW_BUTTON));

        return dbTemplateCodeMain;
    }

    /**
     * 查询form配置模板数据
     * @param templateCodeTable 模板数据
     */
    private void queryCodeFormTemplateData(DbTemplateCodeTableDTO templateCodeTable) {
        // 1、查询form配置信息
        final List<DbCodeFormConfigPO> codeFormConfigList = this.listDbCodeFormConfig(templateCodeTable.getId(), true);
        // 2、查询下拉table信息
        if (CollectionUtils.isEmpty(codeFormConfigList)) {
            templateCodeTable.setCodeFormConfigList(Lists.newArrayList());
            return;
        }
        final List<Long> formConfigIdList = codeFormConfigList.stream().map(DbCodeFormConfigPO :: getId).collect(Collectors.toList());
        // 查询关联表信息
        final Map<Long, List<DbTemplateCodeTableDTO>> relatedTableMap = this.queryRelatedTableData(formConfigIdList, RelatedTableIdentEnum.FORM, RelatedTableTypeEnum.SELECT_TABLE);
        // 查询RULE关联信息
        // 查询rule信息
        final Map<Long, List<DbCodeRuleConfigPO>> ruleConfigMap = this.queryRuleConfigMap(formConfigIdList, RuleIdentEnum.SAVE_FORM);
        final List<DbTemplateCodeTableDTO.DbCodeFormConfigTemplateDTO> dbCodeFormConfigTemplateList = codeFormConfigList.stream().map(item -> {
            final DbTemplateCodeTableDTO.DbCodeFormConfigTemplateDTO dbCodeFormConfigTemplate = new DbTemplateCodeTableDTO.DbCodeFormConfigTemplateDTO();
            BeanUtils.copyProperties(item, dbCodeFormConfigTemplate);
            // 获取关联表信息
            final List<DbTemplateCodeTableDTO> relatedTableList = relatedTableMap.get(dbCodeFormConfigTemplate.getId());
            if (CollectionUtils.isNotEmpty(relatedTableList)) {
                if (relatedTableList.size() > 1) {
                    throw new BaseException("系统发生未知错误，下拉表格只能设置一个");
                }
                dbCodeFormConfigTemplate.setSelectTable(relatedTableList.get(0));
            }
            // 设置验证类型
            final List<DbCodeRuleConfigPO> ruleList = ruleConfigMap.get(dbCodeFormConfigTemplate.getId());
            dbCodeFormConfigTemplate.setRuleList(ruleList == null ? Lists.newArrayList() : ruleList);
            return dbCodeFormConfigTemplate;
        }).collect(Collectors.toList());
        templateCodeTable.setCodeFormConfigList(dbCodeFormConfigTemplateList);
    }

    /**
     * 查询form配置模板数据
     * @param templateCodeTable 模板数据
     */
    private void queryCodeSearchTemplateData(DbTemplateCodeTableDTO templateCodeTable) {
        // 1、查询form配置信息
        final List<DbCodeSearchConfigPO> codeSearchConfigList = this.listDbCodeSearchConfig(templateCodeTable.getId(), true);
        // 2、查询下拉table信息
        if (CollectionUtils.isEmpty(codeSearchConfigList)) {
            templateCodeTable.setCodeSearchConfigList(Lists.newArrayList());
            return;
        }
        // 查询关联表信息
        final Map<Long, List<DbTemplateCodeTableDTO>> relatedTableMap = this.queryRelatedTableData(codeSearchConfigList.stream().map(DbCodeSearchConfigPO :: getId).collect(Collectors.toList()), RelatedTableIdentEnum.SEARCH, RelatedTableTypeEnum.SELECT_TABLE);
        final List<DbTemplateCodeTableDTO.DbCodeSearchConfigTemplateDTO> dbCodeFormConfigTemplateList = codeSearchConfigList.stream().map(item -> {
            final DbTemplateCodeTableDTO.DbCodeSearchConfigTemplateDTO dbCodeSearchConfigTemplate = new DbTemplateCodeTableDTO.DbCodeSearchConfigTemplateDTO();
            BeanUtils.copyProperties(item, dbCodeSearchConfigTemplate);
            // 获取关联表信息
            final List<DbTemplateCodeTableDTO> relatedTableList = relatedTableMap.get(dbCodeSearchConfigTemplate.getId());
            if (CollectionUtils.isNotEmpty(relatedTableList)) {
                if (relatedTableList.size() > 1) {
                    throw new BaseException("系统发生未知错误，下拉表格只能设置一个");
                }
                dbCodeSearchConfigTemplate.setSelectTable(relatedTableList.get(0));
            }
            return dbCodeSearchConfigTemplate;
        }).collect(Collectors.toList());
        templateCodeTable.setCodeSearchConfigList(dbCodeFormConfigTemplateList);
    }

    /**
     * 查询关联表格信息
     * @param mainIdList 主ID（form配置ID、search配置ID）
     * @param ident 标识
     * @param type 类型
     * @return 关联表格信息
     */
    private Map<Long, List<DbTemplateCodeTableDTO>> queryRelatedTableData(@NonNull List<Long> mainIdList, @NonNull RelatedTableIdentEnum ident, @NonNull RelatedTableTypeEnum type) {
        if (mainIdList.isEmpty()) {
            return new HashMap<>(0);
        }
        // 查询关联信息
        final List<DbCodeRelatedTablePO> dbCodeRelatedTableList = this.dbCodeRelatedTableService.list(
                new QueryWrapper<DbCodeRelatedTablePO>().lambda()
                .select(DbCodeRelatedTablePO :: getMainId, DbCodeRelatedTablePO :: getAddendumId, DbCodeRelatedTablePO :: getRelatedColumn)
                .in(DbCodeRelatedTablePO :: getMainId, mainIdList)
                .eq(DbCodeRelatedTablePO :: getIdent, ident)
                .eq(DbCodeRelatedTablePO :: getType, type)
        );
        if (dbCodeRelatedTableList.isEmpty()) {
            return new HashMap<>(0);
        }
        // 查询表信息并转为map
        final Map<Long, DbTemplateCodeTableDTO> dbTemplateCodeTableMap = dbCodeRelatedTableList.stream()
                .map(item -> {
                    final DbTemplateCodeTableDTO dbTemplateCodeTable = this.createDbTemplateCodeMain(item.getAddendumId());
                    // 验证是否是附表
                    if (!StringUtils.equals(dbTemplateCodeTable.getType(), TableTypeEnum.ADDENDUM.getType())) {
                        throw new BaseException("系统发生未知错误，关联表不是附表，tableName:" + dbTemplateCodeTable.getTableName());
                    }
                    return dbTemplateCodeTable;
                }).collect(Collectors.toMap(DbTemplateCodeTableDTO:: getId, item -> item));
        // 将关联信息转为以main_id 为key，关联表信息为value的map
        final Map<Long, List<DbCodeRelatedTablePO>> dbCodeRelatedTableMap = dbCodeRelatedTableList.stream()
                .collect(
                        Collectors.groupingBy(DbCodeRelatedTablePO :: getMainId)
                );
        final Map<Long, List<DbTemplateCodeTableDTO>> result = Maps.newHashMap();
        dbCodeRelatedTableMap.forEach((key, tableList) -> {
            final List<DbTemplateCodeTableDTO> templateCodeTableList = tableList.stream().map(item -> {
                final DbTemplateCodeTableDTO dbTemplateCodeTable = dbTemplateCodeTableMap.get(item.getAddendumId());
                if (dbTemplateCodeTable != null) {
                    dbTemplateCodeTable.setRelatedColumn(item.getRelatedColumn());
                }
                return dbTemplateCodeTable;
            }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.put(key, templateCodeTableList);
        });
        return result;
    }

    /**
     * 处理页面配置数据
     * @param dbTemplateCodeTable 表格配置
     */
    private void dealCodePageConfig(@NonNull DbTemplateCodeTableDTO dbTemplateCodeTable) {
        // 1、查询page配置
        final List<DbCodePageConfigPO> codePageConfigList = this.listDbCodePageConfig(dbTemplateCodeTable.getId());

        final List<DbCodePageConfigTemplateVO> dbCodePageConfigTemplateList = codePageConfigList.stream().map(item -> {
            final DbCodePageConfigTemplateVO template = new DbCodePageConfigTemplateVO();
            BeanUtils.copyProperties(item, template);
            return template;
        }).collect(Collectors.toList());
        // 获取主键数量
        final List<DbCodePageConfigTemplateVO> primaryKeyList = dbCodePageConfigTemplateList.stream().filter(DbCodePageConfigPO::getPrimaryKey).collect(Collectors.toList());
        if (!primaryKeyList.isEmpty()) {
            // 将第一个主键设置添加注解主键（mybatis plus无法添加多个主键）
            primaryKeyList.get(0).setIdAnnotation(true);
            // 设置有主键
            dbTemplateCodeTable.setHasId(true);
        }
        // 2、处理实体类引入列表，部分实体类属性类型对应的CLASS需要单独引入
        dbTemplateCodeTable.setModelClassImportList(
                dbCodePageConfigTemplateList.stream()
                        .map(DbCodePageConfigTemplateVO :: getJavaType)
                        .filter(item -> !StringUtils.startsWith(item, "java.lang"))
                        .collect(Collectors.toSet())
        );
        dbTemplateCodeTable.setCodePageConfigList(dbCodePageConfigTemplateList);
    }

    /**
     * 查询页面配置信息
     * @param mainId 主ID
     * @return DbCodePageConfigPO
     */
    private List<DbCodePageConfigPO> listDbCodePageConfig(Long mainId) {
        return this.dbCodePageConfigService.list(
                new QueryWrapper<DbCodePageConfigPO>().lambda()
                .eq(DbCodePageConfigPO :: getMainId, mainId)
                .orderByAsc(DbCodePageConfigPO :: getSeq)
        );
    }

    /**
     * 查询表单配置
     * @param mainId 主ID
     * @return DbCodeFormConfigPO
     */
    private List<DbCodeFormConfigPO> listDbCodeFormConfig(Long mainId, boolean visible) {
        final LambdaQueryWrapper<DbCodeFormConfigPO> queryWrapper = new QueryWrapper<DbCodeFormConfigPO>().lambda()
                // 只查询显示的字段
                .eq(DbCodeFormConfigPO :: getMainId, mainId)
                .orderByAsc(DbCodeFormConfigPO :: getSeq);
        if (visible) {
            queryWrapper.eq(DbCodeFormConfigPO :: getVisible, true);
        }
        return this.dbCodeFormConfigService.list(queryWrapper);
    }

    /**
     * 查询search配置DTO对象
     * @param mainId 主ID
     * @param visible 是否查询VISIBLE为false的
     * @return DbCodeSearchConfigDTO
     */
    private List<DbCodeSearchConfigDTO> listDbCodeSearchConfigDto(Long mainId, boolean visible) {
        final List<DbCodeSearchConfigPO> dbCodeSearchConfigList = this.listDbCodeSearchConfig(mainId, visible);
        if (CollectionUtils.isEmpty(dbCodeSearchConfigList)) {
            return Lists.newArrayList();
        }
        // 获取ID集合
        final List<Long> searchConfigIdList = dbCodeSearchConfigList.stream().map(DbCodeSearchConfigPO :: getId).collect(Collectors.toList());
        // 查询关联信息并以主ID、类型分组
        final Map<Long, Map<RelatedTableTypeEnum, List<DbCodeRelatedTablePO>>> relatedTableTypeListMap = this.queryRelatedTableData(searchConfigIdList, RelatedTableIdentEnum.SEARCH);
        // 设置关联表信息
        return dbCodeSearchConfigList.stream().map(item -> {
            final DbCodeSearchConfigDTO dto = new DbCodeSearchConfigDTO();
            BeanUtils.copyProperties(item, dto);
            // 设置下拉表信息
            if (relatedTableTypeListMap.containsKey(item.getId())) {
                dto.setSelectTableList(relatedTableTypeListMap.get(item.getId()).get(RelatedTableTypeEnum.SELECT_TABLE));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 查询表格配置DTO对象
     * @param mainId 主ID
     * @param visible 是否查询VISIBLE为false的
     * @return DbCodeFormConfigDTO
     */
    private List<DbCodeFormConfigDTO> listDbCodeFormConfigDto(Long mainId, boolean visible) {
        final List<DbCodeFormConfigPO> dbCodeFormConfigList = this.listDbCodeFormConfig(mainId, visible);
        if (CollectionUtils.isEmpty(dbCodeFormConfigList)) {
            return Lists.newArrayList();
        }
        // 查询所有关联表信息（下拉table）
        final List<Long> formConfigIdList = dbCodeFormConfigList.stream().map(DbCodeFormConfigPO :: getId).collect(Collectors.toList());
        // 查询关联信息并以主ID、类型分组
        final Map<Long, Map<RelatedTableTypeEnum, List<DbCodeRelatedTablePO>>> relatedTableTypeListMap = this.queryRelatedTableData(formConfigIdList, RelatedTableIdentEnum.FORM);

        // 查询rule信息
        final Map<Long, List<DbCodeRuleConfigPO>> ruleConfigMap = this.queryRuleConfigMap(formConfigIdList, RuleIdentEnum.SAVE_FORM);

        // 设置关联表信息
        return dbCodeFormConfigList.stream().map(item -> {
            final DbCodeFormConfigDTO dto = new DbCodeFormConfigDTO();
            BeanUtils.copyProperties(item, dto);
            // 设置下拉表信息
            if (relatedTableTypeListMap.containsKey(item.getId())) {
                dto.setSelectTableList(relatedTableTypeListMap.get(item.getId()).get(RelatedTableTypeEnum.SELECT_TABLE));
            }
            if (ruleConfigMap.containsKey(item.getId())) {
                dto.setRuleList(ruleConfigMap.get(item.getId()));
            }
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据关联ID查询表单rule配置信息
     * @param relationIdList  关联ID集合
     * @return RULE配置信息
     */
    private Map<Long, List<DbCodeRuleConfigPO>> queryRuleConfigMap(List<Long> relationIdList, RuleIdentEnum ident) {
        if (CollectionUtils.isEmpty(relationIdList)) {
            return Maps.newHashMap();
        }
        return this.dbCodeRuleConfigService.list(
                new QueryWrapper<DbCodeRuleConfigPO>().lambda()
                        .in(DbCodeRuleConfigPO :: getRelationId, relationIdList)
                        .eq(DbCodeRuleConfigPO :: getIdent, ident)
                .orderByAsc(DbCodeRuleConfigPO :: getSeq)
        ).stream().collect(Collectors.groupingBy(DbCodeRuleConfigPO :: getRelationId));
    }

    /**
     * 查询关联表信息
     * @param mainIdList 主表ID集合
     * @param ident 标识
     * @return 关联表信息
     */
    private Map<Long, Map<RelatedTableTypeEnum, List<DbCodeRelatedTablePO>>> queryRelatedTableData(@NonNull List<Long> mainIdList, @NonNull RelatedTableIdentEnum ident) {
        if (mainIdList.isEmpty()) {
            return Maps.newHashMap();
        }
        return this.dbCodeRelatedTableService.list(
                new QueryWrapper<DbCodeRelatedTablePO>().lambda()
                        .in(DbCodeRelatedTablePO :: getMainId, mainIdList)
                        .eq(DbCodeRelatedTablePO :: getIdent, ident)
        ).stream().collect(Collectors.groupingBy(DbCodeRelatedTablePO :: getMainId, Collectors.groupingBy(DbCodeRelatedTablePO :: getType)));
    }

    /**
     * 查询搜索配置信息
     * @param mainId 主配置ID
     * @return 搜索配置信息
     */
    private List<DbCodeSearchConfigPO> listDbCodeSearchConfig(@NonNull Long mainId, boolean visible) {
        final LambdaQueryWrapper<DbCodeSearchConfigPO> queryWrapper = new QueryWrapper<DbCodeSearchConfigPO>().lambda()
                .eq(DbCodeSearchConfigPO :: getMainId, mainId)
                .orderByAsc(DbCodeSearchConfigPO :: getSeq);
        if (visible) {
            queryWrapper.eq(DbCodeSearchConfigPO :: getVisible, true);
        }
        return this.dbCodeSearchConfigService.list(queryWrapper);
    }

    /**
     * 重写删除函数
     * @param idList ID列表
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除主表
        this.baseMapper.deleteBatchIds(idList);
        // 删除附表关联
        this.dbCodeRelatedTableService.remove(
                new QueryWrapper<DbCodeRelatedTablePO>().lambda()
                .in(DbCodeRelatedTablePO :: getMainId, idList)
                .eq(DbCodeRelatedTablePO :: getIdent, RelatedTableIdentEnum.MAIN_TABLE)
        );
        // 删除表格配置
        this.dbCodePageConfigService.remove(
                new QueryWrapper<DbCodePageConfigPO>().lambda()
                        .in(DbCodePageConfigPO :: getMainId, idList)
        );
        // 删除表单配置
        this.removeFormConfigByMainId(idList);
        // 删除搜索配置
        this.removeSearchConfigByMainId(idList);
        // 删除主表附表关联关系

        this.dbCodeRelatedTableService.remove(
                new QueryWrapper<DbCodeRelatedTablePO>().lambda()
                .in(DbCodeRelatedTablePO :: getMainId, idList)
                .eq(DbCodeRelatedTablePO :: getIdent, RelatedTableIdentEnum.MAIN_TABLE)
        );
        // 删除按钮配置信息
        this.dbCodeButtonConfigService.remove(
                new QueryWrapper<DbCodeButtonConfigPO>().lambda()
                .in(DbCodeButtonConfigPO :: getRelatedId, idList)
                // TODO:标识位扩展
        );
        return true;
    }

    /**
     * 删除表单配置
     * @param mainIdList 主配置ID集合
     */
    private void removeFormConfigByMainId(@NonNull Collection<? extends Serializable> mainIdList) {
        // 删除关联table信息
        this.dbCodeRelatedTableService.deleteByMainIdList(
                RelatedTableDeleteByMainConfigQuery.builder()
                .tableName("db_code_form_config")
                .mainIdList(mainIdList)
                .build()
        );
        // 删除rule配置信息
        this.dbCodeRuleConfigService.deleteByMainIdList(
                RelatedTableDeleteByMainConfigQuery.builder()
                        .tableName("db_code_form_config")
                        .mainIdList(mainIdList)
                        .build()
        );
        this.dbCodeFormConfigService.remove(
                new QueryWrapper<DbCodeFormConfigPO>().lambda()
                        .in(DbCodeFormConfigPO :: getMainId, mainIdList)
        );
    }

    /**
     * 删除搜索配置
     * @param mainIdList 主配置ID集合
     */
    private void removeSearchConfigByMainId(@NonNull Collection<? extends Serializable> mainIdList) {
        // 删除关联table信息
        this.dbCodeRelatedTableService.deleteByMainIdList(
                RelatedTableDeleteByMainConfigQuery.builder()
                        .tableName("db_code_search_config")
                        .mainIdList(mainIdList)
                        .build()
        );
        this.dbCodeSearchConfigService.remove(
                new QueryWrapper<DbCodeSearchConfigPO>().lambda()
                        .in(DbCodeSearchConfigPO :: getMainId, mainIdList)
        );
    }
}

package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableList;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.model.CreateUpdateUserSetter;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysFunctionMapper;
import com.smart.system.model.SysFunctionPO;
import com.smart.system.pojo.vo.SysFunctionListVO;
import com.smart.system.pojo.vo.function.SysFunctionVO;
import com.smart.system.service.SysFunctionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/27 12:16 下午
 */
@Service
public class SysFunctionServiceImpl extends BaseServiceImpl<SysFunctionMapper, SysFunctionPO> implements SysFunctionService {

    private UserSetterService userSetterService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        super.removeByIds(idList);
        // 删除下级
        return this.remove(
                new QueryWrapper<SysFunctionPO>().lambda()
                .in(SysFunctionPO :: getParentId, idList)
        );
    }

    @Override
    public List<? extends SysFunctionPO> list(@NonNull QueryWrapper<SysFunctionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysFunctionPO> functionList = super.list(queryWrapper, parameter, paging);
        List<SysFunctionListVO> functionVoList = functionList.stream()
                .map(item -> {
                    SysFunctionListVO vo = new SysFunctionListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).collect(Collectors.toList());
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.queryCreateUpdateUser(functionVoList);
        }
        return functionVoList;
    }

    @Override
    public SysFunctionVO getUserAndParentById(Long functionId) {
        SysFunctionPO function = this.getById(functionId);
        if (function == null) {
            return null;
        }
        SysFunctionVO vo = new SysFunctionVO();
        vo.setFunction(function);
        List<SysFunctionVO> voList = ImmutableList.of(vo);
        this.queryCreateUpdateUser(voList);
        this.queryParent(voList);
        return voList.get(0);
    }

    /**
     * 查询创建人更新人信息
     * @param functionVoList voList
     */
    protected void queryCreateUpdateUser(List<? extends CreateUpdateUserSetter> functionVoList) {
        if (CollectionUtils.isEmpty(functionVoList)) {
            return;
        }
        this.userSetterService.setCreateUpdateUser(functionVoList);
    }

    /**
     * 查询上级信息
     * @param functionVoList voList
     */
    protected void queryParent(List<SysFunctionVO> functionVoList) {
        if (CollectionUtils.isEmpty(functionVoList)) {
            return;
        }
        // 查询上级ID和本级ID
        Set<Long> parentIds = functionVoList.stream().map(item -> item.getFunction().getParentId())
                .filter(item -> item != 0)
                .collect(Collectors.toSet());

        Map<Long, SysFunctionPO> parentMap = this.listByIds(parentIds).stream()
                .collect(Collectors.toMap(SysFunctionPO::getFunctionId, item -> item));

        for (SysFunctionVO vo : functionVoList) {
            Long functionId = vo.getFunction().getParentId();
            if (parentMap.containsKey(functionId)) {
                vo.setParent(parentMap.get(functionId));
            }
        }
    }

    @Autowired
    public void setUserSetterService(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }
}

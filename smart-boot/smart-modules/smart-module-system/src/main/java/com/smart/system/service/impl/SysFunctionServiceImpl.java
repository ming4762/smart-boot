package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysFunctionMapper;
import com.smart.system.model.SysFunctionPO;
import com.smart.system.pojo.vo.SysFunctionListVO;
import com.smart.system.service.SysFunctionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author jackson
 * 2020/1/27 12:16 下午
 */
@Service
public class SysFunctionServiceImpl extends BaseServiceImpl<SysFunctionMapper, SysFunctionPO> implements SysFunctionService {

    private UserSetterService userSetterService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
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
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(functionVoList);
        }
        return functionVoList;
    }

    @Autowired
    public void setUserSetterService(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }
}

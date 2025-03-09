package com.smart.module.system.service.impl;

import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.commons.core.utils.TreeUtils;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.SysDataPermissionMapper;
import com.smart.module.system.model.SysDataPermissionPO;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.pojo.vo.datapermission.SysDataPermissionListVO;
import com.smart.module.system.service.SysDataPermissionService;
import com.smart.module.system.service.SysFunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* sys_data_permission - 数据权限表 Service实现类
* @author SmartCodeGenerator
* 2025年3月7日 19:29:00
*/
@Service
@RequiredArgsConstructor
public class SysDataPermissionServiceImpl extends BaseServiceImpl<SysDataPermissionMapper, SysDataPermissionPO> implements SysDataPermissionService {

    private final SysFunctionService sysFunctionService;

    /**
     * 查询所有数据权限列表
     * 1、查询所有功能
     * 2、构建功能书，并
     * @return 数据权限列表
     */
    @Override
    public List<Tree<SysDataPermissionListVO>> listAllWithFunction() {
        // 查询所有数据权限
        List<SysDataPermissionPO> dataPermissionList = this.lambdaQuery()
                .select(SysDataPermissionPO::getId, SysDataPermissionPO::getFunctionId, SysDataPermissionPO::getPermissionCode, SysDataPermissionPO::getPermissionName)
                .eq(SysDataPermissionPO::getUseYn, Boolean.TRUE)
                .list();
        if (CollectionUtils.isEmpty(dataPermissionList)) {
            return Collections.emptyList();
        }
        // 查询所有功能，并构建为树节点
        List<Tree<SysDataPermissionListVO>> voList = this.sysFunctionService.lambdaQuery()
                .select(SysFunctionPO::getFunctionId, SysFunctionPO::getParentId, SysFunctionPO::getFunctionName)
                .eq(SysFunctionPO::getUseYn, Boolean.TRUE)
                .eq(SysFunctionPO::getDeleteYn, Boolean.FALSE)
                .eq(SysFunctionPO::getIsMenu, Boolean.TRUE)
                .orderByAsc(SysFunctionPO::getSeq)
                .list().stream()
                .map(item -> {
                    SysDataPermissionListVO vo = new SysDataPermissionListVO();
                    BeanUtils.copyProperties(item, vo);
                    vo.setWithDataPermission(false);
                    return Tree.<SysDataPermissionListVO>builder()
                            .id(vo.getFunctionId())
                            .text(vo.getFunctionName())
                            .parentId(vo.getParentId())
                            .data(vo)
                            .build();
                }).toList();
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }

        Set<Long> permissionFunctionIds = dataPermissionList.stream()
                .map(SysDataPermissionPO::getFunctionId)
                .collect(Collectors.toSet());

        // 构建功能树
        List<Tree<SysDataPermissionListVO>> functionTree = TreeUtils.buildList(voList, 0L);
        // 设置节点是否配置数据权限
        this.setFunctionTreeByPermission(functionTree, permissionFunctionIds);
        // 排除第一层未设置数据权限
        List<Tree<SysDataPermissionListVO>> filterTreeList = functionTree.stream()
                .filter(item -> Boolean.TRUE.equals(item.getData().getWithDataPermission()))
                .toList();
        // 排除未设置数据权限节点
        this.filterFunctionTreeByPermission(filterTreeList);
        return filterTreeList;
    }

    /**
     * 过滤树结构，设置是否有数据权限
     * @param functionTree 功能树
     * @param permissionFunctionIds 权限功能ID
     */
    private void setFunctionTreeByPermission(List<Tree<SysDataPermissionListVO>> functionTree, Set<Long> permissionFunctionIds) {
        functionTree.forEach(treeNode -> {
            if (permissionFunctionIds.contains((Long) treeNode.getId())) {
                this.childToParentSetDataPermission(treeNode);
            }
            if (Boolean.TRUE.equals(treeNode.getHasChildren())) {
                // 使用递归循环设置
                this.setFunctionTreeByPermission(treeNode.getChildren(), permissionFunctionIds);
            }
        });
    }

    /**
     * 从子节点开始，向上设置数据权限
     * @param treeNode 树节点
     */
    private void childToParentSetDataPermission(Tree<SysDataPermissionListVO> treeNode) {
        if (Boolean.TRUE.equals(treeNode.getData().getWithDataPermission())) {
            return;
        }
        treeNode.getData().setWithDataPermission(true);
        if (treeNode.getParent() != null) {
            this.childToParentSetDataPermission(treeNode.getParent());
        }
    }

    /**
     * 过滤树结构，只保留有权限的功能
     * @param functionTree 功能树
     */
    private void filterFunctionTreeByPermission(List<Tree<SysDataPermissionListVO>> functionTree) {
        Iterator<Tree<SysDataPermissionListVO>> iterator = functionTree.iterator();
        while (iterator.hasNext()) {
            Tree<SysDataPermissionListVO> treeNode = iterator.next();
            if (!Boolean.TRUE.equals(treeNode.getData().getWithDataPermission())) {
                iterator.remove();
                if (CollectionUtils.isEmpty(treeNode.getParent().getChildren())) {
                    treeNode.getParent().setHasChildren(false);
                    treeNode.getParent().setChildren(null);
                }
                continue;
            }
            if (Boolean.TRUE.equals(treeNode.getHasChildren())) {
                this.filterFunctionTreeByPermission(treeNode.getChildren());
            }
        }
    }

}
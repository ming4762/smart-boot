package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.commons.core.utils.TreeUtils;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.SysDataPermissionMapper;
import com.smart.module.system.model.SysDataPermissionPO;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.model.SysRoleDataPermissionPO;
import com.smart.module.system.pojo.vo.datapermission.SysDataPermissionListVO;
import com.smart.module.system.service.SysDataPermissionService;
import com.smart.module.system.service.SysFunctionService;
import com.smart.module.system.service.SysRoleDataPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    private final SysRoleDataPermissionService sysRoleDataPermissionService;

    @Override
    public List<Tree<SysDataPermissionListVO>> listAllWithFunction() {
        // 1. 查询所有数据权限，并以功能ID分组
        Map<Long, List<SysDataPermissionPO>> dataPermissionFunctionMap = this.lambdaQuery()
                .select(SysDataPermissionPO::getId, SysDataPermissionPO::getFunctionId, SysDataPermissionPO::getPermissionCode, SysDataPermissionPO::getPermissionName, SysDataPermissionPO::getScope)
                .eq(SysDataPermissionPO::getUseYn, Boolean.TRUE)
                .list().stream()
                .collect(Collectors.groupingBy(SysDataPermissionPO::getFunctionId));
        if (CollectionUtils.isEmpty(dataPermissionFunctionMap)) {
            return Collections.emptyList();
        }

        // 2. 查询所有功能，并构建初步的树节点
        List<Tree<SysDataPermissionListVO>> voList = this.sysFunctionService.lambdaQuery()
                .select(SysFunctionPO::getFunctionId, SysFunctionPO::getParentId, SysFunctionPO::getFunctionName)
                .eq(SysFunctionPO::getUseYn, Boolean.TRUE)
                .eq(SysFunctionPO::getDeleteYn, Boolean.FALSE)
                .eq(SysFunctionPO::getIsMenu, Boolean.TRUE)
                .orderByAsc(SysFunctionPO::getSeq)
                .list().stream()
                .map(item -> {
                    SysDataPermissionListVO vo = SysDataPermissionListVO.builder()
                            .dataId(item.getFunctionId())
                            .parentId(item.getParentId())
                            .name(item.getFunctionName())
                            .isDataPermission(false)
                            // 设置功能节点对应的数据权限列表（可能为 null）
                            .dataPermissionList(dataPermissionFunctionMap.get(item.getFunctionId()))
                            .build();
                    return dataPermissionVoToTree(vo);
                }).toList();

        // 3. 构建功能树（父子关系）
        List<Tree<SysDataPermissionListVO>> functionTree = TreeUtils.buildList(voList, 0L);

        // 4. 使用递归剪枝，去除无数据权限的分支，同时将数据权限挂载为子节点
        return functionTree.stream()
                .map(this::pruneDataPermissionTree)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * 递归剪枝算法：
     *  - 递归处理子节点，只保留返回非 null 的子节点
     *  - 如果当前节点自身绑定了数据权限，则转换并挂载为子节点
     *  - 如果当前节点有数据权限或其任一子节点有效，则返回当前节点，否则返回 null
     */
    private Tree<SysDataPermissionListVO> pruneDataPermissionTree(Tree<SysDataPermissionListVO> node) {
        if (node == null) {
            return null;
        }
        // 递归处理所有子节点
        List<Tree<SysDataPermissionListVO>> prunedChildren = new ArrayList<>();
        if (node.getChildren() != null) {
            for (Tree<SysDataPermissionListVO> child : node.getChildren()) {
                Tree<SysDataPermissionListVO> prunedChild = pruneDataPermissionTree(child);
                if (prunedChild != null) {
                    prunedChildren.add(prunedChild);
                }
            }
        }
        // 检查当前节点是否绑定了数据权限
        boolean hasPermission = node.getData().getDataPermissionList() != null
                && !node.getData().getDataPermissionList().isEmpty();
        // 如果当前节点有数据权限，则将数据权限转换为树节点，并挂载到当前节点下
        if (hasPermission) {
            List<Tree<SysDataPermissionListVO>> permissionNodes = node.getData().getDataPermissionList().stream()
                    .map(dp -> {
                        SysDataPermissionListVO vo = SysDataPermissionListVO.builder()
                                .dataId(dp.getId())
                                .parentId(dp.getFunctionId())
                                .name(dp.getPermissionName())
                                .dataPermissionScope(dp.getScope().getRemark())
                                .isDataPermission(true)
                                .build();
                        return dataPermissionVoToTree(vo);
                    }).toList();
            prunedChildren.addAll(permissionNodes);
        }
        // 如果当前节点自身有权限或其子节点中存在有效节点，则保留当前节点
        if (hasPermission || !prunedChildren.isEmpty()) {
            node.setChildren(prunedChildren);
            node.getData().setWithDataPermission(true);
            return node;
        }
        return null;
    }

    /**
     * 将 VO 转换为树节点
     */
    private Tree<SysDataPermissionListVO> dataPermissionVoToTree(SysDataPermissionListVO vo) {
        return Tree.<SysDataPermissionListVO>builder()
                .id(vo.getDataId())
                .text(vo.getName())
                .parentId(vo.getParentId())
                .data(vo)
                .build();
    }

    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     *
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除角色数据权限关联
        this.sysRoleDataPermissionService.remove(
                Wrappers.lambdaQuery(SysRoleDataPermissionPO.class)
                        .in(SysRoleDataPermissionPO::getDataPermissionId, idList)
        );
        return super.removeByIds(idList);
    }
}
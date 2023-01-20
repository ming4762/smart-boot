package com.smart.db.generator.service;

import com.smart.crud.service.BaseService;
import com.smart.db.generator.model.DbCodeMainPO;
import com.smart.db.generator.pojo.dto.DbCodeMainSaveParameter;
import com.smart.db.generator.pojo.dto.DbCreateCodeDTO;
import com.smart.db.generator.pojo.vo.DbCodeVO;
import com.smart.db.generator.pojo.vo.DbMainConfigVO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:37
 * @since 1.0
 */
public interface DbCodeMainService extends BaseService<DbCodeMainPO> {

    /**
     * 执行保存操作
     * @param model 实体
     * @return 是否保存成功
     */
    Long saveUpdate(DbCodeMainSaveParameter model);

    /**
     * 生成代码
     * @param parameter 参数
     * @return 代码
     */
    List<DbCodeVO> createCode(@NonNull DbCreateCodeDTO parameter);

    /**
     * 通过ID获取配置信息
     * @param id 配置ID
     * @return 配置信息
     */
    DbMainConfigVO getConfigById(@NonNull Long id);
}

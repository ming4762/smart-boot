package com.smart.module.code.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.code.model.DbCodeMainPO;
import com.smart.module.code.pojo.dto.DbCodeMainSaveParameter;
import com.smart.module.code.pojo.dto.DbCreateCodeDTO;
import com.smart.module.code.pojo.dto.DbGenerateMapperBySqlParameter;
import com.smart.module.code.pojo.vo.DbCodeVO;
import com.smart.module.code.pojo.vo.DbMainConfigVO;
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

    /**
     * 通过SQL生成Mapper
     * @param parameter 参数
     * @return 代码
     */
    List<DbCodeVO> generateMapperBySql(DbGenerateMapperBySqlParameter parameter);
}

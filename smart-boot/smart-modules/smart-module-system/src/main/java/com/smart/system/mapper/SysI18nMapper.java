package com.smart.system.mapper;

import com.smart.crud.mapper.CrudBaseMapper;
import com.smart.system.model.SysI18nPO;
import com.smart.system.pojo.dbo.I18nCodeValueBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Locale;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
public interface SysI18nMapper extends CrudBaseMapper<SysI18nPO> {

    /**
     * 通过语言查询I18N信息
     * @param locale  语言
     * @return 国际化信息
     */
    List<I18nCodeValueBO> listI18nByLocale(@Param("locale")String locale);

    /**
     * 查询所有语言
     * @return 语言列表
     */
    List<Locale> listLocale();
}

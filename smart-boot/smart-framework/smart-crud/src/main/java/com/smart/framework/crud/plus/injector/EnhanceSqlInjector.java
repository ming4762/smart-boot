package com.smart.framework.crud.plus.injector;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.smart.framework.crud.plus.injector.methods.SmartDelete;
import com.smart.framework.crud.plus.injector.methods.SmartDeleteBatchByIds;
import com.smart.framework.crud.plus.injector.methods.SmartDeleteById;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author shizhongming
 * 2023/10/31 11:19
 * @since 3.0.0
 */
@Slf4j
public class EnhanceSqlInjector extends AbstractSqlInjector {
    /**
     * <p>
     * 获取 注入的方法
     * </p>
     *
     * @param mapperClass 当前mapper
     * @param tableInfo tableInfo
     * @return 注入的方法集合
     * @since 3.1.2 add  mapperClass
     */
    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        GlobalConfig.DbConfig dbConfig = GlobalConfigUtils.getDbConfig(configuration);
        Stream.Builder<AbstractMethod> builder = Stream.<AbstractMethod>builder()
                .add(new Insert(dbConfig.isInsertIgnoreAutoIncrementColumn()))
                .add(new SmartDelete())
                .add(new Update())
                .add(new SelectCount())
                .add(new SelectMaps())
                .add(new SelectObjs())
                .add(new SelectList());
        if (tableInfo.havePK()) {
            builder.add(new SmartDeleteById())
                    .add(new SmartDeleteBatchByIds())
                    .add(new UpdateById())
                    .add(new SelectById())
                    .add(new SelectByIds());
        } else {
            logger.warn(String.format("%s ,Not found @TableId annotation, Cannot use Mybatis-Plus 'xxById' Method.",
                    tableInfo.getEntityType()));
        }
        return builder.build().toList();
    }
}

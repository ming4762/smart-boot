package com.smart.kettle.core.repository.pool;

import com.smart.kettle.core.KettleActuator;
import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * kettle数据库资源库提供器
 * @author ShiZhongMing
 * 2021/7/15 11:20
 * @since 1.0
 */
@Slf4j
public class KettleDatabaseRepositoryProvider implements InitializingBean, DisposableBean {

    private final GenericKeyedObjectPool<KettleDatabaseRepositoryProperties, KettleDatabaseRepository> pool = new GenericKeyedObjectPool<>(new KettleDatabaseRepositoryFactory());

    /**
     * 获取连接池对象
     * @param properties 连接参数
     * @return KettleDatabaseRepository
     */
    @SneakyThrows
    public KettleDatabaseRepository getRepository(KettleDatabaseRepositoryProperties properties) {
        KettleActuator.initKettle();
        return pool.borrowObject(properties);
    }

    /**
     * 归还连接池对象
     * 资源库使用完毕必须归还
     * @param properties 参数
     * @param repository KettleDatabaseRepository
     */
    @SneakyThrows
    public void returnRepository(KettleDatabaseRepositoryProperties properties, KettleDatabaseRepository repository) {
        pool.returnObject(properties, repository);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("============= kettle资源库连接池完成初始化 =============");
    }

    @Override
    public void destroy() {
        this.pool.close();
        log.info("============= kettle资源库连接池已销毁 =============");
    }
}

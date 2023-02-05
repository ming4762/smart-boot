package com.smart.crud.mybatis.plus;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.smart.commons.core.utils.IdGenerator;

/**
 * mybatis plus ID生成器，缩短2位，防止js number超长
 * @author zhongming4762
 * 2023/2/4 20:54
 */
public class ShortenIdGenerator implements IdentifierGenerator {
    /**
     * 生成Id
     *
     * @param entity 实体
     * @return id
     */
    @Override
    public Number nextId(Object entity) {
        return IdGenerator.nextId();
    }
}

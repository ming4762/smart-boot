package com.smart.framework.commons.core.utils;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BeanUtilsTest {

    // 测试数据模型
    @Data
    static class SourceBean {
        private String name;
        private Integer age;
        private NestedBean nested;
    }

    @Data
    static class TargetBean {
        private String name;
        private Integer age;
    }

    @Data
    static class NestedBean {
        private String value;
    }

    // 测试copyProperties方法
    @Test
    void copyProperties_shouldMapCollection() {
        // 准备测试数据
        SourceBean source = new SourceBean();
        source.setName("Test");
        source.setAge(20);

        // 执行测试
        List<TargetBean> result = BeanUtils.copyProperties(Collections.singletonList(source), TargetBean.class);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("Test", result.getFirst().getName());
        assertEquals(20, result.getFirst().getAge());
    }

    @Test
    void copyProperties_shouldReturnEmptyListWhenSourceEmpty() {
        List<TargetBean> result = BeanUtils.copyProperties(Collections.emptyList(), TargetBean.class);
        assertEquals(0, result.size());
    }

    // 测试beanToMap方法
    @Test
    void beanToMap_shouldConvertBeanToMap() {
        SourceBean source = new SourceBean();
        source.setName("Test");
        source.setAge(20);

        Map<String, Object> result = BeanUtils.beanToMap(source);

        assertEquals("Test", result.get("name"));
        assertEquals(20, result.get("age"));
    }

    @Test
    void beanToMap_shouldThrowWhenSourceNull() {
        Executable action = () -> BeanUtils.beanToMap(null);
        assertThrows(NullPointerException.class, action);
    }

    // 测试beanToMapDeep方法
    @Test
    void beanToMapDeep_shouldRecursivelyConvertNestedBean() {
        NestedBean nested = new NestedBean();
        nested.setValue("nested");

        SourceBean source = new SourceBean();
        source.setName("Test");
        source.setNested(nested);

        Map<String, Object> result = BeanUtils.deepBeanToMap(source);

        assertNull(result.get("age"));
        assertEquals("Test", result.get("name"));
        assertEquals("nested", ((Map<String, Object>) result.get("nested")).get("value"));
    }

    @Test
    void beanToMapDeep_shouldHandleMapParameter() {
        // 创建包含嵌套Map的测试数据
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("innerKey", "innerValue");

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("name", "TestMap");
        inputMap.put("nested", nestedMap);
        inputMap.put("age", 30);

        // 执行测试
        Map<String, Object> result = BeanUtils.deepBeanToMap(inputMap);

        // 验证结果
        assertEquals("TestMap", result.get("name"));
        assertEquals(30, result.get("age"));
                          
        // 验证嵌套Map是否被正确转换
        Map<?, ?> convertedNested = (Map<?, ?>) result.get("nested");
        assertEquals("innerValue", convertedNested.get("innerKey"));
    }
}
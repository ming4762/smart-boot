package com.smart.framework.commons.core.utils;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

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
        private List<String> tags;
        // 可以是 MultipartFile 模拟
        private Object file;
    }

    @Data
    static class ComplexBean {
        private String name;
        private int age;
        private NestedBean nested;
        private Map<String, Object> extra;
        private List<NestedBean> nestedList;
        // 可以是 MultipartFile 模拟
        private Object file;
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

    @Test
    void flattenBean_shouldFlattenSimpleNestedObject() {
        NestedBean nested = new NestedBean();
        nested.setValue("nestedVal");
        ComplexBean bean = new ComplexBean();
        bean.setName("Alice");
        bean.setAge(25);
        bean.setNested(nested);

        Map<String, Object> flat = BeanUtils.flattenBean(bean);

        assertEquals("Alice", flat.get("name"));
        assertEquals(25, flat.get("age"));
        assertEquals("nestedVal", flat.get("nested.value"));
    }

    @Test
    void flattenBean_shouldFlattenListAndMap() {
        NestedBean nested1 = new NestedBean();
        nested1.setValue("n1");
        NestedBean nested2 = new NestedBean();
        nested2.setValue("n2");

        Map<String, Object> extra = new HashMap<>();
        extra.put("key1", "value1");
        extra.put("key2", Arrays.asList("a", "b"));

        ComplexBean bean = new ComplexBean();
        bean.setNestedList(Arrays.asList(nested1, nested2));
        bean.setExtra(extra);

        Map<String, Object> flat = BeanUtils.flattenBean(bean);

        // List 索引展开
        assertEquals("n1", flat.get("nestedList[0].value"));
        assertEquals("n2", flat.get("nestedList[1].value"));

        // Map 展开
        assertEquals("value1", flat.get("extra.key1"));
        assertEquals("a", flat.get("extra.key2[0]"));
        assertEquals("b", flat.get("extra.key2[1]"));
    }

    @Test
    void flattenBean_shouldHandleMultipartFile() {
        // 使用 Spring MockMultipartFile 模拟文件
        MockMultipartFile file1 = new MockMultipartFile("file1", "test1.txt", "text/plain", "content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.txt", "text/plain", "content".getBytes());
        MockMultipartFile file3 = new MockMultipartFile("file3", "test3.txt", "text/plain", "content".getBytes());
        ComplexBean bean = new ComplexBean();
        bean.setFile(file1);

        NestedBean nested1 = new NestedBean();
        nested1.setValue("n1");
        nested1.setFile(file2);
        NestedBean nested2 = new NestedBean();
        nested2.setValue("n2");
        nested2.setFile(file3);
        bean.setNestedList(List.of(nested1, nested2));

        Map<String, Object> flat = BeanUtils.flattenBean(bean);

        assertTrue(flat.containsKey("file"));
        Object value = flat.get("file");
        // 保留原对象引用
        assertSame(file1, value);

        assertSame(file2, flat.get("nestedList[0].file"));
        assertSame(file3, flat.get("nestedList[1].file"));
    }

    @Test
    void flattenBean_shouldHandleComplexNestedStructure() {
        NestedBean nested = new NestedBean();
        nested.setValue("val");
        nested.setTags(Arrays.asList("tag1", "tag2"));

        Map<String, Object> extra = new HashMap<>();
        extra.put("innerMap", Map.of("k", "v"));

        ComplexBean bean = new ComplexBean();
        bean.setName("Bob");
        bean.setNested(nested);
        bean.setExtra(extra);

        Map<String, Object> flat = BeanUtils.flattenBean(bean);

        assertEquals("Bob", flat.get("name"));
        assertEquals("val", flat.get("nested.value"));
        assertEquals("tag1", flat.get("nested.tags[0]"));
        assertEquals("tag2", flat.get("nested.tags[1]"));
        assertEquals("v", flat.get("extra.innerMap.k"));
    }
}
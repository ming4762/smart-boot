package com.smart.framework.commons.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XmlUtilsTest {

    @Test
    void testToXmlWithSimpleObject() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "test");
        map.put("value", "123");

        String xml = XmlUtils.toXml(map);
        assertNotNull(xml);
        assertTrue(xml.contains("<name>test</name>"));
        assertTrue(xml.contains("<value>123</value>"));
    }

    @Test
    void testParseWithSimpleXml() {
        String xml = "<Person><name>John</name><age>30</age></Person>";
        Map<String, String> result = XmlUtils.parse(xml, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("John", result.get("name"));
        assertEquals("30", result.get("age"));
    }

    @Test
    void testParseWithClass() {
        String xml = "<Person><name>Alice</name><age>25</age></Person>";
        Map<?, ?> result = XmlUtils.parse(xml, Map.class);

        assertNotNull(result);
        assertEquals("Alice", result.get("name"));
        assertEquals("25", result.get("age"));
    }

    @Test
    void testParseWithInvalidXml() {
        String invalidXml = "<Person><name>Invalid</name>";
        assertThrows(JsonProcessingException.class, () -> XmlUtils.parse(invalidXml));
    }

    @Test
    void testParseWithEmptyXml() {
        assertThrows(JsonProcessingException.class, () -> XmlUtils.parse(""));
    }

    @Test
    void testParseWithTypeReference() {
        String xml = "<Person><name>Bob</name><age>40</age></Person>";
        Map<String, String> result = XmlUtils.parse(xml, new TypeReference<>() {
        });

        assertNotNull(result);
        assertEquals("Bob", result.get("name"));
        assertEquals("40", result.get("age"));
    }
}
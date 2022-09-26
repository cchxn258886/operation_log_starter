package com.chen.operationlogstarter.utils;

import com.chen.operationlogstarter.aspect.CustomObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @Author chenl
 * @Date 2022/9/16 4:06 下午
 */
public class JsonUtil {

    private JsonUtil() {

    }

    private static class JacksonHolder {
        private static ObjectMapper INSTANCE = new CustomObjectMapper();
    }

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    public static <T> T parse(String json, Class<T> clazz) {
        ObjectMapper mapper = getInstance();
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
                    .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("expense service, parse failed.", e);
        }
        return null;
    }

    public static String stringify(Object object) {
        ObjectMapper mapper = getInstance();
        try {
            mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("stringify failed", e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            map = mapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            log.error("parseJSON2Map failed", e);
        }
        return map;
    }


    public static String parseMap2JSON(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                .enable(JsonParser.Feature.IGNORE_UNDEFINED)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String json = "";
        try {
            json = mapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error("parseMap2JSON failed", e);
        }
        return json;
    }

    public static <C, V> C parseContainer(byte[] bytes, Class<C> containerType, Class<V> elementType) {
        if (bytes == null) {
            return null;
        }

        JavaType javaType = getGenericContainerType(containerType, elementType);
        try {
            return getInstance().readValue(bytes, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取泛型的container Type
     */
    private static JavaType getGenericContainerType(Class<?> containerClass, Class<?>... elementClasses) {
        return getInstance().getTypeFactory()
                .constructParametricType(containerClass, elementClasses);
    }

}

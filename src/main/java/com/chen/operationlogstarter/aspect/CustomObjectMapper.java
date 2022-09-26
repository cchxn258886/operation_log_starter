package com.chen.operationlogstarter.aspect;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @Author chenl
 * @Date 2022/9/16 4:09 下午
 */
public class CustomObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
//         javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addDeserializer(Date.class, new CustomDateDeserializer());
//
//        javaTimeModule.addSerializer(LocalDateTime.class, new CustomTimeSerializer.LocalDateTimeSerializer());
//        javaTimeModule.addSerializer(LocalDate.class, new CustomTimeSerializer.LocalDateSerializer());
//        javaTimeModule.addSerializer(LocalTime.class, new CustomTimeSerializer.LocalTimeSerializer());
//
//        javaTimeModule.addDeserializer(LocalDateTime.class, new CustomTimeSerializer.LocalDateTimeDeserializer());
//        javaTimeModule.addDeserializer(LocalDate.class, new CustomTimeSerializer.LocalDateDeserializer());
//        javaTimeModule.addDeserializer(LocalTime.class, new CustomTimeSerializer.LocalTimeDeserializer());
//        this.registerModule(javaTimeModule);
    }
}

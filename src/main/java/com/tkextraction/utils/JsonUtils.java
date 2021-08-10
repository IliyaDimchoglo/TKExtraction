package com.tkextraction.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class JsonUtils {

    private final static ObjectMapper om = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public <T> String toStr(T data) {
        try {
            return om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonUtils toStr failed to parse obj errMsg=" + e.getMessage());
        }
    }


    public <T> T toObj(Map<String, String> data, Class<T> type) {
        return om.convertValue(data, type);
    }


    public <T> T toObj(String data, Class<T> type) {
        try {
            return om.readValue(data, type);
        } catch (IOException e) {
            throw new RuntimeException("JsonUtils toObj failed to parse json errMsg=" + e.getMessage());
        }
    }

    public <T> T toObj(MultipartFile file, Class<T> type) {
        try {
            return om.readValue(file.getBytes(), type);
        } catch (IOException e) {
            throw new RuntimeException("JsonUtils toObj failed to parse json errMsg=" + e.getMessage());
        }
    }
}

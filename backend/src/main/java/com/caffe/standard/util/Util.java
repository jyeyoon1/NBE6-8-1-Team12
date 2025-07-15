package com.caffe.standard.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
    public static class json {
        public static ObjectMapper objectMapper;

        public static String toString(Object object) {
            return toString(object, null);
        }

        public static String toString(Object object, String defaultValue) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }
}

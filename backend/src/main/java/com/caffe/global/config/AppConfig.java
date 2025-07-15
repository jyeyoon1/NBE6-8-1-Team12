package com.caffe.global.config;

import com.caffe.standard.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    private static Environment env;
    @Autowired
    public void setEnv(Environment env) {
        AppConfig.env = env;
    }

    public static boolean isDevMode() {
        return env.matchesProfiles("dev");
    }

    public static boolean isTestMode() {
        return env.matchesProfiles("test");
    }

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        AppConfig.objectMapper = objectMapper;
    }

    @PostConstruct
    public void postConstruct() {
        Util.json.objectMapper = objectMapper;
    }
}

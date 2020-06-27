package com.telegram.cvetochek.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class ComplimentsDictionary {
    private Map<String, Set<String>> dictionary = new HashMap<>();
}

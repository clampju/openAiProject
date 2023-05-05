package com.openai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "project-config")
public class OpenAiProjectConfig {
    private List<String> imageCreatePrefix;

    private String chatModel;
    private Double temperature;
}

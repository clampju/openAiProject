package com.openai.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: dong.zhang
 * @date: 2023-03-24 15:50
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx-constant")
public class WxConstant {

    private String wxHost = "https://api.weixin.qq.com";

    private String appId;

    private String appSecret;

    private String serverToken;

    private String encodingAesKey;

    private String accessToken;

}

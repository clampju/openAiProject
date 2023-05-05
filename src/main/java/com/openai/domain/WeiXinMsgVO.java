package com.openai.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author: dong.zhang
 * @date: 2023-03-23 08:20
 */
@Data
@AllArgsConstructor
public class WeiXinMsgVO {

    @JsonProperty("CreateTime")
    private Long createTime;
    @JsonProperty("FromUserName")
    private String fromUserName;
    @JsonProperty("ToUserName")
    private String toUserName;
    @JsonProperty("MsgType")
    private String msgType;
    @JsonProperty("Content")
    private String content;
}

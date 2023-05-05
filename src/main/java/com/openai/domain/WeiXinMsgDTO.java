package com.openai.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author: dong.zhang
 * @date: 2023-03-23 08:20
 */
@Data
public class WeiXinMsgDTO {

    /**
     * 开发者微信号
     */
    @JsonProperty("ToUserName")
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @JsonProperty("FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @JsonProperty("CreateTime")
    private String CreateTime;
    /**
     * 消息类型，文本为text
     */
    @JsonProperty("MsgType")
    private String msgType;
    /**
     * 文本消息内容
     */
    @JsonProperty("Content")
    private String content;
    /**
     * 消息id，64位整型
     */
    @JsonProperty("MsgId")
    private String msgId;
    /**
     * 消息的数据ID（消息如果来自文章时才有）
     */
    @JsonProperty("MsgDataId")
    private String msgDataId;
    /**
     * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
     */
    @JsonProperty("Idx")
    private String idx;
}

package com.openai.model;

import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ChatCompletionRequestX implements Serializable {
    private String model;
    private List<ChatMessage> messages;
    private Double temperature;
    private Boolean stream;
    private Integer maxTokens;
    private String user;
}

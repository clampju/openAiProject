package com.openai.model;

import lombok.Data;

@Data
public class ChatCompletion {
    private String userName;
    private String userId;
    private String prompt;
}

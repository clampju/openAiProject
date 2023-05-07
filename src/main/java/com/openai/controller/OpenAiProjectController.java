package com.openai.controller;

import com.openai.config.OpenAiProjectConfig;
import com.openai.model.ChatCompletion;
import com.openai.model.ChatCompletionRequestX;
import com.openai.service.OpenAiProjectService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.github.asleepyfish.enums.RoleEnum;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * chat gpt控制器
 */
@RestController
@RequestMapping("/v1/chat")
public class OpenAiProjectController {
    @Resource
    private OpenAiProjectService openAiProjectService;
    @Resource
    private OpenAiService openAiService;
    /**
     * 简单聊天接口
     */
    @PostMapping("/completions")
    @ResponseBody
    public ChatCompletionResult chatCompletion(@RequestBody ChatCompletionRequestX chatCompletionRequestX) {
        System.out.println("------------------------------"+chatCompletionRequestX.getModel());
        return openAiService.createChatCompletion(ChatCompletionRequest.builder()
                .model(chatCompletionRequestX.getModel())
                .messages(chatCompletionRequestX.getMessages())
                .user(chatCompletionRequestX.getUser())
                .temperature(chatCompletionRequestX.getTemperature())
                .build());
        //return openAiProjectService.getOpenaiCompletionMsgList(chatCompletion.getUserId(),chatCompletion.getPrompt());
    }
}

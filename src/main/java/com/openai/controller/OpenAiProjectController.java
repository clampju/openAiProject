package com.openai.controller;

import com.openai.config.OpenAiProjectConfig;
import com.openai.model.ChatCompletion;
import com.openai.service.OpenAiProjectService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * chat gpt控制器
 */
@RestController
@RequestMapping("/v1/chat")
public class OpenAiProjectController {
    @Resource
    private OpenAiProjectService openAiProjectService;
    private static OpenAiService openAiService;
    /**
     * 简单聊天接口
     */
    @PostMapping("/completions")
    @ResponseBody
    public ChatCompletionResult chatCompletion(@RequestBody ChatCompletionRequest chatCompletionRequest) {
        return openAiService.createChatCompletion(chatCompletionRequest);
        //return openAiProjectService.getOpenaiCompletionMsgList(chatCompletion.getUserId(),chatCompletion.getPrompt());
    }
}

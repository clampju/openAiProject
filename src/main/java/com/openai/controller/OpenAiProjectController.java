package com.openai.controller;

import com.openai.config.OpenAiProjectConfig;
import com.openai.model.ChatCompletion;
import com.openai.service.OpenAiProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * chat gpt控制器
 */
@RestController
@RequestMapping("/openai")
public class OpenAiProjectController {
    @Resource
    private OpenAiProjectService openAiProjectService;

    /**
     * 简单聊天接口
     */
    @PostMapping("/chatCompletion")
    @ResponseBody
    public List<String> chatCompletion(@RequestBody ChatCompletion chatCompletion) {
        return openAiProjectService.getOpenaiCompletionMsgList(chatCompletion.getUserId(),chatCompletion.getPrompt());
    }
}

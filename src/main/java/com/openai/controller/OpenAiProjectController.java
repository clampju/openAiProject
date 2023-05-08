package com.openai.controller;

import com.openai.model.ChatCompletionRequestX;
import com.openai.service.OpenAiProjectService;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @PostMapping(value = "/completions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void chatCompletion(@RequestBody ChatCompletionRequestX chatCompletionRequestX) {
        System.out.println("------------------------------"+chatCompletionRequestX.getModel()+","+chatCompletionRequestX.getStream());
       openAiService.streamChatCompletion(ChatCompletionRequest.builder()
                        .model(chatCompletionRequestX.getModel())
                        .messages(chatCompletionRequestX.getMessages())
                        .user(chatCompletionRequestX.getUser())
                        .temperature(chatCompletionRequestX.getTemperature())
                        .stream(chatCompletionRequestX.getStream())
                        .build())
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(System.out::println);
//        return openAiService.createChatCompletion(ChatCompletionRequest.builder()
//                .model(chatCompletionRequestX.getModel())
//                .messages(chatCompletionRequestX.getMessages())
//                .user(chatCompletionRequestX.getUser())
//                .temperature(chatCompletionRequestX.getTemperature())
//                .stream(chatCompletionRequestX.getStream())
//                .build());
        //return openAiProjectService.getOpenaiCompletionMsgList(chatCompletion.getUserId(),chatCompletion.getPrompt());
    }

}

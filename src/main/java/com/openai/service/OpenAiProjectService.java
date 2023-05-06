package com.openai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.openai.config.OpenAiProjectConfig;
import com.openai.domain.WeiXinMsgDTO;
import com.openai.domain.WeiXinMsgVO;
import com.openai.utils.CommonUtil;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import io.github.asleepyfish.config.ChatGPTProperties;
import io.github.asleepyfish.enums.RoleEnum;
import io.github.asleepyfish.util.OpenAiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;


@Slf4j
@Service
public class OpenAiProjectService {
    @Resource
    private OpenAiProjectConfig openAiProjectConfig;

    /**
     * 生成图片接口----默认生成1张图片
     * @param userId
     * @param content
     * @return
     */
    public String getOpenaiImage(String userId,String content) {
        List<String> imageList = OpenAiUtils.createImage(content,userId);
        log.warn("用户请求,用户:{},请求内容:{},返回内容:{}", userId, content, imageList);
        return (imageList!=null && imageList.size()>0)?imageList.get(0):null;
    }

    public List<String> getOpenaiCompletionMsgList(String userId,String content) {
        List<String> chatCompletion = OpenAiUtils.createChatCompletion(ChatCompletionRequest.builder()
                                            .model(openAiProjectConfig.getChatModel())
                                            .messages(Collections.singletonList(new ChatMessage(RoleEnum.USER.getRoleName(), content)))
                                            .user(userId)
                                            .temperature(openAiProjectConfig.getTemperature())
                                            .build());
        log.warn("用户请求,用户:{},请求内容:{},返回内容:{}", userId, content, chatCompletion);
        return chatCompletion;
    }

    public String getOpenaiCompletionMsg(String userId,String promptContent) {
        StringBuilder content = new StringBuilder();
        List<String> msgList = getOpenaiCompletionMsgList(userId,promptContent);
        for (int i = 0; i < msgList.size(); i++) {
            content.append(msgList.get(i));
            if (i < msgList.size() - 1) {
                content.append("\\n");
            }
        }
        return content.toString();
    }
}

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

    public List<String> getOpenaiMessageList(String userId,String content) {
        List<String> chatCompletion = new ArrayList<String>();
        if(CommonUtil.mateImageCreatePrefix(content,openAiProjectConfig.getImageCreatePrefix())){
            //调用画图接口
            chatCompletion = OpenAiUtils.createImage(content,userId);
        }else{
            //调用聊天接口
            chatCompletion = OpenAiUtils.createChatCompletion(ChatCompletionRequest.builder()
                                            .model(openAiProjectConfig.getChatModel())
                                            .messages(Collections.singletonList(new ChatMessage(RoleEnum.USER.getRoleName(), content)))
                                            .user(userId)
                                            .temperature(openAiProjectConfig.getTemperature())
                                            .build());
        }
        log.warn("用户请求,用户:{},请求内容:{},返回内容:{}", userId, content, chatCompletion);
        return chatCompletion;
    }

    public String getOpenaiMessage(String userId,String promptContent) {
        StringBuilder content = new StringBuilder();
        List<String> msgList = getOpenaiMessageList(userId,promptContent);
        for (int i = 0; i < msgList.size(); i++) {
            content.append(msgList.get(i));
            if (i < msgList.size() - 1) {
                content.append("\\n");
            }
        }
        return content.toString();
    }
}

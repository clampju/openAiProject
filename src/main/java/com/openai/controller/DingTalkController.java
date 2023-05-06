package com.openai.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.openai.config.OpenAiProjectConfig;
import com.openai.service.DingTalkService;
import com.openai.service.OpenAiProjectService;
import com.openai.utils.CommonUtil;
import com.openai.utils.FastJsonUtils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 钉钉接入相关控制器
 */
@Slf4j
@RestController
@RequestMapping("/dtbyopenai")
public class DingTalkController {
    @Resource
    private DingTalkService dingTalkService;
    @Resource
    private OpenAiProjectConfig openAiProjectConfig;
    @Resource
    private OpenAiProjectService openAiProjectService;

    @RequestMapping("/callback")
    public void callbackRobotsByOpenAi(@RequestBody(required = false) JSONObject json) {
        log.warn("--------DingTalk request pam:"+ FastJsonUtils.toJSONString(json));
        String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
        if ("text".equals(json.getString("msgtype"))) {
            String message = null;
            String msgtype = null;
            if(CommonUtil.mateImageCreatePrefix(content,openAiProjectConfig.getImageCreatePrefix())){
                //调用画图接口
                message = openAiProjectService.getOpenaiImage("dingtalk_"+json.getString("senderId"),content);
                msgtype = "image";
            }else {
                //调用聊天接口
                message = openAiProjectService.getOpenaiCompletionMsg("dingtalk_"+json.getString("senderId"),content);
                msgtype = "text";
            }
            dingTalkService.callbackRobots(json.getString("sessionWebhook"),msgtype,message);
        }
    }
}

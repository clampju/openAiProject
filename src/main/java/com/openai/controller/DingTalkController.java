package com.openai.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.openai.service.DingTalkService;
import com.openai.utils.FastJsonUtils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 钉钉接入相关控制器
 */
@Slf4j
@RestController
@RequestMapping("/dtbyopenai")
public class DingTalkController {
    @Resource
    private DingTalkService dingTalkService;

    @RequestMapping("/callback")
    public void helloRobots(@RequestBody(required = false) JSONObject json) {
        log.warn("--------DingTalk request pam:"+ FastJsonUtils.toJSONString(json));
        String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
        if ("text".equals(json.getString("msgtype"))) {
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            String message = dingTalkService.getOpenaiMessageByDT(json.getString("senderId"),content);
            text.setContent(message);
            try {
                String sessionWebhook = json.getString("sessionWebhook");
                DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
                OapiRobotSendRequest request = new OapiRobotSendRequest();
                request.setMsgtype("text");
                request.setText(text);
                OapiRobotSendResponse response = client.execute(request);
                log.warn("--------DingTalk response pam:"+ response.getBody());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}

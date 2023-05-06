package com.openai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.openai.domain.WeiXinMsgDTO;
import com.openai.utils.CommonUtil;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class DingTalkService {

    public OapiRobotSendResponse callbackRobots(String sessionWebhook,String msgtype,String message){
        OapiRobotSendResponse response = null;
        if (StringUtils.hasLength(sessionWebhook) && StringUtils.hasLength(msgtype) && StringUtils.hasLength(message)) {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype(msgtype);
            if(msgtype.equals("text")){
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                text.setContent(message);
                request.setText(text);
            }else if(msgtype.equals("image")){
                OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
                request.setMsgtype("link");
                link.setTitle("");
                link.setText("");
                link.setPicUrl(message);
                link.setMessageUrl(message);
                request.setLink(link);
            }
            response = callbackRobots(request,sessionWebhook);
        }
        return response;
    }

    public OapiRobotSendResponse callbackRobots(OapiRobotSendRequest request,String sessionWebhook){
        OapiRobotSendResponse response = null;
        if(StringUtils.hasLength(sessionWebhook) && request!=null){
            try {
                DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);
                response = client.execute(request);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            log.warn("--------DingTalk callbackRobots response pam:"+ (response!=null?response.getBody():null));
        }
        return response;
    }
}

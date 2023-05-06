package com.openai.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.openai.domain.WeiXinMsgDTO;
import com.openai.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class DingTalkService {
    @Resource
    private OpenAiProjectService openAiProjectService;

    public String getOpenaiMessageByDT(String userid,String content) {
        String msgContent = null;
        if (StringUtils.hasLength(userid) && StringUtils.hasLength(content)) {
            msgContent = openAiProjectService.getOpenaiMessage("dingtalk_"+userid,content);
        }
        return msgContent;
    }
}

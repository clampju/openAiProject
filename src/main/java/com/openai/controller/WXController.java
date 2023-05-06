package com.openai.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.openai.domain.WeiXinMsgDTO;
import com.openai.service.OpenAiProjectService;
import com.openai.service.WxService;
import com.openai.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * chat gpt控制器
 */
@Slf4j
@RestController
@RequestMapping("/wxbyopenai")
public class WXController {
    @Resource
    private WxService wxService;

    /**
     * 用于微信接入验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return java.lang.String
     */
    @GetMapping("/callback")
    @ResponseBody
    public String callback(String signature,String timestamp,String nonce,String echostr) {
        log.warn("--------weixin request pam:"+signature+","+timestamp+","+nonce+","+echostr);
        return wxService.checkSignature(signature, timestamp, nonce, echostr);
    }

    /**
     * 用于接收微信消息
     * @param request
     * @param pw
     * @return void
     */
    @PostMapping("/callback")
    public void callback(HttpServletRequest request, PrintWriter pw) {
        JSONObject jsonObject = CommonUtil.xmlToJson(request);
        WeiXinMsgDTO params = JSON.parseObject(jsonObject.toJSONString(), WeiXinMsgDTO.class);
        pw.write(wxService.callbackRobots(params));
    }


}

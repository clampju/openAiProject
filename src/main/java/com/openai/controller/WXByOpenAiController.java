package com.openai.controller;

import com.openai.service.OpenAiProjectService;
import com.openai.service.WxService;
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
public class WXByOpenAiController {
    @Resource
    private OpenAiProjectService openAiProjectService;
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
        pw.write(openAiProjectService.chat(request));
    }


}

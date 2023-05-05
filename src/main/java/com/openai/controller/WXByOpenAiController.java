package com.openai.controller;

import com.openai.service.OpenAiProjectService;
import com.openai.service.WxService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * chat gpt控制器
 */
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
    public String callback(@RequestParam("signature") String signature,
                          @RequestParam("timestamp") String timestamp,
                          @RequestParam("nonce") String nonce,
                          @RequestParam("echostr") String echostr) {
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

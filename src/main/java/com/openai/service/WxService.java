package com.openai.service;

import com.alibaba.fastjson.JSONObject;
import com.openai.config.WxConstant;
import com.openai.domain.WeiXinMsgDTO;
import com.openai.domain.WeiXinMsgVO;
import com.openai.utils.CommonUtil;
import com.openai.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

@Slf4j
@Service
public class WxService{
    @Resource
    private WxConstant wxConstant;
    @Resource
    private OpenAiProjectService openAiProjectService;

//    @Scheduled(cron = "0 0 0/2 * * ? *")
//    @PostConstruct
    public String refreshToken() {
        String url = wxConstant.getWxHost() + "/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        url = String.format(url, wxConstant.getAppId(), wxConstant.getAppSecret());
        JSONObject result = HttpUtil.getForJson(url);
        wxConstant.setAccessToken(result.getString("access_token"));
        return wxConstant.getAccessToken();
    }

    /**
     * 验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public String checkSignature(String signature, String timestamp, String nonce, String echostr) {
        String[] arr = new String[]{wxConstant.getServerToken(), timestamp, nonce};
        log.warn("--------weixin checkSignature:"+wxConstant.getServerToken()+","+arr);
        // 将token、timestamp、nonce三个参数进行字典序排序
        CommonUtil.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = CommonUtil.byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        if (tmpStr != null) {
            if (tmpStr.equals(signature.toUpperCase())) {
                return echostr;
            }
            return "error";
        }
        return "error";

    }

    public WeiXinMsgVO getOpenaiMessageToWXVO(WeiXinMsgDTO params) {
        String content = "感谢关注!";
        if (params!=null && StringUtils.hasLength(params.getContent())) {
            content = openAiProjectService.getOpenaiMessage(params.getFromUserName(),params.getContent());
        }
        return new WeiXinMsgVO(System.currentTimeMillis(),
                params.getToUserName(), params.getFromUserName(),
                "text", content.toString());
    }

    public String getOpenaiMessageByWX(WeiXinMsgDTO params) {
        String content = "感谢关注!";
        if (params!=null && StringUtils.hasLength(params.getContent())) {
            content = openAiProjectService.getOpenaiMessage(params.getFromUserName(),params.getContent());
        }
        return getResult(params, content);
    }

    private String getResult(WeiXinMsgDTO params, String content) {
        return "<xml>" + "<ToUserName><![CDATA[" + params.getFromUserName() + "]]></ToUserName>"
                + "<FromUserName><![CDATA[" + params.getToUserName() + "]]></FromUserName>" + "<CreateTime>"
                + System.currentTimeMillis() + "</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
                + "<Content><![CDATA[" + new String(content.getBytes(), ISO_8859_1) + "]]></Content></xml>";
    }

}

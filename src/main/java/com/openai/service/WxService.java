package com.openai.service;

import com.alibaba.fastjson.JSONObject;
import com.openai.config.Constant;
import com.openai.service.WxService;
import com.openai.utils.CommonUtil;
import com.openai.utils.HttpUtil;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class WxService{

//    @Scheduled(cron = "0 0 0/2 * * ? *")
//    @PostConstruct
    public String refreshToken() {
        String url = Constant.WX_HOST + "/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        url = String.format(url, Constant.APP_ID, Constant.APP_SECRET);
        JSONObject result = HttpUtil.getForJson(url);
        Constant.ACCESS_TOKEN = result.getString("access_token");
        return Constant.ACCESS_TOKEN;
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
        String[] arr = new String[]{Constant.SERVER_TOKEN, timestamp, nonce};
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

}

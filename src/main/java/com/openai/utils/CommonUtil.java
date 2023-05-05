package com.openai.utils;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author: dong.zhang
 * @date: 2023-03-25 11:18
 */
public class CommonUtil {


    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    public static void sort(String a[]) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[i]) < 0) {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }
    }

    public static JSONObject xmlToJson(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        SAXReader reader = new SAXReader();
        try {
            InputStream ins = request.getInputStream();
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> list = root.elements();
            for (Element element : list) {
                json.put(element.getName(), element.getText());
            }
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static boolean mateImageCreatePrefix(String context,List<String> imageCreatePrefix){
        if(StringUtils.hasLength(context) && imageCreatePrefix!=null){
            for(String icp : imageCreatePrefix){
                List<String> searchWords = Arrays.asList(icp.split("\\..."));
                if (searchWords.size()==1 && String.valueOf(context.charAt(0)).equals(searchWords.get(0))) {
                    //进行头字母的匹配
                    return true;
                }else if(searchWords.size()>1 && context.contains(searchWords.get(0)) && context.contains(searchWords.get(1)) ){
                    //进行"生成...图"的匹配
                    return true;
                }
            }
        }
        return false;
    }


}

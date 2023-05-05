package com.openai.utils;

/**
 * @author: dong.zhang
 * @date: 2023-03-25 10:27
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.openai.domain.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public HttpUtil() {
    }

    public static String delete(String url, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = initHeaders(headers);
            RequestEntity request = new RequestEntity(httpHeaders, HttpMethod.DELETE, new URI(url));
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            return resp.getStatusCode() != null ? (String)resp.getBody() : null;
        } catch (URISyntaxException var6) {
            return null;
        }
    }

    public static <T> ResultVo<T> get(String url, Map<String, String> headers, Class<T> responseType) {
        try {
            RequestEntity request = getRequestInit(url, headers, (Map)null);
            return toDo(url, request, responseType);
        } catch (URISyntaxException var4) {
            return new ResultVo(9999, var4.getMessage());
        }
    }

    public static <T> ResultVo<List<T>> getList(String url, Map<String, String> headers, Class<T> responseType) {
        try {
            RequestEntity request = getRequestInit(url, headers, (Map)null);
            return toDoList(url, request, responseType);
        } catch (URISyntaxException var4) {
            return new ResultVo(9999, var4.getMessage());
        }
    }

    public static JSONObject getForJson(String url) {
        try {
            RequestEntity request = getRequestInit(url, (Map)null, (Map)null);
            RestTemplate rest = new RestTemplate();
            ResponseEntity<JSONObject> resp = rest.exchange(request, JSONObject.class);
            if (resp.getStatusCode() != null && resp.getStatusCodeValue() == 200) {
                JSONObject result = (JSONObject)resp.getBody();
                return result;
            }
        } catch (Exception var5) {
        }

        return null;
    }

    public static String get(String url) {
        try {
            RequestEntity request = getRequestInit(url, (Map)null, (Map)null);
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            return resp.getStatusCode() != null ? (String)resp.getBody() : null;
        } catch (Exception var4) {
            return null;
        }
    }

    private static RequestEntity getRequestInit(String url, Map<String, String> headers, Map<String, Object> params) throws URISyntaxException {
        try {
            HttpHeaders httpHeaders = initHeaders(headers);
            ArrayList tem;
            if (params != null && !params.isEmpty()) {
                if (!url.endsWith("?")) {
                    url = url + "?";
                }

                tem = new ArrayList();
                Iterator var5 = params.keySet().iterator();

                label55:
                while(true) {
                    while(true) {
                        String key;
                        do {
                            if (!var5.hasNext()) {
                                url = url + String.join("&", tem);
                                break label55;
                            }

                            key = (String)var5.next();
                        } while(params.get(key) == null);

                        Object obj = params.get(key);
                        if (!(obj instanceof Long[]) && !(obj instanceof String[]) && !(obj instanceof List) && !(obj instanceof JSONObject) && !Pattern.compile("[\\u4e00-\\u9fa5]").matcher(obj + "").find()) {
                            tem.add(key + "=" + params.get(key));
                        } else {
                            try {
                                if (obj instanceof String) {
                                    tem.add(key + "=" + URLEncoder.encode(params.get(key) + "", "utf-8"));
                                } else {
                                    tem.add(key + "=" + URLEncoder.encode(JSON.toJSONString(params.get(key)), "utf-8"));
                                }
                            } catch (UnsupportedEncodingException var9) {
                                var9.printStackTrace();
                            }
                        }
                    }
                }
            }

            tem = null;
            RequestEntity request;
            if (params != null) {
                request = new RequestEntity(params, httpHeaders, HttpMethod.GET, new URI(url));
            } else {
                request = new RequestEntity(httpHeaders, HttpMethod.GET, new URI(url));
            }

            return request;
        } catch (URISyntaxException var10) {
            throw var10;
        }
    }

    public static <T> ResultVo<T> post(String url, Map<String, Object> params, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            return toDo(url, request, responseType);
        } catch (URISyntaxException var5) {
            return new ResultVo(9999, var5.getMessage());
        }
    }

    public static String post(String url, Map<String, Object> params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            return resp.getStatusCode() != null ? (String)resp.getBody() : null;
        } catch (URISyntaxException var6) {
            return null;
        } catch (HttpServerErrorException | HttpClientErrorException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static String post(String url, MultiValueMap<String, Object> params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            return resp.getStatusCode() != null ? (String)resp.getBody() : null;
        } catch (URISyntaxException var6) {
            return null;
        } catch (HttpServerErrorException | HttpClientErrorException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static String post(String url, List<Map<String, Object>> params) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            return resp.getStatusCode() != null ? (String)resp.getBody() : null;
        } catch (URISyntaxException var6) {
            return null;
        } catch (HttpServerErrorException | HttpClientErrorException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static String post(String url, Map<String, String> headers, MultiValueMap<String, Object> params) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            if (resp.getStatusCode() != null) {
                return (String)resp.getBody();
            }

            return null;
        } catch (HttpServerErrorException | HttpClientErrorException var7) {
            log.error("error", var7);
        } catch (URISyntaxException var8) {
            log.error("error", var8);
        }

        return null;
    }

    public static <T> ResultVo<T> post(String url, Map<String, String> headers, Map<String, Object> params, Class<T> responseType) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            return toDo(url, request, responseType);
        } catch (URISyntaxException var6) {
            return new ResultVo(9999, var6.getMessage());
        }
    }

    public static <T> ResultVo<T> post(String url, Map<String, String> headers, List<Map<String, Object>> params, Class<T> responseType) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            return toDo(url, request, responseType);
        } catch (URISyntaxException var6) {
            return new ResultVo(9999, var6.getMessage());
        }
    }

    public static <T> ResultVo<T> postForFile(String url, Map<String, String> headers, MultiValueMap<String, Object> params, Class<T> responseType) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            return toDo(url, request, responseType);
        } catch (URISyntaxException var6) {
            return new ResultVo(9999, var6.getMessage());
        }
    }

    public static <T> ResultVo<List<T>> postList(String url, Map<String, String> headers, Map<String, Object> params, Class<T> responseType) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            return toDoList(url, request, responseType);
        } catch (URISyntaxException var6) {
            return new ResultVo(9999, var6.getMessage());
        }
    }

    public static <T> ResultVo<List<T>> postList(String url, Map<String, String> headers, List<Map<String, Object>> params, Class<T> responseType) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.POST, new URI(url));
            return toDoList(url, request, responseType);
        } catch (URISyntaxException var6) {
            return new ResultVo(9999, var6.getMessage());
        }
    }

    private static HttpHeaders initHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null && !headers.isEmpty()) {
            Iterator var2 = headers.keySet().iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                httpHeaders.add(key, (String)headers.get(key));
            }
        }

        return httpHeaders;
    }

    private static <T> ResultVo toDo(String url, RequestEntity request, Class<T> responseType) {
        RestTemplate rest = new RestTemplate();

        try {
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            if (resp.getStatusCode() != null) {
                if (resp.getStatusCodeValue() == 200) {
                    String str = (String)resp.getBody();
                    JSONObject result = JSON.parseObject(str);
                    ResultVo resultVo = (ResultVo)result.toJavaObject(ResultVo.class);
                    if (ResultVo.getSuccess().getCode().equals(resultVo.getCode())) {
                        if (responseType != null) {
                            T data = JSON.toJavaObject(result.getJSONObject("data"), responseType);
                            resultVo.setData(data);
                        }

                        return resultVo;
                    } else {
                        return resultVo;
                    }
                } else {
                    return new ResultVo(resp.getStatusCodeValue(), resp.getStatusCode().getReasonPhrase());
                }
            } else {
                return new ResultVo(9999, url + "连接超时");
            }
        } catch (JSONException var9) {
            return new ResultVo(9999, "json解析异常，" + var9.getMessage());
        } catch (HttpServerErrorException | HttpClientErrorException var10) {
            return new ResultVo(var10.getRawStatusCode(), var10.getMessage());
        }
    }

    private static <T> ResultVo<List<T>> toDoList(String url, RequestEntity request, Class<T> responseType) {
        RestTemplate rest = new RestTemplate();

        try {
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            if (resp.getStatusCode() != null) {
                if (resp.getStatusCodeValue() == 200) {
                    String str = (String)resp.getBody();
                    JSONObject result = JSON.parseObject(str);
                    ResultVo resultVo = (ResultVo)result.toJavaObject(ResultVo.class);
                    if (ResultVo.getSuccess().getCode().equals(resultVo.getCode())) {
                        JSONArray data = result.getJSONArray("data");
                        if (data != null) {
                            resultVo.setData(data.toJavaList(responseType));
                            return resultVo;
                        }
                    }

                    return resultVo;
                } else {
                    return new ResultVo(resp.getStatusCodeValue(), resp.getStatusCode().getReasonPhrase());
                }
            } else {
                return new ResultVo(9999, url + "连接超时");
            }
        } catch (JSONException var9) {
            return new ResultVo(9999, "json解析异常，" + var9.getMessage());
        } catch (HttpServerErrorException | HttpClientErrorException var10) {
            return new ResultVo(var10.getRawStatusCode(), var10.getMessage());
        }
    }

    public static void main(String[] args) {
        Map<String, String> header = new HashMap(1);
        header.put("sign", "1");
        ResultVo<String> post = post("http://47.110.157.222:9998/prod-api/api-server/gm/api/cn/forbidUser", header, (List)Collections.emptyList(), String.class);
        System.out.println(post);

        try {
            String post1 = post("http://47.110.157.222:9998/prod-api/api-server/gm/api/cn/forbidUser2", Collections.EMPTY_MAP);
            System.out.println(post1);
        } catch (HttpServerErrorException | HttpClientErrorException var4) {
            var4.printStackTrace();
        }

    }

    public static String put(String url, Map<String, String> headers, MultiValueMap<String, Object> params) {
        HttpHeaders httpHeaders = initHeaders(headers);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            RequestEntity request = new RequestEntity(params, httpHeaders, HttpMethod.PUT, new URI(url));
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> resp = rest.exchange(request, String.class);
            if (resp.getStatusCode() != null) {
                return (String)resp.getBody();
            }

            return null;
        } catch (HttpServerErrorException | HttpClientErrorException var7) {
            log.error("error", var7);
        } catch (URISyntaxException var8) {
            log.error("error", var8);
        }

        return null;
    }
}


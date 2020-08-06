package com.github.firelcw.util;


import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.model.HttpMethod;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;
import com.github.firelcw.model.HttpRequest;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/7/30 17:28
 */
public class HttpUtils {
    private static RequestConfig config;

    private static final CloseableHttpClient httpClient;
    static {
        httpClient = HttpClientBuilder.create().build();
    }
    private HttpUtils() {
    }

    public static void buildConfig(HttpRequestConfig httpConfig) {
       if (config == null) {
           config = RequestConfig.custom()
                   .setConnectTimeout(httpConfig.getConnectTimeout())
                   .setConnectionRequestTimeout(httpConfig.getConnectionRequestTimeout())
                   .setSocketTimeout(httpConfig.getSocketTimeout())
                   .build();
       }
    }

    public static HttpResponse request(HttpRequest param) {
        HttpMethod method = param.getMethod();
        String url = param.getUrl();
        Map<String,String> headers = param.getHeaders();
        Map<String,String> params = param.getQueryParams();
        String content = param.getBody();

        if (config == null) {
            throw new EasyHttpException("request configuration is null");
        }
        if (method == null) {
            throw new EasyHttpException("request method is not supported");
        }

        HttpResponse result = new HttpResponse();
        if (params != null && params.size() > 0) {
            List<BasicNameValuePair> paramList = new ArrayList<>();
            params.forEach((k,v)-> paramList.add(new BasicNameValuePair(k, v)));

           try {
               String urlParam = EntityUtils.toString(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
               if (url.contains("?")) {
                   url = url + "&" + urlParam;
               }else {
                   url = url + "?" + urlParam;
               }
           }catch (IOException e) {
               result.setMessage("param setting error:" + e.getMessage());
               return result;
           }

        }
        HttpUriRequest request;

        switch (method) {

            case GET:
                HttpGet httpGet = new HttpGet(url);
                httpGet.setConfig(config);
                request = httpGet;
                break;
            case POST:
                HttpPost httpPost = new HttpPost(url);
                if (content != null) {
                    StringEntity stringEntity = new StringEntity(content, Consts.UTF_8);
                    stringEntity.setContentType(param.getContentType());
                    httpPost.setEntity(stringEntity);
                }
                httpPost.setConfig(config);
                request = httpPost;
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                if (content != null) {
                    StringEntity stringEntity = new StringEntity(content,Consts.UTF_8);
                    stringEntity.setContentType(param.getContentType());
                    httpPut.setEntity(stringEntity);
                }
                httpPut.setConfig(config);
                request = httpPut;
                break;
            case DELETE:
                HttpDelete httpDelete = new HttpDelete(url);
                httpDelete.setConfig(config);
                request = httpDelete;
                break;
            default:
                result.setMessage("request method is not supported");
                return result;
        }

        //添加请求头
        if (headers != null) {
            headers.forEach(request::addHeader);
        }

        try (CloseableHttpResponse response = httpClient.execute(request)){
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                result.setMessage("the body is null");
                return result;
            }
            String ret = EntityUtils.toString(httpEntity);
            result.setBody(ret);
            result.setStatusCode(response.getStatusLine().getStatusCode());
            result.setMessage(response.getStatusLine().getReasonPhrase());
        } catch (IOException e) {
            result.setMessage("request error:"+e.getMessage());
            return result;
        }

        return result;

    }
}

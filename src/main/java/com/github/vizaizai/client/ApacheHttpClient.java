package com.github.vizaizai.client;


import com.github.vizaizai.apache.HttpDeleteWithBody;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.model.HttpMethod;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpRequestConfig;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.util.Utils;
import org.apache.commons.collections.CollectionUtils;
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
public class ApacheHttpClient extends AbstractClient {
    private RequestConfig config;
    private final CloseableHttpClient httpClient;

    private ApacheHttpClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public static ApacheHttpClient getInstance() {
        return ClientInstance.INSTANCE;

    }

    public static class ClientInstance {
        private ClientInstance() {
        }
        private static final ApacheHttpClient INSTANCE = new ApacheHttpClient();
    }


    public void setConfig(HttpRequestConfig httpConfig) {
       super.setConfig(httpConfig);
       if (config == null) {
           config = RequestConfig.custom()
                   .setConnectTimeout(httpConfig.getConnectTimeout()) //设置连接超时时间
                   .setConnectionRequestTimeout(15000) // 获取连接超时时间
                   .setSocketTimeout(httpConfig.getRequestTimeout()) //请求超时时间
                   .build();
       }
    }

    public HttpResponse request(HttpRequest param) {
        HttpMethod method = param.getMethod();
        String url = param.getUrl();
        Map<String,String> headers = param.getHeaders();
        Map<String,String> params = param.getQueryParams();
        String content = param.getBody();

        if (config == null) {
            throw new EasyHttpException("HttpClient request configuration is null");
        }
        if (method == null) {
            throw new EasyHttpException("HttpClient request method is not supported");
        }

        List<BasicNameValuePair> queryParams = new ArrayList<>();
        HttpResponse result = new HttpResponse();
        if (params != null && params.size() > 0) {
            params.forEach((k,v)-> queryParams.add(new BasicNameValuePair(k, v)));
        }
        HttpUriRequest request;
        switch (method) {

            case GET:
                HttpGet httpGet = new HttpGet(convertUrl(url,queryParams));
                httpGet.setConfig(config);
                request = httpGet;
                break;
            case POST:
                HttpPost httpPost = new HttpPost(convertUrl(url, queryParams, param.getContentType()));
                httpPost.setEntity(genEntity(queryParams,content,param.getContentType()));
                httpPost.setConfig(config);
                request = httpPost;

                break;
            case PUT:
                HttpPut httpPut = new HttpPut(convertUrl(url, queryParams, param.getContentType()));
                httpPut.setEntity(genEntity(queryParams,content,param.getContentType()));
                httpPut.setConfig(config);
                request = httpPut;
                break;
            case DELETE:
                HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(convertUrl(url,queryParams,param.getContentType()));
                httpDelete.setEntity(genEntity(queryParams,content,param.getContentType()));
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
            String ret = EntityUtils.toString(httpEntity, Utils.UTF_8);
            result.setBody(ret);
            result.setStatusCode(response.getStatusLine().getStatusCode());
            result.setContentLength(response.getEntity().getContentLength());
            result.setMessage(response.getStatusLine().getReasonPhrase());
        } catch (IOException e) {
            result.setMessage("request error:"+e.getMessage());
            return result;
        }

        return result;

    }



    /**
     * 转化url
     * @param url 原url
     * @param queryParams 待拼接参数
     * @return String
     */
    private static String convertUrl(String url,  List<BasicNameValuePair> queryParams) {
        if (CollectionUtils.isEmpty(queryParams)) {
            return url;
        }
        try {
            String urlParam = EntityUtils.toString(new UrlEncodedFormEntity(queryParams, Consts.UTF_8));
            if (url.contains("?")) {
                url = url + "&" + urlParam;
            }else {
                url = url + "?" + urlParam;
            }
            return url;
        }catch (IOException e) {
            return url;
        }
    }

    /**
     * 转化url
     * @param url 原url
     * @param queryParams 待拼接参数
     * @param contentType contentType
     * @return String
     */
    private static String convertUrl(String url,  List<BasicNameValuePair> queryParams, String contentType) {
        if (Utils.isForm(contentType)) {
            return url;
        }
        return convertUrl(url, queryParams);
    }
    /**
     * 生成Entity
     * @param contentType contentType
     * @param queryParams 请求参数
     * @param content 请求体字符串
     * @return HttpEntity
     */
    private static HttpEntity genEntity(List<BasicNameValuePair> queryParams, String content,String contentType) {
        if (Utils.isForm(contentType)) {
           return new UrlEncodedFormEntity(queryParams, Consts.UTF_8);
        }
        if (content != null) {
            StringEntity stringEntity = new StringEntity(content, Consts.UTF_8);
            stringEntity.setContentType(contentType);
            return stringEntity;
        }
        return null;
    }
}

package com.github.vizaizai.client;


import com.github.vizaizai.apache.HttpDeleteWithBody;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.model.HttpMethod;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpRequestConfig;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.model.body.InputStreamBody;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.StringNameValues;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaochongwei
 * @date 2020/7/30 17:28
 */
public class ApacheHttpClient extends AbstractClient {
    private RequestConfig config;
    private final CloseableHttpClient httpClient;
    private final SSLConnectionSocketFactory sslConnectionSocketFactory;
    private final HostnameVerifier hostnameVerifier;

    private ApacheHttpClient(SSLConnectionSocketFactory sslConnectionSocketFactory, HostnameVerifier hostnameVerifier) {
        this.sslConnectionSocketFactory = sslConnectionSocketFactory;
        this.hostnameVerifier = hostnameVerifier;

        // SSL
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (sslConnectionSocketFactory != null) {
            httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        }
        if (hostnameVerifier != null) {
            httpClientBuilder.setSSLHostnameVerifier(hostnameVerifier);
        }
        this.httpClient  = httpClientBuilder.build();

    }

    public static ApacheHttpClient getInstance() {
        return new ApacheHttpClient(null, null);
    }
    public static ApacheHttpClient getInstance(SSLConnectionSocketFactory sslConnectionSocketFactory) {
        return new ApacheHttpClient(sslConnectionSocketFactory, null);
    }
    public static ApacheHttpClient getInstance(SSLConnectionSocketFactory sslConnectionSocketFactory, HostnameVerifier hostnameVerifier) {
        return new ApacheHttpClient(sslConnectionSocketFactory, hostnameVerifier);
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

    public HttpResponse request(HttpRequest param) throws IOException{
        HttpMethod method = param.getMethod();
        String url = param.getUrl();
        HeadersNameValues headers = param.getHeaders();
        StringNameValues params = param.getQueryParams();
        String content = param.getBody();

        if (config == null) {
            throw new EasyHttpException("HttpClient request configuration is null");
        }
        if (method == null) {
            throw new EasyHttpException("HttpClient request method is not supported");
        }

        List<BasicNameValuePair> queryParams = new ArrayList<>();
        HttpResponse result = new HttpResponse();
        if (params != null && !params.isEmpty()) {
            params.forEach(e-> queryParams.add(new BasicNameValuePair(e.getName(), e.getValue())));
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
            headers.forEach(e-> request.addHeader(e.getName(), e.getValue()));
        }

        try (CloseableHttpResponse response = httpClient.execute(request)){
            // 响应头
            Header[] allHeaders = response.getAllHeaders();
            if (allHeaders != null && allHeaders.length > 0) {
                HeadersNameValues headersNameValues = new HeadersNameValues();
                for (Header header : allHeaders) {
                    if (header.getName() != null && header.getValue()!=null) {
                        headersNameValues.add(header.getName(),header.getValue());
                    }
                }
                result.setHeaders(headersNameValues);
            }

            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                result.setMessage("Response body is null");
                return result;
            }
            //String ret = EntityUtils.toString(httpEntity, Utils.UTF_8);
            result.setBody(InputStreamBody.ofNullable(httpEntity.getContent(),(int) httpEntity.getContentLength()));
            result.setStatusCode(response.getStatusLine().getStatusCode());
            result.setMessage(response.getStatusLine().getReasonPhrase());
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

    public SSLConnectionSocketFactory getSslConnectionSocketFactory() {
        return sslConnectionSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }
}

package com.github.vizaizai.client;


import com.github.vizaizai.client.apache.BodyEntity;
import com.github.vizaizai.client.apache.HttpDeleteWithBody;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpRequestConfig;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.body.Body;
import com.github.vizaizai.entity.body.InputStreamBody;
import com.github.vizaizai.entity.body.RequestBody;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.VUtils;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.StringNameValues;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 设置最大连接
        connectionManager.setMaxTotal(500);
        // 设置每个主机的最大连接数
        connectionManager.setDefaultMaxPerRoute(20);

        httpClientBuilder.setConnectionManager(connectionManager);
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

    @Override
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

    @Override
    public HttpResponse request(HttpRequest request) throws IOException{
        HttpMethod method = request.getMethod();
        String url = request.getUrl();
        HeadersNameValues headers = request.getHeaders();
        StringNameValues params = request.getParams();
        RequestBodyType bodyType = request.getBody() == null ? null : request.getBody().getType();
        if (config == null) {
            throw new EasyHttpException("HttpClient request configuration is null");
        }
        if (method == null) {
            throw new EasyHttpException("HttpClient request method is not supported");
        }

        List<BasicNameValuePair> queryParams = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            params.forEach(e-> queryParams.add(new BasicNameValuePair(e.getName(), e.getValue())));
        }
        url = this.convertUrl(url, queryParams, bodyType);
        HttpUriRequest httpUriRequest;
        switch (method) {
            case GET:
                HttpGet httpGet = new HttpGet(url);
                httpGet.setConfig(config);
                httpUriRequest = httpGet;
                break;
            case POST:
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(assembleEntity(request));
                httpPost.setConfig(config);
                httpUriRequest = httpPost;
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                httpPut.setEntity(assembleEntity(request));
                httpPut.setConfig(config);
                httpUriRequest = httpPut;
                break;
            case DELETE:
                HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
                httpDelete.setEntity(assembleEntity(request));
                httpDelete.setConfig(config);
                httpUriRequest = httpDelete;
                break;
            case PATCH:
                HttpPatch httpPatch = new HttpPatch(url);
                httpPatch.setEntity(assembleEntity(request));
                httpPatch.setConfig(config);
                httpUriRequest = httpPatch;
                break;
            case HEAD:
                HttpHead httpHead = new HttpHead(url);
                httpHead.setConfig(config);
                httpUriRequest = httpHead;
                break;
            case OPTIONS:
                HttpOptions httpOptions = new HttpOptions(url);
                httpOptions.setConfig(config);
                httpUriRequest = httpOptions;
                break;
            case TRACE:
                HttpTrace httpTrace = new HttpTrace(url);
                httpTrace.setConfig(config);
                httpUriRequest = httpTrace;
                break;
            default:
                throw new EasyHttpException("Request method '" + method.name() +"' is not supported");
        }

        //添加请求头
        if (headers != null) {
            headers.forEach(e-> httpUriRequest.addHeader(e.getName(), e.getValue()));
        }
        // 返回数据
        HttpResponse result = new HttpResponse();
        try (CloseableHttpResponse response = httpClient.execute(httpUriRequest)){
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
    private String convertUrl(String url,  List<BasicNameValuePair> queryParams, RequestBodyType bodyType) {
        if (VUtils.isEmpty(queryParams) || RequestBodyType.X_WWW_FROM_URL_ENCODED.equals(bodyType)) {
            return url;
        }
        try {
            String urlParam = EntityUtils.toString(new UrlEncodedFormEntity(queryParams, this.getHttpRequestConfig().getEncoding()));
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
     * 组装实体
     */
    private static HttpEntity assembleEntity(HttpRequest request) {
        RequestBody body = request.getBody();
        // 无请求体
        if (body == null || RequestBodyType.NONE.equals(body.getType())) {
            return null;
        }
        // 文件上传类: 二进制和form-data
        if (RequestBodyType.BINARY.equals(body.getType())
                || RequestBodyType.FORM_DATA.equals(body.getType())) {
            return new BodyEntity(body, request.getEncoding(), request.getContentType());
        }
        // 文本类型
        if (body.getContent()!= null) {
            Body content = body.getContent();
            return new InputStreamEntity(content.asInputStream(), content.length(),
                    ContentType.create(request.getContentType(), request.getEncoding()));
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

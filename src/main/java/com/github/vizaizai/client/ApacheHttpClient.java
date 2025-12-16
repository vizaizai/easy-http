package com.github.vizaizai.client;


import com.github.vizaizai.client.apache.BodyEntity;
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
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.util.TimeValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liaochongwei
 * @date 2020/7/30 17:28
 */
public class ApacheHttpClient extends AbstractClient {
    private CloseableHttpClient httpClient;
    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    private ApacheHttpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
        this.poolingHttpClientConnectionManager = poolingHttpClientConnectionManager;
    }

    public static ApacheHttpClient getInstance() {
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(20)
                .build();
        return new ApacheHttpClient(connectionManager);
    }

    public static ApacheHttpClient getInstance(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
        return new ApacheHttpClient(poolingHttpClientConnectionManager);
    }

    @Override
    public void init(HttpRequestConfig httpConfig) {
       super.init(httpConfig);
       if (httpConfig == null) {
           throw new EasyHttpException("HttpClient request configuration is null");
       }
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(httpConfig.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .setSocketTimeout(httpConfig.getRequestTimeout(), TimeUnit.MILLISECONDS)
                .setTimeToLive(TimeValue.ofMinutes(5))
                .build();
       poolingHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        this.httpClient  = httpClientBuilder.build();
    }

    @Override
    public HttpResponse request(HttpRequest request) throws IOException{
        HttpMethod method = request.getMethod();
        String url = request.getUrl();
        HeadersNameValues headers = request.getHeaders();
        StringNameValues params = request.getParams();
        RequestBodyType bodyType = request.getBody() == null ? null : request.getBody().getType();
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
                httpUriRequest = new HttpGet(url);
                break;
            case POST:
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(assembleEntity(request));
                httpUriRequest = httpPost;
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                httpPut.setEntity(assembleEntity(request));
                httpUriRequest = httpPut;
                break;
            case DELETE:
                HttpDelete httpDelete = new HttpDelete(url);
                httpDelete.setEntity(assembleEntity(request));
                httpUriRequest = httpDelete;
                break;
            case PATCH:
                HttpPatch httpPatch = new HttpPatch(url);
                httpPatch.setEntity(assembleEntity(request));
                httpUriRequest = httpPatch;
                break;
            case HEAD:
                httpUriRequest = new HttpHead(url);
                break;
            case OPTIONS:
                httpUriRequest = new HttpOptions(url);
                break;
            case TRACE:
                httpUriRequest = new HttpTrace(url);
                break;
            default:
                throw new EasyHttpException("Request method '" + method.name() +"' is not supported");
        }

        //添加请求头
        if (headers != null) {
            headers.forEach(e-> httpUriRequest.addHeader(e.getName(), e.getValue()));
        }

        return httpClient.execute(httpUriRequest, response->{
            // 返回数据
            HttpResponse result = new HttpResponse();
            // 响应头
            Header[] allHeaders = response.getHeaders();
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
            result.setBody(InputStreamBody.ofNullable(httpEntity.getContent(), httpEntity.getContentLength()));
            result.setStatusCode(response.getCode());
            result.setMessage(response.getReasonPhrase());
            return result;
        });
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
        }catch (Exception e) {
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

    public PoolingHttpClientConnectionManager getConnectionManager() {
        return this.poolingHttpClientConnectionManager;
    }

}

package com.github.vizaizai.client;

import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpRequestConfig;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.body.InputStreamBody;
import com.github.vizaizai.entity.body.RequestBody;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.VUtils;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.NameValue;
import com.github.vizaizai.util.value.StringNameValues;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.vizaizai.util.Utils.*;
import static java.lang.String.format;

/**
 * @author liaochongwei
 * @date 2020/8/26 14:28
 */
public class DefaultURLClient extends AbstractClient{
    private final SSLSocketFactory sslContextFactory;
    private final HostnameVerifier hostnameVerifier;

    public DefaultURLClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
        this.sslContextFactory = sslContextFactory;
        this.hostnameVerifier = hostnameVerifier;
    }

    public static DefaultURLClient getInstance() {
        return new DefaultURLClient(null,null);
    }

    public static DefaultURLClient getInstance(SSLSocketFactory sslContextFactory) {
        return new DefaultURLClient(sslContextFactory,null);
    }

    public static DefaultURLClient getInstance(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
        return new DefaultURLClient(sslContextFactory,hostnameVerifier);
    }

    @Override
    public HttpResponse request(HttpRequest request) throws IOException{
        HttpRequestConfig config = super.getHttpRequestConfig();

        Entity entity = new Entity(request);
        HeadersNameValues headers = new HeadersNameValues();
        this.addHeaders(request, headers);

        final URL url;
        final HttpURLConnection connection;
        url = new URL(entity.url);
        connection = (HttpURLConnection) url.openConnection();
        // ssl
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection sslConnection = (HttpsURLConnection) connection;
            if (this.sslContextFactory != null) {
                sslConnection.setSSLSocketFactory(sslContextFactory);
            }
            if (this.hostnameVerifier != null) {
                sslConnection.setHostnameVerifier(this.hostnameVerifier);
            }
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setConnectTimeout(config.getConnectTimeout());
        connection.setReadTimeout(config.getRequestTimeout());
        connection.setAllowUserInteraction(false);
        connection.setInstanceFollowRedirects(false);
        // 不支持PATCH请求
        connection.setRequestMethod(request.getMethod().name());
        connection.setChunkedStreamingMode(8192);

        for (NameValue<String,String> nameValue : headers) {
            connection.addRequestProperty(nameValue.getName(), nameValue.getValue());
        }
        connection.connect();
        if (entity.body != null) {
            try (OutputStream out = connection.getOutputStream()) {
                entity.body.writeTo(out, request.getEncoding());
            }
        }
        return this.convertResponse(connection);

    }

    /**
     * 转化为HttpResponse
     * @param connection
     * @return HttpResponse
     * @throws IOException
     */
    private HttpResponse convertResponse(HttpURLConnection connection) throws IOException {

        HttpResponse response = new HttpResponse();
        int status = connection.getResponseCode();
        response.setStatusCode(status);
        response.setMessage(connection.getResponseMessage());

        if (status < 0) {
            throw new IOException(format("Invalid status(%s) executing %s %s", status,
                    connection.getRequestMethod(), connection.getURL()));
        }
        // 响应头
        Map<String, List<String>> allHeaders = connection.getHeaderFields();
        if (VUtils.isNotEmpty(allHeaders)) {
            Set<Map.Entry<String, List<String>>> entries = allHeaders.entrySet();
            HeadersNameValues headersNameValues = new HeadersNameValues();
            for (Map.Entry<String, List<String>> entry : entries) {
                if (entry.getKey() != null && entry.getValue()!= null) {
                    headersNameValues.addHeaders(entry.getKey(), entry.getValue());
                }
            }
            response.setHeaders(headersNameValues);
        }

        if (status >= 400) {
            response.setBody(InputStreamBody.ofNullable(connection.getErrorStream(), connection.getContentLength()));
        } else {
            response.setBody(InputStreamBody.ofNullable(connection.getInputStream(), connection.getContentLength()));
        }

        return response;
    }

    /**
     * 添加默认
     * @param request
     */
    private void addHeaders(HttpRequest request, HeadersNameValues headers) {

        if (VUtils.isNotEmpty(request.getHeaders())) {
            headers.addAll(request.getHeaders());
        }
        if (request.getContentType() != null) {
            headers.add(CONTENT_TYPE, request.getContentType());
        }
        if (request.getContentType() != null && request.getBody() != null) {
            headers.add(CONTENT_LENGTH, Utils.toString(request.getBody().length(request.getEncoding())));
        }
        if (request.getHeaders().getHeaders(ACCEPT).isEmpty()) {
            headers.add(ACCEPT,"*/*");
        }
    }

    public static class Entity {
        private RequestBody body;
        private String url;

        public Entity(HttpRequest request) {
            url = request.getUrl();
            RequestBody requestBody = request.getBody();
            if (requestBody == null || requestBody.getType().equals(RequestBodyType.NONE)) {
                this.handleUrl(request.getParams(),request.getEncoding());
                return;
            }
            body = requestBody;
        }

        private void handleUrl(StringNameValues params, Charset charset) {
            String urlParams = asUrlEncoded(params, charset.name());
            if (urlParams == null) {
                return;
            }
            if (url.contains("?")) {
                url = url + "&" + urlParams;
            }else {
                url = url + "?" + urlParams;
            }
        }
    }

    public SSLSocketFactory getSslContextFactory() {
        return sslContextFactory;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }
}

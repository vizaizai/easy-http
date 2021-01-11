package com.github.vizaizai.client;

import com.github.vizaizai.model.HttpMethod;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpRequestConfig;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.model.body.InputStreamBody;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.StringNameValue;
import com.github.vizaizai.util.value.StringNameValues;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.github.vizaizai.util.Utils.*;
import static java.lang.String.format;

/**
 * @author liaochongwei
 * @date 2020/8/26 14:28
 */
public class DefaultURLClient extends AbstractClient{

    public static DefaultURLClient getInstance() {
        return DefaultURLClient.ClientInstance.INSTANCE;

    }
    public static class ClientInstance {
        private ClientInstance() {
        }
        private static final DefaultURLClient INSTANCE = new DefaultURLClient();
    }


    @Override
    public HttpResponse request(HttpRequest param) throws IOException{
        HttpRequestConfig config = super.getConfig();

        Entity entity = new Entity();
        entity.init(param);

        HeadersNameValues headers = new HeadersNameValues();
        this.addHeaders(param, headers);


        final URL url;
        final HttpURLConnection connection;
        url = new URL(entity.url);
        connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(config.getConnectTimeout());
        connection.setReadTimeout(config.getRequestTimeout());
        connection.setAllowUserInteraction(false);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(param.getMethod().name());


        for (StringNameValue nameValue : headers) {
            connection.addRequestProperty(nameValue.getName(), nameValue.getValue());
        }

        if (entity.body != null) {
            connection.setChunkedStreamingMode(8196);
            connection.setDoOutput(true);
            try (OutputStream out = connection.getOutputStream()) {
                out.write(entity.body);
            }
        }
        connection.connect();
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
        // Charset charset = getCharset(connection.getHeaderField(CONTENT_TYPE));
        if (status >= 400) {
            response.setBody(InputStreamBody.ofNullable(connection.getErrorStream(), connection.getContentLength()));
            //response.setBody(Utils.toString(connection.getErrorStream(), charset));
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

        if (CollectionUtils.isNotEmpty(request.getHeaders())) {
            headers.addAll(request.getHeaders());
        }
        if (request.getContentType() != null) {
            headers.add(CONTENT_TYPE, request.getContentType());
        }
        if (request.getHeaders().getHeaders(ACCEPT).isEmpty()) {
            headers.add(ACCEPT,"*/*");
        }
    }

    public static class Entity {
        private byte[] body;
        private String url;

        public void init(HttpRequest request) {
            url = request.getUrl();
            if (HttpMethod.GET.equals(request.getMethod())) {
                this.handleUrl(request.getQueryParams());
                return;
            }
            if (isForm(request.getContentType())) {
                this.addBody(asUrlEncoded(request.getQueryParams()));
            }else {
                this.addBody(request.getBody());
                this.handleUrl(request.getQueryParams());
            }


        }

        private void addBody(String content) {
            if (content != null) {
                this.body = content.getBytes(UTF_8);
            }
        }

        private void handleUrl(StringNameValues params) {
            String urlParams = asUrlEncoded(params, UTF_8.name());
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
}

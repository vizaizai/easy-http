package com.github.vizaizai.hander;

import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.model.RetrySettings;
import com.github.vizaizai.parser.ArgParser;
import com.github.vizaizai.parser.InterfaceParser;
import com.github.vizaizai.parser.MethodParser;

import java.util.List;

/**
 * 上下文
 * @author liaochongwei
 * @date 2020/12/16 11:26
 */
public interface Context {
    /**
     * 请求信息
     * @return HttpRequest
     */
    HttpRequest getRequest();

    /**
     * 响应信息
     * @return HttpResponse
     */
    HttpResponse getResponse();

    /**
     * 拦截器
     * @return List<HttpInterceptor>
     */
    List<HttpInterceptor> getInterceptors();

    /**
     * 方法解析
     * @return MethodParser
     */
    MethodParser getMethodParser();
    /**
     * 方法参数解析
     * @return List<ArgParser>
     */
    List<ArgParser> getArgParsers();

    /**
     * 接口解析
     * @return InterfaceParser
     */
    InterfaceParser getInterfaceParser();

    /**
     * 编码器
     * @return Encoder
     */
    Encoder getEncoder();

    /**
     * 解码器
     * @return Decoder
     */
    Decoder getDecoder();

    /**
     * 重试设置
     * @return RetrySettings
     */
    RetrySettings getRetrySettings();
}

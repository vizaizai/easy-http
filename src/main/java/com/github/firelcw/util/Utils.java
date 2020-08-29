package com.github.firelcw.util;

import com.github.firelcw.annotation.Headers;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liaochongwei
 * @date 2020/8/26 14:41
 */
public class Utils {

    private Utils() {
    }

    /**
     * The HTTP Content-Length header field name.
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * The HTTP Content-Encoding header field name.
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String ACCEPT = "Accept";

    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * Value for the Content-Encoding header that indicates that GZIP encoding is in use.
     */
    public static final String ENCODING_GZIP = "gzip";
    /**
     * Value for the Content-Encoding header that indicates that DEFLATE encoding is in use.
     */
    public static final String ENCODING_DEFLATE = "deflate";
    /**
     * UTF-8: eight-bit UCS Transformation Format.
     */
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static final int BUF_SIZE = 0x800; // 2K chars (4K bytes)

    public static final String PLACEHOLDER_PREFIX = "{";
    public static final String PLACEHOLDER_SUFFIX = "}";

    public static final String COLON = ":";


    private static final Logger logger = LoggerFactory.getLogger(Utils.class);



    public static String asUrlEncoded(Map<String, String> source) {
        return asUrlEncoded(source,null);
    }
    /**
     * 做url编码转换
     * 如果encode存在，则会对value进行百分号编码
     * @param source
     * @param encode
     * @return String
     */
    public static String asUrlEncoded(Map<String, String> source, String encode) {
        if (MapUtils.isEmpty(source)) {
            return null;
        }
        Iterator<String> it = source.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()){
            String key = it.next();
            String value = source.get(key);
            if (StringUtils.isBlank(value)){
                continue;
            }
            if (encode != null) {
                value = urlEncode(value,encode);
            }
            sb.append("&").append(key).append("=").append(value);
        }
        return sb.substring(1);
    }

    public static String urlEncode(String source, String encode) {
        try {
            return URLEncoder.encode(source, encode);
        } catch (UnsupportedEncodingException e) {
            logger.error("URL encode error: {}", e.getMessage());
            return source;
        }
    }

    public static void ensureClosed(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) { // NOPMD
            }
        }
    }

    public static String toString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        Reader reader = new InputStreamReader(inputStream,UTF_8);
        try {
            StringBuilder to = new StringBuilder();
            CharBuffer charBuf = CharBuffer.allocate(BUF_SIZE);
            // must cast to super class Buffer otherwise break when running with java 11
            while (reader.read(charBuf) != -1) {
                charBuf.flip();
                to.append(charBuf);
                charBuf.clear();
            }
            return to.toString();
        } finally {
            ensureClosed(reader);
        }
    }

    /**
     * 格式化占位符
     * /books?name={name}&author={author}
     * @param source
     * @param params
     * @return
     */
    public static String formatPlaceholder(String source, Map<String,String> params) {
        if (MapUtils.isEmpty(params)) {
            return source;
        }
        StringBuilder buf = new StringBuilder(source);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();

                String propVal = params.get(placeholder);
                if (propVal != null) {
                    buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                    nextIndex = startIndex + propVal.length();
                } else {
                    logger.warn("Could not resolve placeholder {} in [{}]", placeholder, source);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();

    }


    /**
     * 从@Headers获取请求头
     * @param annotations
     * @return  Map<String,String>
     */
    public static Map<String,String> getHeaders(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        return Stream.of(annotations)
                     .filter(e -> e instanceof Headers)
                     .flatMap(e -> {
                         String[] values = ((Headers) e).value();
                         return Stream.of(values);
                     })
                     .map(Utils::genKeyValue)
                     .collect(Collectors.toMap(e->e[0], e->e[1]));
    }

    private static String[] genKeyValue(String header) {
        String[] split = header.split(COLON+" ");
        if (split.length != 2 ) {
            logger.error("Formatting error: {}", header);
            throw new IllegalArgumentException("Formatting error");
        }
        split[0] = split[0].trim();
        split[1] = split[1].trim();
        return split;
    }

}

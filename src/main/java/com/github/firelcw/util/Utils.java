package com.github.firelcw.util;

import com.github.firelcw.exception.EasyHttpException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

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
            try {
                // URL 编码
                if (encode != null)
                    value = URLEncoder.encode(value, encode);
            } catch (UnsupportedEncodingException e) {
                throw new EasyHttpException("URLEncoder error");
            }

            sb.append("&").append(key).append("=").append(value);
        }
        return sb.substring(1);
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

}

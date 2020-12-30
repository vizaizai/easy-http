package com.github.vizaizai.util;

import com.github.vizaizai.annotation.Headers;
import com.github.vizaizai.model.ContentType;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.StringNameValue;
import com.github.vizaizai.util.value.StringNameValues;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import com.github.vizaizai.logging.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liaochongwei
 * @date 2020/8/26 14:41
 */
public class Utils {

    public static final String CHARSET_S_F = "charset=";

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

    public static final String COMMA = ",";


    private static final Logger logger = LoggerFactory.getLogger(Utils.class);



    public static String asUrlEncoded(StringNameValues source) {
        return asUrlEncoded(source,null);
    }
    /**
     * 做url编码转换
     * 如果encode存在，则会对value进行百分号编码
     * @param source
     * @param encode
     * @return String
     */
    public static String asUrlEncoded(StringNameValues source, String encode) {
        if (CollectionUtils.isEmpty(source)) {
            return null;
        }
        Iterator<StringNameValue> iterator = source.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()){
            StringNameValue nameValue = iterator.next();
            String key = nameValue.getName();
            String value = nameValue.getValue();
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


    public static Charset getCharset(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return UTF_8;
        }
        int index = contentType.indexOf(CHARSET_S_F);
        if (index == -1) {
            return UTF_8;
        }
        return Charset.forName(contentType.substring(index + CHARSET_S_F.length()));
    }

    /**
     * 是否表单提交
     * @param contentType
     * @return boolean
     */
    public static boolean isForm(String contentType) {
      if (StringUtils.isBlank(contentType) || contentType.length() < 33) {
          return false;
      }
      return contentType.startsWith(ContentType.APPLICATION_FORM_URLENCODED_UTF8.substring(0, 33 - 1));
    }


    public static void ensureClosed(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) { // NOPMD
            }
        }
    }


    public static String toString(InputStream inputStream, Charset charset) throws IOException {
        if (inputStream == null) {
            return null;
        }
        if (charset == null) {
            charset = UTF_8;
        }
        Reader reader = new InputStreamReader(inputStream, charset);
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
     * @return path
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
    public static HeadersNameValues getHeaders(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return null;
        }
        List<StringNameValue> nameValues = Stream.of(annotations)
                                                .filter(e -> e instanceof Headers)
                                                .flatMap(e -> {
                                                    String[] values = ((Headers) e).value();
                                                    return Stream.of(values);
                                                })
                                                .map(Utils::genKeyValue)
                                                .flatMap(Collection::stream)
                                                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(nameValues)) {
            return null;
        }
        HeadersNameValues headersNameValues = new HeadersNameValues();
        headersNameValues.addAll(nameValues);

        return headersNameValues;
    }


    /**
     * 转化为NameMap
     * @param queryObj
     * @return StringNameValues
     */
    public static StringNameValues toNameValues(Map<?,?> queryObj) {
        StringNameValues nameValues = new StringNameValues();
        for (Map.Entry<?, ?> entry : queryObj.entrySet()) {
            Object key = entry.getKey();
            Object value = queryObj.get(entry.getKey());
            if (value == null) {
                continue;
            }
            String strKey = String.valueOf(key);
            if (TypeUtils.isArrayType(value.getClass())) { // 值为数组
                nameValues.addAll(getNameValuesFromArray(strKey, value));
            } else if (value instanceof Iterable) { // 值为集合
                nameValues.addAll(getNameValuesFromList(strKey, (Iterable<?>) value));
            } else {
                nameValues.add(strKey, String.valueOf(value));
            }

        }
        return nameValues;
    }

    /**
     * 从数组中获取NameValues
     * @param key keu
     * @param value array
     * @return StringNameValues
     */
    public static StringNameValues getNameValuesFromArray(String key, Object value) {
        StringNameValues nameValues = new StringNameValues();
        if (!TypeUtils.isArrayType(value.getClass())) {
           return null;
        }
        int length = Array.getLength(value);
        if (length == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            nameValues.add(key, String.valueOf(Array.get(value, i)));
        }
        return nameValues;
    }

    /**
     * 从集合中获取NameValues
     * @param key
     * @param iterable
     * @return StringNameValues
     */
    public static StringNameValues getNameValuesFromList(String key, Iterable<?> iterable) {
        StringNameValues nameValues = new StringNameValues();
        for (Object item : iterable) {
            nameValues.add(key, String.valueOf(item));
        }
        return nameValues;
    }

    private static List<StringNameValue> genKeyValue(String header) {
        String[] split = header.split(COLON+" ");
        if (split.length != 2 ) {
            logger.error("Formatting error: {}", header);
            throw new IllegalArgumentException("Formatting error");
        }
        String name =  split[0].trim();
        String value = split[1].trim();
        List<StringNameValue> nameValues = new LinkedList<>();
        String[] values = value.split(COMMA);

        for (String v : values) {
            nameValues.add(new StringNameValue(name,v));
        }
        return nameValues;
    }



    public static void outputClazz(byte[] bytes) {
        FileOutputStream out = null;
        try {
            String pathName = Utils.class.getResource("/").getPath() + "AutoByte.class";
            out = new FileOutputStream(new File(pathName));
            System.out.println("类输出路径：" + pathName);
            out.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

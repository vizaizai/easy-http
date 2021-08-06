package com.github.vizaizai.util;

import com.github.vizaizai.annotation.Headers;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.logging.LoggerFactory;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.NameValue;
import com.github.vizaizai.util.value.StringNameValue;
import com.github.vizaizai.util.value.StringNameValues;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.slf4j.Logger;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLEncoder;
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
    public static final String CLASS = "class";

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
    public static final Charset ASCII = StandardCharsets.US_ASCII;
    private static final int BUF_SIZE = 0x800; // 2K chars (4K bytes)

    public static final String PLACEHOLDER_PREFIX = "{";
    public static final String PLACEHOLDER_SUFFIX = "}";

    public static final String COLON = ":";

    public static final String COMMA = ",";

    private static final Random rand = new Random();
    private static final char[] MULTIPART_CHARS =
            "_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

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
        if (VUtils.isEmpty(source)) {
            return null;
        }
        Iterator<NameValue<String,String>> iterator = source.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()){
            NameValue<String,String> nameValue = iterator.next();
            String key = nameValue.getName();
            String value = nameValue.getValue();
            if (key == null || value == null){
                continue;
            }
            if (encode != null) {
                key = urlEncode(key,encode);
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

    public static String toString(Object o) {
        if (o == null) return null;
        return o.toString();
    }
    public static String toText(Object o) {
        if (o == null) return "";
        return o.toString();
    }

    public static Charset getCharset(String contentType) {
        if (VUtils.isBlank(contentType)) {
            return UTF_8;
        }
        int index = contentType.indexOf(CHARSET_S_F);
        if (index == -1) {
            return UTF_8;
        }
        return Charset.forName(contentType.substring(index + CHARSET_S_F.length()));
    }


    /**
     * 格式化占位符
     * /books?name={name}&author={author}
     * @param source
     * @param params
     * @return path
     */
    public static String formatPlaceholder(String source, Map<String,String> params) {
        if (VUtils.isEmpty(params)) {
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
        if (VUtils.isEmpty(nameValues)) {
            return null;
        }
        HeadersNameValues headersNameValues = new HeadersNameValues();
        headersNameValues.addAll(nameValues);

        return headersNameValues;
    }

    /**
     * JavaBean转化为Map
     * @param bean JavaBean
     * @return Map
     */
    public static Map<String,Object> bean2Map(Object bean) {
        Class<?> beanClazz = bean.getClass();
        Map<String,Object> beanMap = new HashMap<>();
        try {
            // 获取Bean信息
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz);
            // 获取属性描述
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String name = property.getName();
                // 过滤class属性
                if (!name.equals(CLASS)) {
                    // 得到属性对应的getter方法
                    Method getter = property.getReadMethod();
                    // 执行getter方法得到属性值
                    Object value = getter.invoke(bean);
                    beanMap.put(name, value);
                }
            }
            return beanMap;
        }catch (Exception e) {
            throw new EasyHttpException("JavaBean convert to map error.");
        }
    }
    /**
     * 转化为StringNameValues
     * @param varName 方法参数名
     * @param object 方法参数
     * @return StringNameValues
     */
    public static StringNameValues encodeNameValue(String varName, Object object, Type type) {
        if (object == null) {
            return null;
        }
        StringNameValues nameValues = getNameValues(varName, object, type);
        if (nameValues != null) {
            return nameValues;
        }
        // 参数值为JavaBean或者map
        Map<?,?> values;
        if (object instanceof Map) {
            values = (Map<?, ?>) object;
        } else {
            values = bean2Map(object);
        }
        nameValues = new StringNameValues();
        for (Map.Entry<?, ?> entry : values.entrySet()) {
            Object key = entry.getKey();
            Object value = values.get(entry.getKey());
            if (value == null) {
                continue;
            }
            String strKey = toString(key);
            StringNameValues itemNameValues = getNameValues(strKey, value, value.getClass());
            if (itemNameValues != null) {
                nameValues.addAll(itemNameValues);
            }
        }
        return nameValues;
    }

    private static StringNameValues getNameValues(String key, Object value, Type type) {
        // 基本类型
        if (TypeUtils.isBaseType(type, value)) {
            StringNameValues nameValues = new StringNameValues();
            nameValues.add(key,toString(value));
            return nameValues;
        }

        if (TypeUtils.isArrayType(type)) { // 值为数组
            return getNameValuesFromArray(key, value);
        }
        if (value instanceof Iterable) { // 值为集合
            return getNameValuesFromList(key, (Iterable<?>) value);
        }
        // 其它类型（JavaBean、map）
        return null;
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
            nameValues.add(key, toString(Array.get(value, i)));
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
            nameValues.add(key,toString(item));
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


    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String getMultiContentType(String boundary) {
        return "multipart/form-data; boundary=" + boundary;
    }
    public static String generateBoundary() {
        final StringBuilder buffer = new StringBuilder();
        final int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }


    /**
     * unicode字符串转化成普通字符串
     * @param unicode unicode
     * @return 普通字符串
     */
    public static String unicodeToString(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return unicode;
        }

        final int len = unicode.length();
        StringBuilder sb = new StringBuilder();
        int i;
        int pos = 0;
        while ((i = StringUtils.indexOfIgnoreCase(unicode, "\\u", pos)) != -1) {
            sb.append(unicode, pos, i);//写入Unicode符之前的部分
            pos = i;
            if (i + 5 < len) {
                char c;
                try {
                    c = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
                    sb.append(c);
                    pos = i + 6;//跳过整个Unicode符
                } catch (NumberFormatException e) {
                    //非法Unicode符，跳过
                    sb.append(unicode, pos, i + 2);//写入"\\u"
                    pos = i + 2;
                }
            } else {
                pos = i;//非Unicode符，结束
                break;
            }
        }

        if (pos < len) {
            sb.append(unicode, pos, len);
        }
        return sb.toString();
    }
}

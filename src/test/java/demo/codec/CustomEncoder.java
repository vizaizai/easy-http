package demo.codec;

import com.github.firelcw.codec.Encoder;

import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/8/4 13:41
 */
public class CustomEncoder implements Encoder {
    /**
     * 将对象转化成成Map<String,String> 用于编码@Params和@Headers注解的对象
     * @param object 待编码对象
     * @return map
     */
    @Override
    public Map<String, String> encodeMap(Object object) {
        return null;
    }

    /**
     * 将对象转化成string，用于编码 @Data注解的对象（默认是解析成json字符串）
     * @param object
     * @return string
     */
    @Override
    public String encodeString(Object object) {
        return null;
    }
}

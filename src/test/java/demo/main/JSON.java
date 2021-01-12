package demo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vizaizai.exception.CodecException;

/**
 * @author liaochongwei
 * @date 2021/1/12 16:31
 */
public class JSON {
    public static String toJSONString(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        }catch (Exception e) {
            throw new CodecException(e);
        }
    }
}

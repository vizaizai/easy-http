package demo.main;

import com.github.vizaizai.exception.CodecException;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author liaochongwei
 * @date 2021/1/12 16:31
 */
public class JSON {
    public static String toJSONString(Object o) {
        JsonMapper mapper = JsonMapper.builder().build();
        try {
            return mapper.writeValueAsString(o);
        }catch (Exception e) {
            throw new CodecException(e);
        }
    }
}

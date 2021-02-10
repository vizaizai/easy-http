package demo.codec;

import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.entity.HttpResponse;

import java.lang.reflect.Type;

/**
 * @author liaochongwei
 * @date 2020/8/4 13:50
 */
    public class CustomDecoder implements Decoder {
        @Override
        public Object decode(HttpResponse response, Type type) {
            return null;
        }
    }

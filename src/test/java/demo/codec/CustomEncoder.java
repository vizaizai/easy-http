package demo.codec;

import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.entity.body.Body;

import java.lang.reflect.Type;

/**
 * @author liaochongwei
 * @date 2020/8/4 13:41
 */
public class CustomEncoder implements Encoder {


    @Override
    public Body encode(Object object, Type bodyType) {
        return null;
    }
}

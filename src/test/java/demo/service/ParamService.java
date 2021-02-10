package demo.service;

import com.github.vizaizai.annotation.Get;
import com.github.vizaizai.annotation.Param;
import com.github.vizaizai.annotation.Post;
import com.github.vizaizai.entity.body.RequestBodyType;
import demo.model.QueryForm;

/**
 * @author liaochongwei
 * @date 2021/2/7 11:21
 */
public interface ParamService {
    @Get("/test1")
    String test1();

    @Get("/test2")
    String test2(@Param String p1, @Param String p2);

    @Post(value = "/test3")
    String test3(@Param QueryForm<String> p1, @Param String p2);

    @Post(value = "/test4", bodyType = RequestBodyType.NONE)
    String test4(@Param QueryForm<String> p1, @Param String p2);
}

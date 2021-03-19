package demo.service;

import com.github.vizaizai.annotation.Get;
import com.github.vizaizai.annotation.Param;
import com.github.vizaizai.annotation.Post;
import com.github.vizaizai.entity.body.RequestBodyType;
import demo.model.QueryForm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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


    @Get("/test5")
    String test5(Map<String,Integer> p1);

    @Get("/test6")
    String test6(List<String> p1);

    @Get("/test7")
    String test7(Integer[] p1);

    @Get("/test8")
    <T> String test8(T p1);

    @Get("/test9")
    <T> CompletableFuture test9(T... p1);

}

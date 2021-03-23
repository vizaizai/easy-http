package demo.service;

import com.github.vizaizai.annotation.Get;
import com.github.vizaizai.annotation.Param;
import com.github.vizaizai.annotation.Post;
import com.github.vizaizai.annotation.Var;
import com.github.vizaizai.entity.body.RequestBodyType;
import demo.model.Book1;
import demo.model.QueryForm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author liaochongwei
 * @date 2021/2/7 11:21
 */
public interface ParamService {
    @Get("/listAllBooks")
    String test1();

    @Get("/getBookById/{id}")
    String test2(@Var String id);

    @Get(value = "/listBooks")
    String test3(@Param String author, @Param String lang);

    @Get(value = "/listBooks")
    String test3_1(Map<String,String> params);

    @Get(value = "/listBooks")
    String test3_2(Book1 book1);

    @Get(value = "/listBooks/categories/{cid}/books")
    String test4(@Var String cid, @Param String author, @Param String lang);

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

package demo.service;

import com.github.vizaizai.annotation.*;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.entity.form.BodyContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import com.github.vizaizai.entity.form.FormData;
import com.github.vizaizai.hander.RequestHandler;
import com.github.vizaizai.interceptor.ErrorInterceptor;
import com.github.vizaizai.interceptor.LogInterceptor;
import demo.interceptor.RequestHeadersInterceptor;
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

    @Headers("key: 123123")
    @Get(value = "/listAllBooks",interceptors = {LogInterceptor.class, ErrorInterceptor.class, RequestHeadersInterceptor.class})
    String test1();

    @Get("/getBookById/{id}")
    String test2(@Var String id);

    @Get(value = "/listBooks")
    String test3(@Param String author, @Param String lang);

    @Get(value = "/listBooks", interceptors = LogInterceptor.class)
    String test3_1(Map<String,String> params);

    @Get(value = "/listBooks")
    String test3_2(Book1 book1);

    @Get(value = "/listBooks?author={author}&lang={lang}")
    String test3_3(@Var String author, @Var String lang);

    @Get(value = "/listBooks/categories/{cid}/books")
    String test4(@Var String cid, @Param String author, @Param String lang);

    @Post("/addBookUseForm")
    String test5(Book1 book1);

    @Post(value = "/addBookUseForm")
    String test5_1(Map<String,Object> book);

    @Post("/addBookUseJSON")
    String test6(@Body Book1 book1);

    @Post("/addBookUseJSON")
    String test6_1(@Body Map<String,Object> book);

    @Mapping(value = "/addBookUseJSON", httpMethod = HttpMethod.PATCH)
    @Headers("key: 1231231")
    String test6_2(@Body Map<String,Object> book);

    @Get("/listBookByIds")
    String test7(String[] ids);

    @Get("/listBookByIds")
    String test7_1(List<String> ids);

    @Get("/listBookByIds")
    String test7_2(String ...ids);

    @Post("/addBookUseFormData")
    String test8(@Body FormData formData);

    @Post("/upload/e-book/{id}")
    String test9(@Var String id, @Body BodyContent bodyContent);

    @Post("/upload/e-book")
    String test10(@Param String id1,@Param String id2,@Body Book1 book1);

    @Post("/upload/e-book")
    String test10_1(@Param Book1 book,@Body Book1 book1);
}

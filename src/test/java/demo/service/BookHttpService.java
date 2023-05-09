package demo.service;

import com.github.vizaizai.annotation.*;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.HttpResponse;
import demo.interceptor.ResultInterceptor;
import demo.model.ApiResult;
import demo.model.Book;
import demo.model.QueryForm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:27
 */
@Headers({"client: easy-http,okHttp","sign: 56c41d9e1142784770d2c8cd1049c9e3"})
public interface BookHttpService {

    @Post("/books")
    ApiResult<Void> addBook(@Body Book book);

    @Delete("/books/{id}")
    ApiResult<String> deleteBook(@Var("id") String id);

    @Delete("/books/body")
    ApiResult<String> deleteBookByBody(@Body String id);

    @Put("/books")
    ApiResult<Void> editBook(@Body Book book);

    @Get("/books")
    ApiResult<List<Book>> listAllBooks();

    @Get("/books/search")
    ApiResult<List<Book>> searchBooks(@Param("keyword") String keyword);

    @Get("/books")
    CompletableFuture<ApiResult<List<Book>>> foo();

    @Get(value = "/book/bar", retries = 1, interval = 10)
    String[] bar(@Param("ids") String[] ids, @Headers QueryForm headers);

    @Get(value = "/book/bar", retries = 1, interval = 10)
    String[] bar1(@Param("ids") List<String> ids);

    @Headers({"client: easy-http1,okHttp","sign: 56c41d9e1142784770d2sc8cd1049c9e3"})
    @Get(value = "/book/bar")
    String[] foo(QueryForm form, @Headers Map<String,String> headers);

    @Post(value = "/book/foo")
    HttpResponse foo1(@Body(wrapRoot = "foo") QueryForm<String> form);

    @Post(value = "/book/foo1?params={params}")
    HttpResponse foo1(@Var String params);

    @Mapping(value = "/management-center/jsd-management/opsAloneStoreAudit/qryByStoreId",
            httpMethod = HttpMethod.GET,
            interceptors = ResultInterceptor.class,
            retries = 5)
    CompletableFuture<Book> baidu(@Param("platformId") String platformId, @Param("storeId") String storeId);

    @Get(value = "/management-center/jsd-management/opsAloneStoreAudit/qryByStoreId")
    Book man(@Param("platformId") String platformId, @Param("storeId") String storeId);


    @Post("/books/batch")
    ApiResult<Void> addBooks(@Body List<Book> books);

    @Post("/user/login")
    String login(@Body Map<String,String> body);
}

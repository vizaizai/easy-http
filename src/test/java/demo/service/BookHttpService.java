package demo.service;

import com.github.vizaizai.annotation.*;
import com.github.vizaizai.model.HttpMethod;
import demo.interceptor.ResultInterceptor;
import demo.model.ApiResult;
import demo.model.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:27
 */
@Headers({"client: easy-http","sign: 56c41d9e1142784770d2c8cd1049c9e3"})
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
    ApiResult<List<Book>> searchBooks(@Query("keyword") String keyword);

    @Get("/books")
    CompletableFuture<ApiResult<List<Book>>> foo();

    @Get("/books/bar")
    String[] bar();

    @Mapping(value = "/management-center/jsd-management/opsAloneStoreAudit/qryByStoreId",
            httpMethod = HttpMethod.GET,
            interceptors = ResultInterceptor.class)
    CompletableFuture<String> baidu(@Query("platformId") String platformId, @Query("storeId") String storeId);
}

package demo.service;

import com.github.firelcw.annotation.*;
import demo.model.ApiResult;
import demo.model.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:27
 */
@Headers({"client: easy-http"})
public interface BookHttpService {

    @Post("/book")
    ApiResult<Void> addBook(@Body Book book);

    @Delete("/book/{id}")
    ApiResult<String> deleteBook(@Var("id") String id);

    @Put("/book")
    ApiResult<Void> editBook(@Body Book book);

    @Get("/book")
    ApiResult<List<Book>> listAllBooks();

    @Get("/book/search")
    ApiResult<List<Book>> searchBooks(@Query("keyword") String keyword);

    @Get("/book")
    CompletableFuture<ApiResult<List<Book>>> foo();

    @Get("/book/bar")
    String[] bar();

    @Get
    String baidu();
}

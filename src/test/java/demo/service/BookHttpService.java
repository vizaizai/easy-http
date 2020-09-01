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

    @Post("/books")
    ApiResult<Void> addBook(@Body Book book);

    @Delete("/books/{id}")
    ApiResult<String> deleteBook(@Var("id") String id);

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
}

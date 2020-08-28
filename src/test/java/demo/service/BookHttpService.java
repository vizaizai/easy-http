package demo.service;

import com.github.firelcw.annotation.*;
import com.github.firelcw.model.ContentType;
import demo.model.ApiResult;
import demo.model.Book;

import java.util.List;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:27
 */
public interface BookHttpService {

    @Get("/books/{id}")
    ApiResult<Book> getBookById(@Var("id") String id);

    @Get("/books?author={author}")
    ApiResult<List<Book>> listBooksByAuthor(@Var("author") String author);

    @Get("/books/list/get")
    ApiResult<List<Book>> listBooks();

    @Post(value = "/books/getByAuthor")
    ApiResult<List<Book>> listBooksByAuthor(Map<String, String> params,@Body Book book);

    @Post("/books")
    void addBook(@Body Book book);

    @Delete("/books/{id}")
    ApiResult<Void> deleteBookById(@Var("id") String id);

    @Put("/books")
    ApiResult<Void> editBook(@Body Book book);


    @Post("/books")
    ApiResult<Void> addBook(@Body Book book, @Headers Map<String, String> headers);

}

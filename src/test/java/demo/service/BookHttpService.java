package demo.service;

import com.github.firelcw.annotation.*;
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

    @Get("/books")
    ApiResult<List<Book>> listBooksByAuthor(@Params Map<String, String> params);

    @Post("/books")
    void addBook(@Data Book book);

    @Delete("/books/{id}")
    ApiResult<Void> deleteBookById(@Var("id") String id);

    @Put("/books")
    ApiResult<Void> editBook(@Data Book book);


    @Post("/books")
    ApiResult<Void> addBook(@Headers Map<String, String> headers);

}

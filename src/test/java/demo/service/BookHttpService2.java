package demo.service;


import com.github.vizaizai.annotation.Get;
import com.github.vizaizai.annotation.Var;
import demo.model.Book;

import java.util.List;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:27
 */
public interface BookHttpService2 {

    @Get("/books/{id}")
    Book getBookById(@Var("id") String id);

    @Get("/books?author={author}")
    List<Book>  listBooksByAuthor(@Var("author") String author);
}

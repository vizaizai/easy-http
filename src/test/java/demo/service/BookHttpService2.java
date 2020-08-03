package demo.service;


import com.github.firelcw.annotation.Get;
import com.github.firelcw.annotation.Var;
import demo.model.Book;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:27
 */
public interface BookHttpService2 {

    @Get("/books/{id}")
    Book getBookById(@Var("id") String id);
}

package demo.main;


import com.github.firelcw.EasyHttp;
import com.github.firelcw.client.ApacheHttpClient;
import com.github.firelcw.client.DefaultURLClient;
import com.github.firelcw.interceptor.ErrorInterceptor;
import com.github.firelcw.interceptor.TimeInterceptor;
import demo.model.ApiResult;
import demo.model.Book;
import demo.service.BookHttpService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:43
 */

public class BookServiceTest {
    BookHttpService bookHttpService;
    @Before
    public void init() {
        bookHttpService = EasyHttp.builder()
                                    .url("127.0.0.1:8888")
                                    .client(ApacheHttpClient.getInstance())
                                    .withInterceptor(new TimeInterceptor())
                                    .withInterceptor(new ErrorInterceptor())
                                    .build(BookHttpService.class);
    }


    @Test
    public void addBook() {
        Book book = new Book();
        book.setId(uuid());
        book.setPrice(BigDecimal.valueOf(69.40));
        book.setName("零基础学Python（全彩版）");
        book.setAuthor("明日科技(Mingri Soft)");
        book.setDescription("Python3全新升级！超20万读者认可的彩色书，从基本概念到完整项目开发，助您快速掌握Python编程。");


        ApiResult<Void> bookRet = bookHttpService.addBook(book);
        System.out.println(bookRet.getCode());
    }
    @Test
    public void listAllBooks() {
        ApiResult<List<Book>> listApiResult = bookHttpService.listAllBooks();
        System.out.println(listApiResult.getData());
    }

    @Test
    public void editBook() {
        Book book = new Book();
        book.setId("a443257960944e45aee4da013754bdf9");
        book.setPrice(BigDecimal.valueOf(66.40));
        book.setName(" Java从入门到精通（第5版）");
        book.setAuthor("明日科技");
        book.setDescription("297个应用实例+37个典型应用+30小时教学视频+海量开发资源库，丛书累计销量200多万册,是Java入门的好图书");

        ApiResult<Void> bookRet = bookHttpService.editBook(book);
        System.out.println(bookRet.getCode());
    }


    @Test
    public void deleteBook() {

        ApiResult<String> bookRet = bookHttpService.deleteBook("ba81da8f6ec54c4a80dedaedc6400719");
        System.out.println(bookRet.getData());
    }

    @Test
    public void searchBooks() {

        Map<String,String> headers = new HashMap<>();
        headers.put("client","easy-http");
        ApiResult<List<Book>> bookRet = bookHttpService.searchBooks("Java中文文档");
        bookRet.getData().forEach(System.out::println);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }

}

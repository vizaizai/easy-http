package demo.main;



import com.github.firelcw.client.EasyHttp;
import com.github.firelcw.interceptor.TimeInterceptor;
import demo.codec.CustomDecoder;
import demo.codec.CustomEncoder;
import demo.interceptor.ResultInterceptor;
import demo.model.ApiResult;
import demo.model.Book;
import demo.service.BookHttpService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:43
 */
public class BookMain {
    public static void main(String[] args) {
        BookMain main = new BookMain();
        //main.getBookById();
         main.listBooksByAuthor1();
        // main.listBooksByAuthor2();
        // main.addBook();
    }

    void getBookById() {
        BookHttpService bookHttpService = EasyHttp.builder()
                                                    .url("127.0.0.1:8888")
                                                    .withInterceptor(new TimeInterceptor())
                                                    .build(BookHttpService.class);
        ApiResult<Book> bookRet = bookHttpService.getBookById("166895");
        System.out.println(bookRet.getData().getName());

    }


    void listBooksByAuthor1(){
        BookHttpService bookHttpService = EasyHttp.builder()
                .url("127.0.0.1:8888")
                .build(BookHttpService.class);
        Map<String, String> params = new HashMap<>();
        params.put("author","tom");
        ApiResult<List<Book>> listApiResult = bookHttpService.listBooksByAuthor(params);
        System.out.println(listApiResult.getData().get(0).getName());
    }

    void listBooksByAuthor2(){
        BookHttpService bookHttpService = EasyHttp.builder()
                .url("127.0.0.1:8888")
                .build(BookHttpService.class);
        ApiResult<List<Book>> listApiResult = bookHttpService.listBooksByAuthor("tom");
        System.out.println(listApiResult.getData().get(0).getName());
    }

    void addBook(){
        BookHttpService bookHttpService = EasyHttp.builder()
                .url("127.0.0.1:8888")
                .withInterceptor(new ResultInterceptor())
                .withInterceptor(new TimeInterceptor())
                .build(BookHttpService.class);

        Book book = new Book();
        book.setName("name");
        book.setAuthor("author");
        bookHttpService.addBook(book);

        System.out.println(1);

    }

    void deleteBookById(){

    }

    void editBook(){

    }
}

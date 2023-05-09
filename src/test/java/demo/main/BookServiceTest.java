package demo.main;


import com.github.vizaizai.EasyHttp;
import com.github.vizaizai.client.ApacheHttpClient;
import com.github.vizaizai.client.DefaultURLClient;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.interceptor.ErrorInterceptor;
import com.github.vizaizai.interceptor.LogInterceptor;
import com.github.vizaizai.retry.DefaultRule;
import demo.interceptor.ResultInterceptor;
import demo.model.ApiResult;
import demo.model.Book;
import demo.model.QueryForm;
import demo.service.BookHttpService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author liaochongwei
 * @date 2020/8/3 10:43
 */

public class BookServiceTest {
    BookHttpService bookHttpService;
    @Before
    public void init() {
        bookHttpService = EasyHttp.builder()
                                    .url("127.0.0.1:1141")
                                    .client(ApacheHttpClient.getInstance())
                                    .withInterceptor(new LogInterceptor())
                                    .withInterceptor(new ErrorInterceptor())
                                    .retryable(3,1000, new DefaultRule())
                                    .build(BookHttpService.class);
    }


    @Test
    public void addBook() {
        Book book = new Book();
        book.setId(uuid());
        book.setPrice(BigDecimal.valueOf(17.40));
        book.setName("零基础学Python（全彩版）2");
        book.setAuthor("明日科技(Mingri Soft)2");
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
        book.setPrice(BigDecimal.valueOf(2.40));
        book.setName(" Java从入门到精通（第5版）");
        book.setAuthor("明日科技");
        book.setDescription("297个应用实例+37个典型应用+30小时教学视频+海量开发资源库，丛书累计销量200多万册,是Java入门的好图书");

        ApiResult<Void> bookRet = bookHttpService.editBook(book);
        System.out.println(bookRet.getCode());
    }


    @Test
    public void deleteBook() {

        //bookHttpService.deleteBook("47df864121ac476093105e58b3ce2ec2");
        ApiResult<String> apiResult = bookHttpService.deleteBookByBody("47df864121ac476093105e58b3ce2ec2");
        System.out.println(apiResult.getData());
    }

    @Test
    public void searchBooks() {

        Map<String,String> headers = new HashMap<>();
        headers.put("client","easy-http");
        ApiResult<List<Book>> bookRet = bookHttpService.searchBooks("Java中文文档");
        bookRet.getData().forEach(System.out::println);
    }


    @Test
    public void demo() {

        CompletableFuture<ApiResult<List<Book>>> foo = bookHttpService.foo();
        foo.thenAccept(e->System.out.println(e.getData()))
           .thenRun(()->System.out.println("异步请求执行完毕"));
        System.out.println("异步");
        foo.join();
    }

    @Test
    public void bar(){
        QueryForm form = new QueryForm();
        form.setIds(Arrays.asList("123","555","lololo"));
        String[] bar = bookHttpService.bar(new String[]{"1","2"}, form);
        System.out.println(JSON.toJSONString(bar));
    }

    @Test
    public void bar1(){
        String[] bar = bookHttpService.bar1(Arrays.asList("123","111"));
        System.out.println(JSON.toJSONString(bar));
    }

    @Test
    public void foo() {
        QueryForm<String> form = new QueryForm<>();
        form.setIds(Arrays.asList("123","555","lololo"));
        form.setTt("123123");
        Map<String,String> h = new HashMap<>();
        h.put("HeaderName","123123");
        HttpResponse response = bookHttpService.foo1(form);
        System.out.println(response.isOk());
    }

    @Test
    public void foo1(){
        bookHttpService.foo1("你好");
    }

    @Test
    public void man() {
        TimeConsuming.mark("jdk-create");
        bookHttpService = EasyHttp.builder()
                .url("http://10.10.11.107:25068/inner")
                .client(ApacheHttpClient.getInstance())
                .withInterceptor(new LogInterceptor())
                .withInterceptor(new ErrorInterceptor())
                .withInterceptor(new ResultInterceptor())
                .build(BookHttpService.class);
        TimeConsuming.printMS("jdk-create");

        long total = 0;
        int n = 10;
        for (int i = 0; i < n; i++) {
            //TimeLooper.sleep(3000);
            long time1= System.currentTimeMillis();
            Book r = bookHttpService.man("dsy_Wlep4Af6LPQf","1290478984305881090");
            long time = System.currentTimeMillis() - time1;
            System.out.println("执行时间:" + time);
            System.out.println(r.getId());
            total = total + time;
        }

        System.out.println("平均执行时间:" + total*1.0 / n);



    }

    @Test
    public void baidu(){
        bookHttpService = EasyHttp.builder()
                                    .url("http://10.10.11.107:25068/inner")
                                    .client(DefaultURLClient.getInstance())
                                    .withInterceptor(new LogInterceptor())
                                    .withInterceptor(new ErrorInterceptor())
                                    .build(BookHttpService.class);
        CompletableFuture<Book> s =  bookHttpService.baidu("dsy_Wlep4Af6LPQf","1290478984305881090");

        s.thenAccept(e->System.out.println(e))
                .thenRun(()->System.out.println("异步请求执行完毕"));
        System.out.println("异步");
        s.join();
        System.out.println(s);

    }

    @Test
    public void addBooks() {
        Book book = new Book();
        book.setId(uuid());
        book.setPrice(BigDecimal.valueOf(17.40));
        book.setName("零基础学Python（全彩版）2");
        book.setAuthor("明日科技(Mingri Soft)2");
        book.setDescription("Python3全新升级！超20万读者认可的彩色书，从基本概念到完整项目开发，助您快速掌握Python编程。");

        ApiResult<Void> bookRet = bookHttpService.addBooks(Arrays.asList(book));
        System.out.println(bookRet.getCode());
    }

    @Test
    public void login() {
        Map<String, String> body = new HashMap<>();
        body.put("userName","admin");
        body.put("password","123456");
        String  ret = bookHttpService.login(body);
        System.out.println(ret);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }

}

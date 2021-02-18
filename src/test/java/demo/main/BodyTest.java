package demo.main;

import com.github.vizaizai.EasyHttp;
import com.github.vizaizai.client.ApacheHttpClient;
import com.github.vizaizai.client.DefaultURLClient;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.form.FileContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import com.github.vizaizai.entity.form.InputStreamContent;
import com.github.vizaizai.entity.form.StringContent;
import com.github.vizaizai.interceptor.LogInterceptor;
import com.github.vizaizai.util.Utils;
import demo.model.Book;
import demo.service.BodyService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

/**
 * @author liaochongwei
 * @date 2021/2/7 11:22
 */
public class BodyTest {

    private BodyService bodyService;
    @Before
    public void init() {
        bodyService = EasyHttp.builder()
                .url("127.0.0.1:8888")
                .client(DefaultURLClient.getInstance())
                .withInterceptor(new LogInterceptor())
                .build(BodyService.class);
    }
    @Test
    public void test1() {
        HttpResponse httpResponse = bodyService.test1();
        System.out.println(httpResponse.getMessage());
    }
    @Test
    public void test2() {
        Book book = new Book();
        book.setId("231434211125444111");
        book.setPrice(BigDecimal.valueOf(231));
        book.setName("吸收");
        book.setAuthor("校区");
        book.setDescription("啦啦啦");

        System.out.println(bodyService.test2(book));
    }

    @Test
    public void test3() {
        System.out.println(bodyService.test3(FileContent.of(new File("C:\\Users\\dell\\Desktop\\商品订单20210201.xlsx"))));
    }
    @Test
    public void test4() {

        FormBodyParts formBodyParts = new FormBodyParts();
        formBodyParts.add("name1", StringContent.of("王小锤", Utils.UTF_8));
        formBodyParts.add("name1", StringContent.of("王大锤", Utils.UTF_8));
        formBodyParts.add("file1", FileContent.of(new File("C:\\Users\\dell\\Desktop\\商品订单20210201.xlsx"), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        try {
            formBodyParts.add("file1", InputStreamContent.of(new FileInputStream("C:\\Users\\dell\\Desktop\\1.txt")));
        }catch (Exception e){}

        System.out.println(bodyService.test4(formBodyParts));
    }

}

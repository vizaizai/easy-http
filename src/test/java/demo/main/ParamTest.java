package demo.main;

import com.github.vizaizai.EasyHttp;
import com.github.vizaizai.client.ApacheHttpClient;
import com.github.vizaizai.client.DefaultURLClient;
import com.github.vizaizai.entity.form.FileContent;
import com.github.vizaizai.entity.form.FormData;
import com.github.vizaizai.interceptor.ErrorInterceptor;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.interceptor.InterceptorGenerator;
import com.github.vizaizai.interceptor.LogInterceptor;
import demo.interceptor.ResultInterceptor;
import demo.model.Book1;
import demo.service.ParamService;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2021/2/7 11:22
 */
public class ParamTest {

    private ParamService paramService;
    @Before
    public void init() {
        paramService = EasyHttp.builder()
                .url("127.0.0.1:8888")
                //.client(DefaultURLClient.getInstance())
                .client(ApacheHttpClient.getInstance())
                .withInterceptor(new ErrorInterceptor())
                .withInterceptor(new LogInterceptor("【","】"))
                .build(ParamService.class);
    }
    @Test
    public void test1() {
        long s = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            paramService.test1();
        }
        System.out.println("总耗时：" + (System.currentTimeMillis() - s)  + "ms");
    }
    @Test
    public void test2() {
        System.out.println(paramService.test2("84669922357412893"));
    }
    @Test
    public void test3() {
        System.out.println(paramService.test3("吴承恩", "chinese"));

        Map<String,String> map = new HashMap<>();
        map.put("author","吴承恩");
        map.put("lang","chinese");
        System.out.println(paramService.test3_1(map));

        Book1 book = new Book1();
        book.setAuthor("吴承恩");
        book.setLang("chinese");
        System.out.println(paramService.test3_2(book));

        System.out.println(paramService.test3_3("吴承恩","chinese"));

    }
    @Test
    public void test4() {
        System.out.println(paramService.test4("23","西游记", "chinese"));
    }

    @Test
    public void test5() {
        Book1 book = new Book1();
        book.setAuthor("吴承恩");
        book.setLang("chinese");
        book.setName("西游记");
        System.out.println(paramService.test5(book));

        Map<String,Object> map = new HashMap<>();
        map.put("author","吴承恩");
        map.put("name","西游记");
        map.put("lang","chinese");
        System.out.println(paramService.test5_1(map));
        System.out.println(paramService.test5_1(map));
        System.out.println(paramService.test5_1(map));
        System.out.println(paramService.test5_1(map));
    }


    @Test
    public void test6() {
//        Book1 book = new Book1();
//        book.setAuthor("吴承恩");
//        book.setLang("chinese");
//        book.setName("西游记");
//        System.out.println(paramService.test6(book));
//
        Map<String,Object> map = new HashMap<>();
        map.put("author","吴承恩");
        map.put("name","西游记");
        map.put("lang","chinese");
//        System.out.println(paramService.test6_1(map));
        System.out.println(paramService.test6_2(map));
        System.out.println(paramService.test6_2(map));
    }

    @Test
    public void test7() {
        System.out.println(paramService.test7(new String[]{"1","2"}));

        System.out.println(paramService.test7_1(Arrays.asList("1","2")));

        System.out.println(paramService.test7_2("1","2"));
    }

    @Test
    public void test8() throws FileNotFoundException {
        FormData formData = new FormData();
        formData.addText("author","吴承恩");
        formData.addText("name","西游记");
        formData.addText("lang","chinese");
        formData.addFile("files", new File("C:\\Users\\dell\\Desktop\\logo.png"));
        formData.addInputStream("files", new FileInputStream("C:\\Users\\dell\\Desktop\\logo.png"));
//        formData.addFile("files", new File("C:\\Users\\dell\\Desktop\\Dingtalk_20210317145859.jpg"));
//        formData.addFile("files", new File("C:\\Users\\dell\\Desktop\\jsd_pro_back.rar"));
        System.out.println(paramService.test8(formData));
    }

    @Test
    public void test9() {
        System.out.println(paramService.test9("123",FileContent.of(new File("C:\\Users\\dell\\Desktop\\jsd_pro_back.rar"))));
    }

    @Test
    public void test10() {
        Book1 book1 = new Book1();
        book1.setName("123123");
        //System.out.println(paramService.test10("123","456",book1));

        Book1 book = new Book1();
        book.setId("egeg");
        book.setLang("www");
        book.setName("ss");
        book.setAuthor("wfg");

        System.out.println(paramService.test10_1(book,book1));
    }

}

package demo.main;

import com.github.vizaizai.EasyHttp;
import com.github.vizaizai.client.ApacheHttpClient;
import com.github.vizaizai.client.DefaultURLClient;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.form.FileContent;
import com.github.vizaizai.entity.form.FormData;
import com.github.vizaizai.hander.mapping.PathConverter;
import com.github.vizaizai.interceptor.ErrorInterceptor;
import com.github.vizaizai.interceptor.LogInterceptor;
import demo.model.ApiResult;
import demo.model.Book1;
import demo.service.ParamService;
import demo.service.ReturnService;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2021/2/7 11:22
 */
public class ReturnTest {

    private ReturnService returnService;
    @Before
    public void init() {
        PathConverter pathConverter = new PathConverter() {
            @Override
            public String get(String value) {
                return value + "xixi";
            }
        };
        returnService = EasyHttp.builder()
                .url("127.0.0.1:8888")
                .client(ApacheHttpClient.getInstance())
                //.client(ApacheHttpClient.getInstance())
                .withInterceptor(new ErrorInterceptor())
                .withInterceptor(new LogInterceptor())
                //.pathConverter(pathConverter)
                .build(ReturnService.class);
    }
    @Test
    public void test1() {
        HttpResponse httpResponse = returnService.test1();
        System.out.println(httpResponse.isOk());
    }

    @Test
    public void test2() {
        ApiResult<List<Book1>> result = returnService.test2();
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void test3() {
        System.out.println(returnService.testInt_1());
        System.out.println(returnService.testInt_2());
        System.out.println(returnService.testInt_3());
        System.out.println(returnService.testInt_4());
        System.out.println(returnService.testInt_5());
    }
    @Test
    public void test4() {
        System.out.println(returnService.testDouble_1());
        System.out.println(returnService.testDouble_2());
        System.out.println(returnService.testDouble_3());
        System.out.println(returnService.testDouble_5());
        returnService.testDouble_4();
    }
}

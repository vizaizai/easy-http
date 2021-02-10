package demo.main;

import com.github.vizaizai.EasyHttp;
import com.github.vizaizai.client.DefaultURLClient;
import demo.model.QueryForm;
import demo.service.ParamService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
                .client(DefaultURLClient.getInstance())
                .build(ParamService.class);
    }
    @Test
    public void test1() {
        System.out.println(paramService.test1());
    }
    @Test
    public void test2() {
        System.out.println(paramService.test2("123",null));
    }
    @Test
    public void test3() {
        QueryForm<String> queryForm = new QueryForm<>();
        queryForm.setTt("123123");
        queryForm.setIds(Arrays.asList(null,"222","333"));
        System.out.println(paramService.test3(queryForm, "balabala"));
    }
}

package demo.service;

import com.github.vizaizai.annotation.Get;
import com.github.vizaizai.entity.HttpResponse;
import demo.model.ApiResult;
import demo.model.Book1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author liaochongwei
 * @date 2021/3/29 16:11
 */
public interface ReturnService {

    @Get("/listAllBooks1")
    HttpResponse test1();

    @Get("/listAllBooks")
    ApiResult<List<Book1>> test2();

    @Get("/testInt")
    int testInt_1();

    @Get("/testInt")
    short testInt_2();

    @Get("/testInt")
    Integer testInt_3();

    @Get("/testInt")
    BigInteger testInt_4();

    @Get("/testInt")
    Number testInt_5();

    @Get("/testDouble")
    double testDouble_1();

    @Get("/testDouble")
    BigDecimal testDouble_2();

    @Get("/testDouble")
    Number testDouble_3();

    @Get("/testDouble")
    void testDouble_4();

    @Get("/testDouble")
    Double testDouble_5();
}

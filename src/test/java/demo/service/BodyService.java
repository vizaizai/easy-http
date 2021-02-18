package demo.service;

import com.github.vizaizai.annotation.Body;
import com.github.vizaizai.annotation.Get;
import com.github.vizaizai.annotation.Headers;
import com.github.vizaizai.annotation.Post;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.form.FileContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import demo.model.Book;

/**
 * @author liaochongwei
 * @date 2021/2/8 17:03
 */
public interface BodyService {
    @Get("https://pics7.baidu.com/feed/29381f30e924b899f2044a6cc4940c9d0b7bf6c0.jpeg?token=1d2879b52a7cc1d7fbc00fdc5f7c4b0d&s=6280DC0BF62F52AD70CDA9D3030010A3")
    HttpResponse test1();

    @Post("/test2")
    String test2(@Body Book book);

    @Post("/test3")
    String test3(@Body FileContent fileContent);

    @Post(value = "/test4")
    @Headers({"version: Android:10.0.0,123123","sign: 1156512313"})
    String test4(@Body FormBodyParts formBodyParts);
}

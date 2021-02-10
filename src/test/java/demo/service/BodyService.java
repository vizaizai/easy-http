package demo.service;

import com.github.vizaizai.annotation.Body;
import com.github.vizaizai.annotation.Post;
import com.github.vizaizai.entity.form.FileContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import demo.model.Book;

/**
 * @author liaochongwei
 * @date 2021/2/8 17:03
 */
public interface BodyService {
    @Post("/test1")
    String test1();

    @Post("/test2")
    String test2(@Body Book book);

    @Post("/test3")
    String test3(@Body FileContent fileContent);

    @Post("/test4")
    String test4(@Body FormBodyParts formBodyParts);
}

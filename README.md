#### easy-http

##### 快速开始

> easy-http是一个完全基于注解和接口的http客户端，为简化开发、提高效率而生。

##### 1. 特性

   + 注解简单： 遵循大家的命名习惯，@Body、@Query、@Var等注解见名之意。
   + 无侵入： 接口不需要继承。
   + 支持自定义编解码：默认已经内置了JSON编解码(返回参数支持泛型)，如需支持xml，可自定义。
   + 支持自定义拦截器：请求前，和请求后的拦截。拦截器可满足大部分业务需求，如：计算请求耗时，动态添加公共请求头，返回错误统一处理等等。

##### 2. 安装

   ``` xml
   <dependency>
     <groupId>com.github.firelcw</groupId>
     <artifactId>easy-http</artifactId>
     <version>1.6.0</version>
   </dependency>
   ```

spring-boot版本移步: [easy-http-boot-starter](https://github.com/firelcw/easy-http-spring)

##### 3. 使用

   首先定义一个接口:

   ``` java
   public interface BookHttpService {
       @Get("/books/{id}")
       ApiResult<Book> getBookById(@Var("id") String id);
   }    
   ```

   这是一个最简单的查询接口，然后通过接口构建执行

   ``` java
   BookHttpService bookHttpService = EasyHttp.builder()
       								.url("127.0.0.1:8888")
       								.build(BookHttpService.class);
   ApiResult<Book> bookRet = bookHttpService.getBookById("166895");
   System.out.println(bookRet.getData().getName());
   ```

   像类似@GET的注解还有@Post、@Put、@Delete，@Post、@Put可指定content-type，默认是application/json。

##### 4.方法参数注解

   + @var

     路径变量注解，用于替换路径上的变量，路径变量只能是简单参数类型，八大基本类型加String。

     ```java
     @Get("/books/{id}")
     ApiResult<Book> getBookById(@Var("id") String id);
     @Get("/books?author={author}")
     ApiResult<Book> getBookById(@Var("author") String author);
     ```

   + @Query

     被注解的参数是复杂类型，是参数的model或者map，因为它生成的是查询参数，会拼接到路径后面。缺省默认为查询参数。

     ``` java
     @Get("/books")
     ApiResult<List<Book>> listBooksByAuthor(@Query Map<String, String> params);
     // 和上面效果一样
     @Get("/books")
     ApiResult<List<Book>> listBooksByAuthor(Map<String, String> params);
     ```

   + @Body

     body参数，被注解的参数会根据编码器处理放到请求body里面。

     ``` java
     @Post("/books")
     void addBook(@Body Book book);
     ```

   + @Headers

     被注解的参数为请求头。

     ``` java
      @Post("/books")
      ApiResult<Void> addBook(@Body Book book,  @Headers Map<String, String> headers);
     ```

##### 5. 自定义编码器

> 编码器： 将参数对象解析成http的相关请求参数

编写自定义编码器类`CustomEncoder`实现`Encoder`接口

``` java
public class CustomEncoder implements Encoder {
     /**
     * 将对象转化成成Map<String,String> 用于编码@Query和@Headers
     * @param object 待编码对象
     * @return map
     */
    @Override
    public Map<String, String> encodeMap(Object object) {
        return null;
    }
	 /**
     * 将对象转化成string，用于编码 @Body注解的对象（默认是解析成json字符串）
     * @param object
     * @return string
     */
    @Override
    public String encodeString(Object object) {
        return null;
    }
}
```

在构建对象是加入`CustomEncoder`

``` java
BookHttpService bookHttpService = EasyHttp.builder()
                                            .url("127.0.0.1:8888")
                                            .encoder(new CustomEncoder())
                                            .build(BookHttpService.class);
```

##### 6. 自定义解码器

> 将返回体解析成对象

编写自定义编码器类`CustomDecoder`实现`Decoder`接口

``` java
public class CustomDecoder implements Decoder {
     /**
     * 响应解码(默认使用fastjon解码)
     * @param response 响应参数
     * @param type 返回值类型
     * @return Object
     */
    @Override
    public Object decode(HttpResponse response, Type type) {
        return null;
    }
}
```
在构建对象是加入`CustomDecoder`

``` java
BookHttpService bookHttpService = EasyHttp.builder()
                                        .url("127.0.0.1:8888")
                                        .encoder(new CustomEncoder())
                                        .decoder(new CustomDecoder())
                                        .build(BookHttpService.class);
```

6. 自定义拦截器

   > 在请求发出前和请求响应后进行拦截

编写自定义编码器类`ResultInterceptor`实现`HttpInterceptor`接口

``` java
public class ResultInterceptor implements HttpInterceptor {
    @Override
    public boolean preHandle(HttpRequest request, HttpRequestConfig config) {
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        if (!response.isSuccess()) {
            throw new EasyHttpException("请求错误~");
        }
        if (StringUtils.isBlank(response.getBody())) {
           return;
        }
        JSONObject retJson = JSON.parseObject(response.getBody());
        // 假设业务code：200 为操作成功
        if (retJson.getInteger("code") == 200) {
            // 覆盖包含公共信息的json
            response.setBody(retJson.getString("data"));
        }
    }

    @Override
    public int order() {
        return 4;
    }
    @Override
    public List<ExcludePath> excludes() {
        return Arrays.asList(ExcludePath.instance("/books/**", HttpMethod.DELETE, HttpMethod.POST));
    }
}
```

`preHandle` 请求前拦截，返回true则通过，反之则被拦截。`postHandle` 响应后拦截。`order`定义拦截器的顺序，值越小，越靠前。`excludes`定义需要排除使用拦截器的路径列表。这里`ResultInterceptor`对统一的返回作了处理。不仅判断了请求和业务是否成功，并且还统一包装了返回体，去掉了通过返回部分，具体根据业务定制。

在构建对象是加入拦截器列表

``` java
        BookHttpService bookHttpService = EasyHttp.builder()
                                                    .url("127.0.0.1:8888")
                                                    .encoder(new CustomEncoder())
                                                    .decoder(new CustomDecoder())
                                                    .withInterceptor(new ResultInterceptor())
                                                    .withInterceptor(new TimeInterceptor())
                                                    .build(BookHttpService.class);
```



#### 联系作者

如您有好的建议，或者有任何疑问，可联系我

- QQ: 274550900
- Email: liaochongwei666@163.com
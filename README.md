#### easy-http

##### 快速开始

> easy-http是一个完全基于注解和接口的http客户端，为简化开发、提高效率而生。

##### 1. 特性

   + 注解简单： 遵循大众的命名习惯，@Body、@Param、@Var等注解见名之意。
   + 无侵入： 接口不需要继承。
   + 请求体支持x-www-form-urlencoded、form-data（包含文件）、binary(二进制)、raw(json、xml等文本)
   + 多客户端实现: 底层支持多种客户端，默认已实现Java原生URL和HttpClient，也可自定义客户端。
   + 支持异步请求。
   + 支持自定义编解码：默认已经内置了JSON编解码(返回参数支持泛型)，如需支持xml，可自定义。
   + 支持自定义拦截器：请求前，和请求后的拦截。拦截器可满足大部分业务需求，如：计算请求耗时，动态添加公共请求头，返回错误统一处理等等。
   + 支持请求重试，可自定义重试触发规则。
   + 提供spring-boot版本，使用更简单。

##### 2. 安装

Java版本: 最低 `8`

   ``` xml
   <dependency>
     <groupId>com.github.vizaizai</groupId>
     <artifactId>easy-http</artifactId>
     <version>最新版</version>
   </dependency>
   ```

spring-boot版本移步: [easy-http-boot-starter](https://github.com/vizaizai/easy-http-spring)

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

   像类似@GET的请求方法注解还有@Post、@Put、@Delete。

##### 4.方法参数注解

> 基础类型定义：基本数据类型+ 包装类型 + void + Void +String + Number + Number实现类

   + @var

     路径变量注解，用于替换路径上的变量，路径变量只能是基础类型参数。

     ```java
     @Get("/books/{id}")
     ApiResult<Book> getBookById(@Var("id") String id);
     @Get("/books?author={author}")
     ApiResult<Book> getBookById(@Var("author") String author);
     ```

   + @Param

     被注解的参数可以是基础类型、数组、列表、Map 和 JavaBean，注意：

     	- 如果被注解的参数是基础类型，那么value查询参数的key。
     	- 如没有指定value并且项目编译时设置了`-parameters`，则key会取被注解字段的名称，否则为arg1~n。
     	- 当一个方法参数上没有任何注解时默认为@Param。
     	- 当请求方式是`GET`时，@Param参数固定为查询参数(拼接url)，否则默认为`x-www-form-urlencoded`(表单)

     ``` java
     @Get("/books")
     ApiResult<List<Book>> listBooksByAuthor1(@Param("author") String author);
     
     // 设置了-parameters
     @Get("/books")
     ApiResult<List<Book>> listBooksByAuthor2(@Param String author);
     
     @Get("/books")
     ApiResult<List<Book>> listBooksByAuthor(@Param Map<String, String> params);
     
     @Get("/books")
     ApiResult<List<Book>> listBooksByAuthor(Map<String, String> params);
     
     @Get("/books")
     ApiResult<List<Book>> listBooksByIds(@Param("ids") List<String> ids);
     
     @Post("/addBookUseForm")
     String test5(@Param Book book); // 请求体x-www-form-urlencoded
     
     @POST(value="/addBookUseForm", bodyType = RequestBodyType.NONE) // 指定无请求体，参数拼接到url上面
     String test5_1(@Param Book book); // 拼接url，如 /addBookUseForm?name=easyhttp&author=lcw
     ```

   + @Body

     包含请求体的内容。

     - 当被注解的参数类型是`FormData`, 则请求体为`multipart/form-data`。`FormData`可添加文本和文件。
     - 当被注解参数类型为`BodyContent`的实现类时，请求体为所有二进制文件。`BodyContent`默认已实现`FileContent`、`InputStreamContent`和`StringContent`。
     - 否则被注解的参数会根据编码器处理放到请求体里, 如果被注解的参数是基础类型，则直接转化成字符传后放入请求体中。

``` java
@Post("/books")
void addBook(@Body Book book);

@Post("/addBookUseJSON")
String test6(@Body Book1 book1);

@Post("/addBookUseJSON")
String test6_1(@Body Map<String,Object> book);

@Post("/addBookUseJSON")
String test6_2(@Body String content);


@Post("/addBookUseFormData")
String test8(@Body FormData formData);
// form-data 的使用
FormData formData = new FormData();
formData.addText("author","吴承恩");
formData.addText("name","西游记");
formData.addText("lang","chinese");
formData.addFile("files", new File("C:\\Users\\dell\\Desktop\\logo.png"));
formData.addFile("files", new File("C:\\Users\\dell\\Desktop\\Dingtalk_20210317145859.jpg"));
formData.addFile("files", new File("C:\\Users\\dell\\Desktop\\jsd_pro_back.rar"));
paramService.test8(formData);

@Post("/upload/e-book/{id}")
String test9(@Var String id, @Body BodyContent bodyContent);
// 二进制文件的使用
paramService.test9("123", FileContent.of(new File("C:\\Users\\dell\\Desktop\\jsd_pro_back.rar")));

```

   + @Headers

     请求头注解，可用于接口、方法和方法参数上，当用于接口和方法需指定value, 格式key:  value (冒号后面有和空格)

     ``` java
     @Headers({"clent: Easy-http"})
     @Post("/books")
     ApiResult<Void> addBook(@Body Book book,  @Headers Map<String, String> headers);
     ```

##### 5. 自定义编码器

> 编码器： 将参数对象解析成http的相关请求参数

编写自定义编码器类`CustomEncoder`实现`Encoder`接口

``` java
public class CustomEncoder implements Encoder {
    /**
     * 将对象转化成Body（如果参数类型本身为基础类型，则直接转化成string），用于编码 @Body注解的对象（默认jackson编码）
     * @param object 待编码对象
     * @param bodyType 请求体对象类型
     * @return Body
     */
    @Override
    public Body encode(Object object, Type bodyType) {
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
     * 响应解码(默认实现jackson解码)
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
##### 7. 自定义拦截器

   > 在请求发出前和请求响应后进行拦截

编写自定义编码器类`ResultInterceptor`实现`HttpInterceptor`接口，抽离data部分返回

``` java
public class ResultInterceptor implements HttpInterceptor {
    @Override
    public boolean preHandle(HttpRequest request) {
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
         if (!response.isOk()) {
            throw new EasyHttpException("请求错误~");
        }
        if (response.getBody() == null) {
           return;
        }

        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ApiResult.class,
                    mapper.getTypeFactory().constructType(response.getReturnType()));
            ApiResult<Object> bizRet = mapper.readValue(response.getBody().asInputStream(), javaType);

            // 假设业务code：200 为操作成功
            if (bizRet.getCode() == 200) {
                if (bizRet.getData() != null) {
                    // 取data作为返回值
                    response.setReturnObject(bizRet.getData());
                    return;
                }
            }
            response.setReturnObject(null);
        }catch (Exception e) {
            throw new EasyHttpException(e);
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
##### 8. 切换客户端

   ```java
    EasyHttp.builder()
            .url("127.0.0.1:8888")
            .client(ApacheHttpClient.getInstance())
            .build(BookHttpService.class);
   ```
##### 9. 异步请求

将方法的返回参数设为`Future`或者`CompletableFuture` , 就可以轻松实现异步。

```java
// 接口方法
@Get("/books")
CompletableFuture<ApiResult<List<Book>>> foo();

// 执行异步请求
CompletableFuture<ApiResult<List<Book>>> foo = bookHttpService.foo();
foo.thenAccept(e->System.out.println(e.getData()))
   .thenRun(()->System.out.println("异步请求执行完毕"));
System.out.println("异步");
foo.join();
```

> 有关Java8`CompletableFuture`的更多操作，请前往 [Java8 CompletableFuture](https://blog.csdn.net/lcw158852/article/details/107981506)

##### 10. 重试

可以全局开启重试，也可以在请求方式注解上针对每一个请求设置重试。

触发规则：默认规则为`当请求方式为GET并且HTTP状态码为5XX时`或者`当连接超时时`,支持自定义触发规则 `retryable(Integer retries, Integer interval, RetryTrigger retryTrigger)`, 实现RetryTrigger接口传入即可。

``` java
EasyHttp.builder()
        .url("127.0.0.1:8888")
        .client(DefaultURLClient.getInstance())
        .retryable(3,1000,new DefaultRule())
        .build(BookHttpService.class);
```

##### 11. 文件下载

easyHttp支持直接返回`HttpResponse`类型，可以拿到响应字节流和响应头。

#### 联系作者

如您有好的建议，或者有任何疑问，可联系我

- QQ: 274550900
- Email: liaochongwei666@163.com
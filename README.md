# easy-http
一个基于注解和接口的http客户端，使用更简单，更方便

##### 示例
``` java 
// 接口
public interface DemoHttpService1 {

    @Get("/goods?storeName={name}")
    String get(@Var("name") String name);

    @Get("/goods")
    String get(Map<String,Object> params);

    @Get("/goods")
    String getHeaders(@Headers Map<String,Object> params);

    @Put
    String updateStatus(@Data Map<String,Object> data);
}

// 用法
DemoHttpService1 service1 = EasyHttp.builder()
                                    .url("http://127.0.0.1:21602")
                                    .withInterceptor(new TimeInterceptor())
                                    .withInterceptor(new ErrorInterceptor())
                                    .build(DemoHttpService1.class);

        Map<String,Object> params = new HashMap<>();
        params.put("page", 2);
        params.put("limit", 13);
        String ret = service1.getHeaders(params);

        Map<String,Object> data = new HashMap<>();
        data.put("id","1111");
        data.put("status",1);
        //String ret = service1.updateStatus(data);
        System.out.println(ret);
```
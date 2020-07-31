package com.lcw.demo;



import com.lcw.client.EasyHttp;
import com.lcw.interceptor.ErrorInterceptor;
import com.lcw.interceptor.TimeInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/7/31 12:57
 */
public class Demo1 {
    public static void main(String[] args) {

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

    }
}

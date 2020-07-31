package com.lcw.demo;

import com.lcw.annotation.*;

import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/7/31 12:57
 */
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

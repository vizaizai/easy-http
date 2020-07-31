package com.github.lcw.demo;

import com.github.annotation.*;
import com.github.lcw.annotation.*;

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

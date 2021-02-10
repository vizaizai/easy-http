package demo.interceptor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;
import demo.model.ApiResult;

/**
 * @author liaochongwei
 * @date 2020/8/3 13:46
 */
public class ResultInterceptor implements HttpInterceptor {
    private final ObjectMapper mapper;

    public ResultInterceptor() {
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
}

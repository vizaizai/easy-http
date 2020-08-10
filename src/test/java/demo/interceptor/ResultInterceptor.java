package demo.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.interceptor.HttpInterceptor;
import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liaochongwei
 * @date 2020/8/3 13:46
 */
public class ResultInterceptor implements HttpInterceptor {
    @Override
    public boolean preHandle(HttpRequest request, HttpRequestConfig config) {
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        if (!response.isOk()) {
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
}

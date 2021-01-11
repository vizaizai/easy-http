package demo.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liaochongwei
 * @date 2020/8/3 13:46
 */
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
//        if (StringUtils.isBlank(response.getBody())) {
//           return;
//        }
//        JSONObject ret = JSON.parseObject(response.getBody());
//        // 假设业务code：200 为操作成功
//        if (ret.getInteger("code") == 200) {
//            // 覆盖包含公共信息的json
//            JSONObject data = ret.getJSONObject("data");
//            if (data != null) {
//                response.setReturnObject(data.toJavaObject(response.getReturnType()));
//                return;
//            }
//        }
//        response.setReturnObject(null);

    }

    @Override
    public int order() {
        return 4;
    }
}

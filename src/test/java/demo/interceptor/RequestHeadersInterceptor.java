package demo.interceptor;


import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;

/**
 * 请求头拦截器
 * @author liaochongwei
 * @date 2020/7/31 13:43
 */
public class RequestHeadersInterceptor implements HttpInterceptor {

    @Override
    public boolean preHandle(HttpRequest request) {
        //request.addHeader("Client-X","api");
        // request.addHeader("platform","wx-mp");
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
    }

    @Override
    public int order() {
        return -1;
    }
}

package zs.xmx.retrofit.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   超时时间设置拦截器
 * @内容说明
 *
 */
public class DynamicTimeoutInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        String questUrl = request.url().toString();
//        boolean isCloseApi = questUrl.contains(PayApi.API_CLOSEORDER);
//        boolean isPollingApi = questUrl.contains(PayApi.API_POLLING);
//        if (isCloseApi) {
//            return chain.withConnectTimeout(TimeOut.CLOSE_CONNECT, TimeUnit.SECONDS)
//                    .withReadTimeout(TimeOut.CLOSE_READ, TimeUnit.SECONDS)
//                    .proceed(request);
//        } else if (isPollingApi) {
//            return chain.withReadTimeout(TimeOut.POLLING_READ, TimeUnit.SECONDS)
//                    .proceed(request);
//        }
        return chain.proceed(request);

    }

}
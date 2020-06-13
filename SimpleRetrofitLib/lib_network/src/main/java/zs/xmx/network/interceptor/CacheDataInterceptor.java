package zs.xmx.network.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述	  缓存拦截器
 * @内容说明   url作为key,将request.body()数据为value保存到数据库
 *
 */
public class CacheDataInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
       // CacheManager.save(request.url().toString(), request.body());
        return chain.proceed(request);
    }
}

package zs.xmx.retrofit.interceptor;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import zs.xmx.retrofit.constant.UrlConstant;


/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   拦截器  替换BaseUrl
 * @内容说明   TODO 后面改造方向,
 *
 *
 *
 */
public class BaseUrlInterceptor implements Interceptor {
    private final String DOMAIN_NAME = "DomainName";


    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取request
        Request request = chain.request();

        // 获取request的创建者builder
        Request.Builder builder = request.newBuilder();

        HttpUrl newBaseUrl;
        String domainName = obtainDomainNameFromHeaders(request);
        //有自定义请求头
        if (!TextUtils.isEmpty(domainName)) {
            // 如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader(DOMAIN_NAME);
            if (UrlConstant.BADIDU.equals(domainName)) {
                newBaseUrl = obtainNewBaseUrl(request, Objects.requireNonNull(HttpUrl.parse(UrlConstant.DEF_BAIDU_URL)));
            } else if (UrlConstant.DOUBAN.equals(domainName)) {
                newBaseUrl = obtainNewBaseUrl(request, Objects.requireNonNull(HttpUrl.parse(UrlConstant.DEF_DOUBAN_URL)));
            } else {
                //request.url() 获取Retrofit或okHttp默认设置的baseUrl
                newBaseUrl = obtainNewBaseUrl(request, request.url());
            }
            Log.i("Logger", "newFullUrl :  设置了请求头");
            // 然后返回一个response至此结束修改
            return chain.proceed(builder.url(newBaseUrl).build());
        } else {//没有自定义请求头(全局的)
            Log.i("Logger", "newFullUrl :  没有设置请求头");
            newBaseUrl = obtainNewBaseUrl(request, Objects.requireNonNull(HttpUrl.parse(UrlConstant.getDefGankUrl())));
        }
        return chain.proceed(builder.url(newBaseUrl).build());

    }

    /**
     * 重建新的HttpUrl，修改需要修改的url部分
     */
    private HttpUrl obtainNewBaseUrl(Request request, HttpUrl baseUrl) {
        HttpUrl oldHttpUrl = request.url();
        return oldHttpUrl.newBuilder()
                // 更换网络协议
                .scheme(baseUrl.scheme())
                // 更换主机名
                .host(baseUrl.host())
                // 更换端口 HTTP默认80,https默认443
                .port(baseUrl.port())
                .build();
    }

    /**
     * 获得对应的DomainName
     */
    private String obtainDomainNameFromHeaders(Request request) {
        List<String> headerValues = request.headers(DOMAIN_NAME);
        if (headerValues.size() == 0) {
            return null;
        }
        if (headerValues.size() > 1)
            throw new IllegalArgumentException("Only one DomainName in the headers");
        return request.header(DOMAIN_NAME);
    }


}

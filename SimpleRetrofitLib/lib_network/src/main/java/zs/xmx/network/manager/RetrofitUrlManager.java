package zs.xmx.network.manager;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   RetrofitUrlManager
 * @内容说明   用于我们可以动态对单个接口或者多个接口替换BaseUrl
 *
 */
public class RetrofitUrlManager {
    private static final String TAG = RetrofitUrlManager.class.getSimpleName();
    private final String DOMAIN_NAME = "DomainName";
    private final Interceptor mInterceptor;
    //使用Map对象将所有的BaseUrl对应的HttpUrl对象保存下来
    private final Map<String, HttpUrl> mDomainNameMap = new HashMap<>();

    private static class RetrofitUrlManagerHolder {
        private static final RetrofitUrlManager INSTANCE = new RetrofitUrlManager();
    }

    public static RetrofitUrlManager getInstance() {
        return RetrofitUrlManagerHolder.INSTANCE;
    }

    private RetrofitUrlManager() {
        this.mInterceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                return chain.proceed(processRequest(chain.request()));
            }
        };
    }

    /**
     * 处理Request
     */
    private Request processRequest(Request request) {
        // 获取request的创建者builder
        Request.Builder newBuilder = request.newBuilder();
        String domainName = obtainDomainNameFromHeaders(request);
        HttpUrl newBaseUrl;
        //todo
        return newBuilder.build();
    }

    /**
     * 将 {@link OkHttpClient.Builder} 传入, 配置一些本框架需要的参数
     */
    public OkHttpClient.Builder with(OkHttpClient.Builder builder) {
        checkNotNull(builder, "builder cannot be null");
        return builder
                .addInterceptor(mInterceptor);
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

    private HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new RuntimeException("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
        } else {
            return parseUrl;
        }
    }

    private <T> void checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }
}

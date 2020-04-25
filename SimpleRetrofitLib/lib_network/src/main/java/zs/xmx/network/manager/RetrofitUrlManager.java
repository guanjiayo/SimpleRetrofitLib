package zs.xmx.network.manager;

import android.text.TextUtils;
import android.util.Log;

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
import zs.xmx.network.urlparse.DefaultUrlParser;
import zs.xmx.network.urlparse.UrlParser;
import zs.xmx.network.utils.CheckUtils;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   RetrofitUrlManager
 * @内容说明   用于我们可以动态对单个接口或者多个接口替换BaseUrl
 *
 * @使用说明
 *
 * 1. 定义我们要替换baseUrl
 *    {"baidu","http://www.baidu.com"}
      RetrofitUrlManager.getInstance().putDomain(UrlConstant.BADIDU, UrlConstant.DEF_BAIDU_URL)

 * 2. 定义接口注解
 * @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + UrlConstant.DOMAIN_BAIDU)
    @GET("Article" + RetrofitUrlManager.DOMAIN_PATH_SIZE + 2)//替换到oldUrl后两段的写法
    fun getArticle(): Call<ApiResponse<Any?>>
 *
 *
 * RetrofitUrlManager.DOMAIN_PATH_SIZE + 2
 * 看自己需要加或不加都可以
 */
public class RetrofitUrlManager {
    private static final String TAG = RetrofitUrlManager.class.getSimpleName();
    private static final String DOMAIN_NAME = "Domain-Name";
    private static final String GLOBAL_DOMAIN_NAME = "GlobalDomainName";
    public static final String DOMAIN_NAME_HEADER = DOMAIN_NAME + ": ";
    private final Interceptor mInterceptor;
    private UrlParser mUrlParser;
    //使用Map对象将所有的BaseUrl对应的HttpUrl对象保存下来
    private final Map<String, HttpUrl> mDomainNameMap = new HashMap<>();

    /**
     * 如果在 Url 地址中加入此标识符, 意味着您想对此 Url 开启超级模式, 框架会将 '=' 后面的数字作为 PathSize, 来确认最终需要被超级模式替换的 BaseUrl
     */
    public static final String DOMAIN_PATH_SIZE = "#baseurl_path_size=";

    private static class RetrofitUrlManagerHolder {
        private static final RetrofitUrlManager INSTANCE = new RetrofitUrlManager();
    }

    public static RetrofitUrlManager getInstance() {
        return RetrofitUrlManagerHolder.INSTANCE;
    }

    private RetrofitUrlManager() {
        UrlParser urlParser = new DefaultUrlParser();
        urlParser.init(this);
        setUrlParser(urlParser);
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

        if (!TextUtils.isEmpty(domainName)) {//如果有自定义请求头
            newBaseUrl = fetchDomain(domainName);
            newBuilder.removeHeader(DOMAIN_NAME);
        } else {//没有自定义请求头
            newBaseUrl = getGlobalDomain();
        }


        if (null != newBaseUrl) {
            //默认配置的BaseUrl
            HttpUrl newUrl = mUrlParser.parseUrl(newBaseUrl, request.url());

            Log.d(TAG, "The new url is { " + newUrl.toString() + " }, old url is { " + request.url().toString() + " }");

            return newBuilder
                    .url(newUrl)
                    .build();
        }

        return newBuilder.build();
    }

    public void setUrlParser(UrlParser mUrlParser) {
        this.mUrlParser = mUrlParser;
    }

    /**
     * 将 {@link OkHttpClient.Builder} 传入, 配置一些本框架需要的参数
     */
    public OkHttpClient.Builder with(OkHttpClient.Builder builder) {
        CheckUtils.checkNotNull(builder, "builder cannot be null");
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

    /**
     * 取出对应 BaseUrl
     */
    public synchronized HttpUrl fetchDomain(String domainName) {
        CheckUtils.checkNotNull(domainName, "domainName cannot be null");
        return mDomainNameMap.get(domainName);
    }

    /**
     * 存放 Domain(BaseUrl) 的映射关系
     */
    public void putDomain(String domainName, String domainUrl) {
        CheckUtils.checkNotNull(domainName, "domainName cannot be null");
        CheckUtils.checkNotNull(domainUrl, "domainUrl cannot be null");
        synchronized (mDomainNameMap) {
            mDomainNameMap.put(domainName, CheckUtils.checkUrl(domainUrl));
        }
    }

    /**
     * 全局动态替换 BaseUrl, 优先级: Header中配置的 BaseUrl > 全局配置的 BaseUrl
     * 除了作为备用的 BaseUrl, 当您项目中只有一个 BaseUrl, 但需要动态切换
     * 这种方式不用在每个接口方法上加入 Header, 就能实现动态切换 BaseUrl
     *
     * @param globalDomain 全局 BaseUrl
     */
    public void setGlobalDomain(String globalDomain) {
        CheckUtils.checkNotNull(globalDomain, "globalDomain cannot be null");
        synchronized (mDomainNameMap) {
            mDomainNameMap.put(GLOBAL_DOMAIN_NAME, CheckUtils.checkUrl(globalDomain));
        }
    }


    /**
     * 获取全局 BaseUrl
     */
    public synchronized HttpUrl getGlobalDomain() {
        return mDomainNameMap.get(GLOBAL_DOMAIN_NAME);
    }

    /**
     * 移除全局 BaseUrl(还原成使用默认的)
     */
    public void removeGlobalDomain() {
        synchronized (mDomainNameMap) {
            mDomainNameMap.remove(GLOBAL_DOMAIN_NAME);
        }
    }


}

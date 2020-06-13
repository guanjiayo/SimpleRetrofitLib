package zs.xmx.network


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.cache.CacheInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import zs.xmx.network.config.ConfigKeys
import zs.xmx.network.config.NetWorkInit
import zs.xmx.network.interceptor.CacheDataInterceptor
import zs.xmx.network.utils.HttpsUtils
import zs.xmx.network.manager.RetrofitUrlManager
import java.util.*
import java.util.concurrent.TimeUnit


/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述	   Retrofit 工厂类
 * @使用说明
 *
 * NetWorkInit.newInstance(this).withNativeApiHost(BaseConstant.URL_BASE)
                .withTimeOut(50)
                .withInterceptor(initLogInterceptor())
                .withInterceptor(AddHeadInterceptor())
                .withCallAdapter(RxJava2CallAdapterFactory.create())
                .withConverter(GsonConverterFactory.create())
                .configure()
 *
 * RetrofitFactory.instance.create(CartApi::class.java).getCartList()
 *
 */
@Suppress("unused")
class RetrofitFactory private constructor() {

    private val baseUrl: String = NetWorkInit.getConfiguration<String>(ConfigKeys.NATIVE_API_HOST)
    private val timeOut: Long = NetWorkInit.getConfiguration<Long>(ConfigKeys.TIME_OUT) //连接超时时间
    private var callAdapter: CallAdapter.Factory? =
        NetWorkInit.getConfiguration<CallAdapter.Factory>(ConfigKeys.CALL_ADAPTER) //callAdapter
    private val interceptors: ArrayList<Interceptor> =
        NetWorkInit.getConfiguration<ArrayList<Interceptor>>(ConfigKeys.INTERCEPTOR)
    private val converters: ArrayList<Converter.Factory> =
        NetWorkInit.getConfiguration<ArrayList<Converter.Factory>>(ConfigKeys.CONVERTER)
    private val retrofit: Retrofit

    /*
        单例实现
     */
    companion object {
        @JvmStatic
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    init {
        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        retrofitBuilder.baseUrl(baseUrl)
        retrofitBuilder.client(initClient())
        if (!converters.isNullOrEmpty()) {
            for (converter in converters) {
                retrofitBuilder.addConverterFactory(converter)
            }
        }

        if (callAdapter != null) {
            retrofitBuilder.addCallAdapterFactory(callAdapter!!)
        }

        retrofit = retrofitBuilder.build()
    }

    /*
        OKHttp创建
     */
    private fun initClient(): OkHttpClient {
        val okhttpBuilder: OkHttpClient.Builder =
            RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        if (!interceptors.isNullOrEmpty()) {
            for (interceptor in interceptors) {
                //仅在response调用一次
                okhttpBuilder.addInterceptor(interceptor)
                //request 和 response 都调用一次
                //okhttpBuilder.addNetworkInterceptor(interceptor)
            }
        }
        okhttpBuilder.addInterceptor(CacheDataInterceptor())
        okhttpBuilder.connectTimeout(timeOut, TimeUnit.SECONDS)
        okhttpBuilder.readTimeout(timeOut, TimeUnit.SECONDS)
        okhttpBuilder.writeTimeout(timeOut, TimeUnit.SECONDS)
        okhttpBuilder.sslSocketFactory(
            HttpsUtils.initSSLSocketFactory(),
            HttpsUtils.initTrustManager()
        )
        return okhttpBuilder.build()
    }

    /*
        具体服务实例化
     */
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}

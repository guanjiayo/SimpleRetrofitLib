package zs.xmx.retrofit

import android.app.Application
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import zs.xmx.network.config.NetWorkInit
import zs.xmx.retrofit.interceptor.BaseUrlInterceptor
import zs.xmx.retrofit.interceptor.HttpLoggerInterceptor

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述	  
 * @内容说明   
 *
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initNetwork()
    }

    private fun initNetwork() {
        NetWorkInit.newInstance(this)
            .withNativeApiHost("https://gank.io/api/v2/categories/")
            .withTimeOut(50)
            .withInterceptor(initLogInterceptor())
            .withInterceptor(BaseUrlInterceptor())
            .withConverter(GsonConverterFactory.create())
            .configure()
    }

    /**
     * 初始化日志拦截器
     */
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggerInterceptor())
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}
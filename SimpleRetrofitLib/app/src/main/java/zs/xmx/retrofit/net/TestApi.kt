package zs.xmx.retrofit.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import zs.xmx.network.ApiResponse
import zs.xmx.network.manager.RetrofitUrlManager
import zs.xmx.retrofit.TestBean
import zs.xmx.retrofit.constant.UrlConstant

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述
 * @内容说明   
 *
 */
interface TestApi {

    @GET("GanHuo")
    fun getGanHuo(): Call<ApiResponse<MutableList<TestBean>?>>

    //直接写BaseUrl拦截器写法
    //@Headers(UrlConstant.DOMAIN_BAIDU)
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + UrlConstant.DOMAIN_BAIDU)
    @GET("Article" + RetrofitUrlManager.DOMAIN_PATH_SIZE + 2)//替换到oldUrl后两段的写法
    fun getArticle(): Call<ApiResponse<Any?>>

    @Headers(UrlConstant.DOMAIN_DOUBAN)
    @GET("Girl")
    fun getGirl(): Call<ApiResponse<Any?>>
}
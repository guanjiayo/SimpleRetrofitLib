package zs.xmx.retrofit.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import zs.xmx.network.callback.ApiResponse
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
    fun getGanHuo(): Call<ApiResponse<Any?>>

    @Headers(UrlConstant.DOMAIN_BAIDU)
    @GET("Article")
    fun getArticle(): Call<ApiResponse<Any?>>

    @Headers(UrlConstant.DOMAIN_DOUBAN)
    @GET("Girl")
    fun getGirl(): Call<ApiResponse<Any?>>
}
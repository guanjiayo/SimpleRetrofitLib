package zs.xmx.network.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述	   通用的Api
 * @内容说明   
 *
 */
interface CommonApi {

    //@QueryMap 就是讲参数拼接到url后面
    @GET
    operator fun get(@Url url: String, @QueryMap params: WeakHashMap<String, Any>): Call<String>

    //post空参的情况
    @GET
    fun getNull(@Url url: String): Call<String>

    @FormUrlEncoded //数据以表达式添加 FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap params: WeakHashMap<String, Any>): Call<String>

    //post空参的情况
    @POST
    fun postNull(@Url url: String): Call<String>

    @FormUrlEncoded
    @PUT
    fun put(@Url url: String, @FieldMap params: WeakHashMap<String, Any>): Call<String>

    @DELETE
    fun delete(@Url url: String, @QueryMap params: WeakHashMap<String, Any>): Call<String>

    //下载是直接到内存,所以需要 @Streaming
    @Streaming
    @GET
    fun download(@Url url: String, @QueryMap params: WeakHashMap<String, Any>): Call<ResponseBody>

    //上传
    @Multipart
    @POST
    fun upload(@Url url: String, @Part file: MultipartBody.Part): Call<String>

    //原始数据
    //Content-Type为application/json使用这种方案
    @POST
    fun postRaw(@Url url: String, @Body body: RequestBody): Call<String>

    //@Body 讲参数放到请求体,一般适合post方式,上面如果报错,就改成这个
    @PUT
    fun putRaw(@Url url: String, @Body body: RequestBody): Call<String>

}
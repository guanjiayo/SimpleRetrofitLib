package zs.xmx.network.callback


import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import zs.xmx.network.ResultCode
import java.net.ConnectException


/**
 * 订阅者默认实现
 *
 */
@SuppressLint("RestrictedApi")
abstract class ApiCallback<T> : Callback<ApiResponse<T>> {

    //private val mainHandler = Handler(Looper.getMainLooper())

    abstract fun onSuccess(t: T?)

    open fun onFailed(code: Int, msg: String?) {}

    override fun onResponse(call: Call<ApiResponse<T>>, response: Response<ApiResponse<T>>) {
        ArchTaskExecutor.getMainThreadExecutor().execute {
            //已在主线程中，更新UI
            val result = response.body()
            if (response.isSuccessful && result != null) {
                if (result.status == ResultCode.Success) {
                    onSuccess(result.data)
                } else {
                    onFailed(result.status, result.message)
                }
            }
        }

    }

    override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
        ArchTaskExecutor.getMainThreadExecutor().execute {
            when (t) {
                is ConnectException -> onFailed(ResultCode.DisConnect, "Server Connect Exception") //网络连接失败
                is HttpException -> onFailed(t.code(), t.message())
                else -> onFailed(ResultCode.OtherException, t.message!!)
            }
        }
    }

}

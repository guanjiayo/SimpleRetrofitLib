package zs.xmx.retrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.network.RetrofitFactory
import zs.xmx.network.ApiCallback
import zs.xmx.network.manager.RetrofitUrlManager
import zs.xmx.retrofit.constant.UrlConstant
import zs.xmx.retrofit.databinding.ActivityMainBinding
import zs.xmx.retrofit.net.TestApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //{"baidu","http://www.baidu.com"}
        RetrofitUrlManager.getInstance().putDomain(UrlConstant.BADIDU, UrlConstant.DEF_BAIDU_URL)

        mBinding.releaseUrl.setOnClickListener {
            UrlConstant.setDefGankUrl(UrlConstant.DEF_DOUBAN_URL)
            //测试切换全局的BaseUrl
            Log.d("Logger", "BaseUrl:   " + UrlConstant.getDefGankUrl())
        }

        mBinding.request.setOnClickListener {
            RetrofitFactory.instance.create(TestApi::class.java).getGanHuo()
                .enqueue(object : ApiCallback<Any?>() {
                    override fun onSuccess(t: Any?) {
                        Log.d("HAHA", "GanHuo:   " + t.toString())
                    }

                    override fun onFailed(code: Int, msg: String?) {
                        Log.e("HAHA", "code:  $code       msg:   $msg ")
                    }
                })
        }
        mBinding.request2.setOnClickListener {
            RetrofitFactory.instance.create(TestApi::class.java).getArticle()
                .enqueue(object : ApiCallback<Any?>() {
                    override fun onSuccess(t: Any?) {
                        Log.d("HAHA", "Article:    " + t.toString())
                    }

                    override fun onFailed(code: Int, msg: String?) {
                        Log.e("HAHA", "code:  $code       msg:   $msg ")
                    }
                })
        }
        mBinding.request3.setOnClickListener {
            RetrofitFactory.instance.create(TestApi::class.java).getGirl()
                .enqueue(object : ApiCallback<Any?>() {
                    override fun onSuccess(t: Any?) {
                        Log.d("HAHA", "Girl:   " + t.toString())
                    }

                    override fun onFailed(code: Int, msg: String?) {
                        Log.e("HAHA", "code:  $code       msg:   $msg ")
                    }
                })
        }
    }
}

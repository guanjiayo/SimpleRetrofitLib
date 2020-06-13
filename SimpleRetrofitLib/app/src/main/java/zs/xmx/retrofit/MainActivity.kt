package zs.xmx.retrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.network.ApiCallback
import zs.xmx.network.RetrofitFactory
import zs.xmx.network.manager.CacheManager
import zs.xmx.network.manager.RetrofitUrlManager
import zs.xmx.retrofit.constant.UrlConstant
import zs.xmx.retrofit.databinding.ActivityMainBinding
import zs.xmx.retrofit.net.TestApi
import java.lang.reflect.ParameterizedType

//https://blog.csdn.net/qqyanjiang/article/details/51316116
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.button.setOnClickListener {
            val mutableListOf = mutableListOf(TestBean(), TestBean())
            CacheManager.save("aa", mutableListOf)

            val mutableList = CacheManager.getCache("aa") as MutableList<*>
            Log.e("aaaa", "" + mutableList.size)
        }

        //{"baidu","http://www.baidu.com"}
        RetrofitUrlManager.getInstance().putDomain(UrlConstant.BADIDU, UrlConstant.DEF_BAIDU_URL)

        mBinding.releaseUrl.setOnClickListener {
            UrlConstant.setDefGankUrl(UrlConstant.DEF_DOUBAN_URL)
            //测试切换全局的BaseUrl
            Log.d("Logger", "BaseUrl:   " + UrlConstant.getDefGankUrl())
        }

        mBinding.request.setOnClickListener {
            val ganHuo = RetrofitFactory.instance.create(TestApi::class.java).getGanHuo()

            RetrofitFactory.instance.create(TestApi::class.java).getGanHuo()
                .enqueue(object : ApiCallback<MutableList<TestBean>?>() {
                    override fun onSuccess(t: MutableList<TestBean>?) {
                        Log.d("HAHA", "GanHuo:   " + t?.size)
                    }

                    override fun onFailed(code: Int, msg: String?) {
                        Log.e("HAHA", "code:  $code       msg:   $msg ")
                    }

                    override fun onCacheSuccess(t: Any?) {
                        val mutableList = t as MutableList<*>?
                        Log.d("HAHA", "GanHuo:(onCacheSuccess)   " + mutableList?.size)
                        //        Log.d("HAHA", "GanHuo:(onCacheSuccess)   " + t.toString())
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

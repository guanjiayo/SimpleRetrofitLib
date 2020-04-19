package zs.xmx.retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import zs.xmx.network.RetrofitFactory
import zs.xmx.network.callback.ApiCallback
import zs.xmx.retrofit.constant.UrlConstant
import zs.xmx.retrofit.net.TestApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.releaseUrl).setOnClickListener {
            UrlConstant.setDefGankUrl(UrlConstant.DEF_DOUBAN_URL)
            //测试切换全局的BaseUrl
            Log.d("Logger", "BaseUrl:   " + UrlConstant.getDefGankUrl())
        }

        findViewById<Button>(R.id.request).setOnClickListener {
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
        findViewById<Button>(R.id.request2).setOnClickListener {
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
        findViewById<Button>(R.id.request3).setOnClickListener {
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

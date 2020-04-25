# SimpleRetrofitLib

正常的配置和使用

```
NetWorkInit.newInstance(this).withNativeApiHost(BaseConstant.URL_BASE)
                .withTimeOut(50)
                .withInterceptor(initLogInterceptor())
                .withInterceptor(AddHeadInterceptor())
                .withCallAdapter(RxJava2CallAdapterFactory.create())
                .withConverter(GsonConverterFactory.create())
                .configure()
 
  RetrofitFactory.instance.create(CartApi::class.java).getCartList()
 
```



动态切换BaseUrl

```
 1. 定义我们要替换baseUrl
 如    {"baidu","http://www.baidu.com"}
      RetrofitUrlManager.getInstance().putDomain(UrlConstant.BADIDU, UrlConstant.DEF_BAIDU_URL)
      
      
 2. 定义接口注解
 @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + UrlConstant.DOMAIN_BAIDU)
    @GET("Article" + RetrofitUrlManager.DOMAIN_PATH_SIZE + 2)//替换到oldUrl后两段的写法
    fun getArticle(): Call<ApiResponse<Any?>>
 
 3. 全局替换方式
 
  RetrofitUrlManager.getInstance().setGlobalDomain(UrlConstant.DEF_BAIDU_URL)
 
 ---
 
  RetrofitUrlManager.DOMAIN_PATH_SIZE + 2
  看自己需要加或不加都可以
 
  todo 补充处理GlobalDomain的方案
```


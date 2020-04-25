package zs.xmx.network.urlparse;

import okhttp3.HttpUrl;
import zs.xmx.network.manager.RetrofitUrlManager;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   默认解析器, 可根据自定义策略选择不同的解析器
 * @内容说明   如果后续有新的需求需要解决,同理实现 UrlParser,自己处理即可
 *
 */
public class DefaultUrlParser implements UrlParser {

    private UrlParser mDomainUrlParser;
    private volatile UrlParser mPathUrlParser;
    private RetrofitUrlManager mRetrofitUrlManager;

    @Override
    public void init(RetrofitUrlManager retrofitUrlManager) {
        this.mRetrofitUrlManager = retrofitUrlManager;
        this.mDomainUrlParser = new DomainUrlParser();
        this.mDomainUrlParser.init(retrofitUrlManager);
    }

    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl oldUrl) {
        if (null == domainUrl) return oldUrl;

        if (oldUrl.toString().contains(RetrofitUrlManager.DOMAIN_PATH_SIZE)) {
            if (mPathUrlParser == null) {
                synchronized (this) {
                    if (mPathUrlParser == null) {
                        mPathUrlParser = new PathUrlParser();
                        mPathUrlParser.init(mRetrofitUrlManager);
                    }
                }
            }
            return mPathUrlParser.parseUrl(domainUrl, oldUrl);
        }

        return mDomainUrlParser.parseUrl(domainUrl, oldUrl);
    }
}

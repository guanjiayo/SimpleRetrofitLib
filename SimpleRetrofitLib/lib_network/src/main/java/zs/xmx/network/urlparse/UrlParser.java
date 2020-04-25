package zs.xmx.network.urlparse;

import okhttp3.HttpUrl;
import zs.xmx.network.manager.RetrofitUrlManager;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   Url解析器
 * @内容说明
 *
 */
public interface UrlParser {

    /**
     * 这里可以做一些初始化操作
     */
    void init(RetrofitUrlManager retrofitUrlManager);

    /**
     * @param domainUrl 用于替换的 URL 地址
     * @param oldUrl    旧 URL 地址
     */
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl oldUrl);

}

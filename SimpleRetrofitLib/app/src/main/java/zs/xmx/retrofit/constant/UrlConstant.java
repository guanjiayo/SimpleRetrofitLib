package zs.xmx.retrofit.constant;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述
 * @内容说明
 *
 */
public class UrlConstant {
    public static final String DOMAIN_DOUBAN = "Domain:douban";
    public static final String DOMAIN_BAIDU = "Domain:baidu";

    public static final String DOUBAN = "douban";
    public static final String BADIDU = "baidu";

    public static String DEF_BAIDU_URL = "https://www.baidu.com/search";
    public static String DEF_DOUBAN_URL = "https://douban.com:443";
    public static String DEF_GANK_URL = "https://gank.io/api/v2/categories/";

    public static String getDefGankUrl() {
        return DEF_GANK_URL;
    }

    public static void setDefGankUrl(String defGankUrl) {
        DEF_GANK_URL = defGankUrl;
    }
}

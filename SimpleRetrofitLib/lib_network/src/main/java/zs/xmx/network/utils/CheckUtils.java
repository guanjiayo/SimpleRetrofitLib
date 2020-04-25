package zs.xmx.network.utils;

import android.text.TextUtils;

import okhttp3.HttpUrl;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述
 * @内容说明
 *
 */
public class CheckUtils {

    private CheckUtils() {
        throw new IllegalStateException("do not instantiation me");
    }

    public static HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new RuntimeException("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
        } else {
            return parseUrl;
        }
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}

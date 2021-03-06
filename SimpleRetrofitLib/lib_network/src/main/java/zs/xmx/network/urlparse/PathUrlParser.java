package zs.xmx.network.urlparse;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.HttpUrl;
import zs.xmx.network.manager.RetrofitUrlManager;

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述   用来替换oldUrl,host及后面' / '的字段
 * @内容说明
 *
 */
public class PathUrlParser implements UrlParser {

    private Map<String, String> mCache;

    @Override
    public void init(RetrofitUrlManager retrofitUrlManager) {
        this.mCache = new ConcurrentHashMap<>();
    }

    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        if (null == domainUrl) return url;

        HttpUrl.Builder builder = url.newBuilder();

        int pathSize = resolvePathSize(url, builder);

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url, pathSize)))) {
            for (int i = 0; i < url.pathSize(); i++) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0);
            }

            List<String> newPathSegments = new ArrayList<>();
            newPathSegments.addAll(domainUrl.encodedPathSegments());


            if (url.pathSize() > pathSize) {
                List<String> encodedPathSegments = url.encodedPathSegments();
                for (int i = pathSize; i < encodedPathSegments.size(); i++) {
                    newPathSegments.add(encodedPathSegments.get(i));
                }
            } else if (url.pathSize() < pathSize) {
                throw new IllegalArgumentException(String.format(
                        "Your final path is %s, the pathSize = %d, but the #baseurl_path_size = %d, #baseurl_path_size must be less than or equal to pathSize of the final path",
                        url.scheme() + "://" + url.host() + url.encodedPath(), url.pathSize(), pathSize));
            }

            for (String PathSegment : newPathSegments) {
                builder.addEncodedPathSegment(PathSegment);
            }
        } else {
            builder.encodedPath(mCache.get(getKey(domainUrl, url, pathSize)));
        }

        HttpUrl httpUrl = builder
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build();

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url, pathSize)))) {
            mCache.put(getKey(domainUrl, url, pathSize), httpUrl.encodedPath());
        }
        return httpUrl;
    }

    private String getKey(HttpUrl domainUrl, HttpUrl url, int PathSize) {
        return domainUrl.encodedPath() + url.encodedPath()
                + PathSize;
    }

    private int resolvePathSize(HttpUrl httpUrl, HttpUrl.Builder builder) {
        String fragment = httpUrl.fragment();

        int pathSize = 0;
        StringBuffer newFragment = new StringBuffer();

        if (fragment.indexOf("#") == -1) {
            String[] split = fragment.split("=");
            if (split.length > 1) {
                pathSize = Integer.parseInt(split[1]);
            }
        } else {
            if (fragment.indexOf(RetrofitUrlManager.DOMAIN_PATH_SIZE) == -1) {
                int index = fragment.indexOf("#");
                newFragment.append(fragment.substring(index + 1, fragment.length()));
                String[] split = fragment.substring(0, index).split("=");
                if (split.length > 1) {
                    pathSize = Integer.parseInt(split[1]);
                }
            } else {
                String[] split = fragment.split(RetrofitUrlManager.DOMAIN_PATH_SIZE);
                newFragment.append(split[0]);
                if (split.length > 1) {
                    int index = split[1].indexOf("#");
                    if (index != -1) {
                        newFragment.append(split[1].substring(index, split[1].length()));
                        String substring = split[1].substring(0, index);
                        if (!TextUtils.isEmpty(substring)) {
                            pathSize = Integer.parseInt(substring);
                        }
                    } else {
                        pathSize = Integer.parseInt(split[1]);
                    }
                }
            }
        }
        if (TextUtils.isEmpty(newFragment.toString())) {
            builder.fragment(null);
        } else {
            builder.fragment(newFragment.toString());
        }
        return pathSize;
    }
}

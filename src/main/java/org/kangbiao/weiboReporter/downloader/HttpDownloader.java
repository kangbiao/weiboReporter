package org.kangbiao.weiboReporter.downloader;

import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.CookieSpec;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bradykang on 4/21/2017.
 * 扩展downloader，增加支持对每个请求设置请求头，Request中设置的请求头优先级大于Site中设置的请求头
 */
public class HttpDownloader extends HttpClientDownloader {

    protected HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers,HttpHost proxy) {
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
        Map<String ,String> allHeaders=new HashMap<String, String>();
        if (headers!=null){
            allHeaders.putAll(headers);
        }
        Object object=request.getExtra("headers");
        if (object!=null){
            Map map= (Map) object;
            for (Object key:map.keySet()){
                allHeaders.put(String.valueOf(key),String.valueOf(map.get(key)));
            }
        }
        for (Map.Entry<String, String> headerEntry : allHeaders.entrySet()) {
            requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectTimeout(site.getTimeOut())
                .setCookieSpec(CookieSpecs.BEST_MATCH);
        if (proxy !=null) {
            requestConfigBuilder.setProxy(proxy);
            request.putExtra(Request.PROXY, proxy);
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }
}

package org.kangbiao.weiboReporter.processer;

import org.kangbiao.weiboReporter.entity.PageType;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 4/19/2017.
 * 微博动态处理器
 */
public class WeiboFeedProcessor implements WeiboProcessor{
    public void process(Page page) {
        Map extras=page.getRequest().getExtras();
        String uid= String.valueOf(extras.get("uid"));
        boolean calPage= Boolean.parseBoolean(String.valueOf(extras.get("calPage")));
        String containerId= String.valueOf(extras.get("containerid"));
        if (calPage) {
            String url = "http://m.weibo.cn/api/container/getIndex?type=uid&value=%s&containerid=%s&page=%s";
            Integer total = Integer.valueOf(page.getJson().jsonPath("$.cardlistInfo.total").get());
            Map<String, Object> pageExtrasMap = new HashMap<String, Object>();
            pageExtrasMap.put("pageType", PageType.WEIBO_FEED);
            pageExtrasMap.put("calPage",false);
            for (int i = 2; i <= total / 10 + 1; i++) {
                Request request = new Request(String.format(url, uid, containerId, i));
                request.setExtras(pageExtrasMap);
                page.addTargetRequest(request);
            }
        }
        List<String> ids=page.getJson().jsonPath(".cards[*].mblog.id").all();
        for (String id:ids){
            String url = "http://m.weibo.cn/api/comments/show?id=%s&page=%s";
            Map<String, Object> pageExtrasMap = new HashMap<String, Object>();
            pageExtrasMap.put("pageType", PageType.WEIBO_COMMENT);
            pageExtrasMap.put("calPage",true);
            pageExtrasMap.put("commentId",id);
            Request request = new Request(String.format(url, id,1));
            request.setExtras(pageExtrasMap);
            page.addTargetRequest(request);
            System.out.println("feedId:"+id);
        }
    }
}
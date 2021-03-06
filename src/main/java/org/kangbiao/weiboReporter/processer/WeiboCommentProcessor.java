package org.kangbiao.weiboReporter.processer;

import com.alibaba.fastjson.JSON;
import org.kangbiao.weiboReporter.entity.PageType;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 4/19/2017.
 * 微博评论处理器
 */
public class WeiboCommentProcessor implements WeiboProcessor{
    public void process(Page page) {
        if (Integer.valueOf(page.getJson().jsonPath("$.ok").get())==0){
            System.out.println(JSON.toJSON(page.getRequest()));
            page.setSkip(true);
            return;
        }
        Map extras=page.getRequest().getExtras();
        String commentId= String.valueOf(extras.get("commentId"));
        boolean calPage= Boolean.parseBoolean(String.valueOf(extras.get("calPage")));
        if (calPage) {
            String url = "http://m.weibo.cn/api/comments/show?id=%s&page=%s";
            Integer total = Integer.valueOf(page.getJson().jsonPath("$.total_number").get());
            Map<String, Object> pageExtrasMap = new HashMap<String, Object>();
            pageExtrasMap.put("pageType", PageType.WEIBO_COMMENT);
            pageExtrasMap.put("calPage",false);
            for (int i = 2; i < total / 10 + 1; i++) {
                Request request = new Request(String.format(url,commentId,i));
                request.setExtras(pageExtrasMap);
                page.addTargetRequest(request);
            }
        }
        try {
            List<String> uids = page.getJson().jsonPath("$.data[*].user.id").all();
            for (String uid : uids) {
                String url = "http://m.weibo.cn/api/container/getIndex?containerid=230283%s_-_INFO";
                Map<String, Object> pageExtrasMap = new HashMap<String, Object>();
                pageExtrasMap.put("pageType", PageType.USER_PROFILE);
                Request request = new Request(String.format(url, uid));
                request.setExtras(pageExtrasMap);
                page.addTargetRequest(request);
            }
        }catch (Exception e){
            System.out.println(page.getJson());
        }
        page.putField("url",page.getRequest().getUrl());
        page.putField("response",page.getRawText());
        page.putField("type",PageType.WEIBO_COMMENT);
    }
}

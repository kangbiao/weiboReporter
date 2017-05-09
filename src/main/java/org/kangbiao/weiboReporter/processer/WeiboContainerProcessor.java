package org.kangbiao.weiboReporter.processer;

import com.google.gson.reflect.TypeToken;
import org.kangbiao.weiboReporter.entity.PageType;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import org.kangbiao.weiboReporter.util.JsonUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bradykang on 4/19/2017.
 * 微博页面容器container处理器
 */
public class WeiboContainerProcessor implements WeiboProcessor{

    public void process(Page page) {
        Map extras=page.getRequest().getExtras();
        String  uid= String.valueOf(extras.get("uid"));
        List<String> tabs= page.getJson().jsonPath("$.tabsInfo.tabs").all();
        Type type=new TypeToken<Map<String,String>>(){}.getType();
        for (String tabString:tabs){
            Map<String,String> tabMap= JsonUtil.fromJson(tabString, type);
            if (tabMap.get("tab_type").equals("profile")){
                System.out.println(tabString);
            }
            else if (tabMap.get("tab_type").equals("weibo")){
                String url = "http://m.weibo.cn/api/container/getIndex?type=uid&value=%s&containerid=%s";
                Request request=new Request(String.format(url,uid,tabMap.get("containerid")));
                Map<String, Object> pageExtrasMap=new HashMap<String, Object>();
                pageExtrasMap.put("pageType",PageType.WEIBO_FEED);
                pageExtrasMap.put("calPage",true);
                pageExtrasMap.put("uid",uid);
                pageExtrasMap.put("containerid",tabMap.get("containerid"));
                request.setExtras(pageExtrasMap);
                page.addTargetRequest(request);
            }
        }
        page.setSkip(true);
    }
}
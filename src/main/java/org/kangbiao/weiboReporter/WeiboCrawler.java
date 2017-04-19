package org.kangbiao.weiboReporter;

import org.kangbiao.weiboReporter.entity.PageType;
import org.kangbiao.weiboReporter.entity.WeiboConfig;
import org.kangbiao.weiboReporter.processer.WeiboProcessorContext;
import org.kangbiao.weiboReporter.schduler.FileScheduler;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bradykang on 3/29/2017.
 * 微博爬虫启动入口
 */
public class WeiboCrawler implements PageProcessor {

    private Site site;
    private WeiboProcessorContext weiboProcessorContext;

    public void process(Page page) {
        PageType pageType= PageType.fromObject(page.getRequest().getExtras().get("pageType"));
        weiboProcessorContext.process(pageType,page);
    }

    public Site getSite() {
       return this.site;
    }

    public static void main(String[] args) {
        try {
            WeiboCrawler weiboCrawler=new WeiboCrawler();
            WeiboConfig weiboConfig=new WeiboConfig();
            weiboCrawler.site=weiboConfig.getSite();
            weiboCrawler.weiboProcessorContext=new WeiboProcessorContext();
            String url="http://m.weibo.cn/api/container/getIndex?type=uid&value=%s";
            Request request=new Request(String.format(url,weiboConfig.getUid()));
            Map<String, Object> pageExtrasMap=new HashMap<String, Object>();
            pageExtrasMap.put("pageType",PageType.CONTAINER_ID);
            pageExtrasMap.put("uid",weiboConfig.getUid());
            request.setExtras(pageExtrasMap);
            Spider.create(weiboCrawler)
                    .addRequest(request)
                    .addPipeline(new ConsolePipeline())
                    .thread(weiboConfig.getThreadNum())
                    .setScheduler(new FileScheduler("C:\\Users\\I337077\\Desktop\\url.txt"))
                    .run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import entity.PageType;
import entity.WeiboConfig;
import processer.WeiboProcessorContext;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bradykang on 3/29/2017.
 *
 */
public class WeiboCrawler implements PageProcessor {

    private Site site;

    public void process(Page page) {
        PageType pageType= (PageType) page.getRequest().getExtras().get("pageType");
        WeiboProcessorContext weiboProcessorContext=new WeiboProcessorContext(pageType);
        weiboProcessorContext.process(page);
    }

    public Site getSite() {
       return this.site;
    }

    public static void main(String[] args) {
        try {
            WeiboCrawler weiboCrawler=new WeiboCrawler();
            WeiboConfig weiboConfig=new WeiboConfig();
            weiboCrawler.site=weiboConfig.getSite();
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
                    .run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
